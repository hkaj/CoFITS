package com.cofits.cofitsandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesFragment extends Fragment {

    private ListView parametersListFiles;

    public static String FILE_NAME = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();
        
        this.parametersListFiles = (ListView) getView().findViewById(R.id.listView_files);
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        String file1 = "File_1.png";
        String file2 = "File_2.doc";
        String file3 = "File_3.txt";
        String file4 = "File_4.jpg";
        String file5 = "File_5.pdf";
        String file6 = "File_6.xml";

        addRow(listItem, file1);
        addRow(listItem, file2);
        addRow(listItem, file3);
        addRow(listItem, file4);
        addRow(listItem, file5);
        addRow(listItem, file6);

        SpecialAdapter mSchedule = new SpecialAdapter(getActivity(), listItem, R.layout.item_file,
                new String[]{"filename","file_icon","delete_icon","download_icon"}, new int[]{R.id.item_filename,R.id.icon,R.id.fileDeleteButton,R.id.fileDownloadButton});

        parametersListFiles.setAdapter(mSchedule);

        ((MainActivity) getActivity()).getmDrawerToggle().setDrawerIndicatorEnabled(true);

    }

	private void addRow(ArrayList<HashMap<String, String>> listItem,
			String file) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("filename", file);
        map.put("file_icon",getFileIcon(file));
        map.put("delete_icon",Integer.toString(R.drawable.ic_action_delete));
        map.put("download_icon",Integer.toString(R.drawable.ic_file_download));
        listItem.add(map);
	}
	private String getFileIcon(String fileName){
		String fileIcon = "";
		String extension = "";
		System.out.println("extension" + fileName);
        String[] morceaux = fileName.split("\\.");
		extension = morceaux[morceaux.length-1];
		System.out.println("extension" + extension);
		if (extension.equals("pdf")) fileIcon = Integer.toString(R.drawable.ic_file_pdf);
		else if (extension.equals("png")) fileIcon = Integer.toString(R.drawable.ic_file_png);
		else if (extension.equals("jpg")) fileIcon = Integer.toString(R.drawable.ic_file_jpg);
		else if (extension.equals("doc")) fileIcon = Integer.toString(R.drawable.ic_file_doc);
		else if (extension.equals("txt")) fileIcon = Integer.toString(R.drawable.ic_file_txt);
		else fileIcon = Integer.toString(R.drawable.ic_file_other);
		
		System.out.println("extension" + fileIcon);

		return fileIcon;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.files, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(getActivity(), AddFileActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        DrawerLayout dr = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ListView dl = (ListView) getActivity().findViewById(R.id.left_drawer);
        if (dr.isDrawerOpen(dl)) {
            menu.findItem(R.id.action_add).setVisible(false);
        } else menu.findItem(R.id.action_add).setVisible(true);

        super.onPrepareOptionsMenu(menu);
    }


    public class SpecialAdapter extends SimpleAdapter  {
        private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
        private List<HashMap<String, String>> items;

        public SpecialAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
            super(context, items, resource, from, to);
            this.items = items;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
/*            int colorPos = position % colors.length;
            view.setBackgroundColor(colors[colorPos]);*/

            ImageButton buttonDownoald = (ImageButton) view.findViewById(R.id.fileDownloadButton);
            buttonDownoald.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FILE_NAME = String.valueOf(items.get(position).get("filename"));
                    System.out.println("FILE_NAME =" + FILE_NAME);
                    Toast.makeText(getActivity(),"Download clicked" + position + FILE_NAME, Toast.LENGTH_SHORT).show();
                }
            });

            ImageButton buttonDelete = (ImageButton) view.findViewById(R.id.fileDeleteButton);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"Delete clicked!" + position + FILE_NAME, Toast.LENGTH_SHORT).show();

                    System.out.println(String.valueOf(items.get(position).get("filename")));

                    FILE_NAME = String.valueOf(items.get(position).get("filename"));
                    System.out.println("FILE_NAME =" + FILE_NAME);

                    ClientAgent clientAgent = AgentProvider.getInstance().getMyAgent();
                    System.out.println("clientAgent =" + clientAgent.getLocalName());
                    clientAgent.addBehaviour(new DeleteFileBehaviour());
                }
            });


            return view;
        }

    }

}
