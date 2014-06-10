package com.android.cofits;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

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
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("filename", "File_2.ppt");
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("filename", "File_3.pdf");
        listItem.add(map);

        SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.item_file,
                new String[]{"filename"}, new int[]{R.id.item_filename});

        parametersListFiles.setAdapter(mSchedule);

        parametersListFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long myLong) {
                myView.setBackgroundColor(Color.CYAN);

                String selectedFromList = parametersListFiles.getItemAtPosition(myItemInt).toString();

                String[] morceaux = selectedFromList.split("=");

                System.out.println("File selected: "+ selectedFromList );
                System.out.println("01"+morceaux[0]);
                System.out.println("File name: "+morceaux[1].substring(0, morceaux[1].length() -1));

                FILE_NAME = morceaux[1].substring(0, morceaux[1].length() -1);
                System.out.println("FILE_NAME =" + FILE_NAME);

                //String selectedFromList = (String) (parametersListSessions.getItemAtPosition(myItemInt));
                Intent intent = new Intent(getActivity(), FileViewActivity.class);
                startActivity(intent);
            }
        });
        parametersListFiles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                Toast.makeText(getActivity(), "Long Clicked : ", Toast.LENGTH_LONG).show();


                Object itemAtPosition = parametersListFiles.getItemAtPosition(position);
                if(itemAtPosition instanceof HashMap<?, ?>) {
                    HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;


                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setTitle(map.get("title"));
                    final String[] types = {"Delete File"};
                    adb.setItems(types, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String selectedFromList = parametersListFiles.getItemAtPosition(position).toString();

                            String[] morceaux = selectedFromList.split("=");

                            System.out.println("File selected: "+ selectedFromList );
                            System.out.println("01"+morceaux[0]);
                            System.out.println("File name: "+morceaux[1].substring(0, morceaux[1].length() -1));

                            FILE_NAME = morceaux[1].substring(0, morceaux[1].length() -1);
                            System.out.println("FILE_NAME =" + FILE_NAME);

                            ClientAgent clientAgent = AgentProvider.getInstance().getMyAgent();
                            System.out.println("clientAgent =" + clientAgent.getLocalName());
                            clientAgent.addBehaviour(new DeleteFileBehaviour());

                        }
                    });

                    adb.show();
                }
                return false;
            }
        });

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

}
