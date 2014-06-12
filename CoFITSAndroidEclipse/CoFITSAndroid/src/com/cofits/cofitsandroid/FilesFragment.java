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
        parametersListFiles = (ListView) getView().findViewById(R.id.listView_files);
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("filename", "File_1.png");
        map.put("file_icon",Integer.toString(R.drawable.ic_file_png));
        map.put("delete_icon",Integer.toString(R.drawable.ic_action_delete));
        map.put("download_icon",Integer.toString(R.drawable.ic_file_download));
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("filename", "File_2.doc");
        map.put("file_icon",Integer.toString(R.drawable.ic_file_doc));
        map.put("delete_icon",Integer.toString(R.drawable.ic_action_delete));
        map.put("download_icon",Integer.toString(R.drawable.ic_file_download));
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("filename", "File_4.txt");
        map.put("file_icon",Integer.toString(R.drawable.ic_file_txt));
        map.put("delete_icon",Integer.toString(R.drawable.ic_action_delete));
        map.put("download_icon",Integer.toString(R.drawable.ic_file_download));
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("filename", "File_5.jpg");
        map.put("file_icon",Integer.toString(R.drawable.ic_file_jpg));
        map.put("delete_icon",Integer.toString(R.drawable.ic_action_delete));
        map.put("download_icon",Integer.toString(R.drawable.ic_file_download));
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("filename", "File_3.pdf");
        map.put("file_icon",Integer.toString(R.drawable.ic_file_pdf));
        map.put("delete_icon",Integer.toString(R.drawable.ic_action_delete));
        map.put("download_icon",Integer.toString(R.drawable.ic_file_download));
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("filename", "File_6.xml");
        map.put("file_icon",Integer.toString(R.drawable.ic_file_other));
        map.put("delete_icon",Integer.toString(R.drawable.ic_action_delete));
        map.put("download_icon",Integer.toString(R.drawable.ic_file_download));
        listItem.add(map);


        SpecialAdapter mSchedule = new SpecialAdapter(getActivity(), listItem, R.layout.item_file,
                new String[]{"filename","file_icon","delete_icon","download_icon"}, new int[]{R.id.item_filename,R.id.icon,R.id.fileDeleteButton,R.id.fileDownloadButton});


        parametersListFiles.setAdapter(mSchedule);

        ((MainActivity) getActivity()).getmDrawerToggle().setDrawerIndicatorEnabled(true);

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
