package com.android.cofits;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectsFragment extends Fragment {
    private ListView parametersListProjects;
    public static String PROJECT_NAME = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        parametersListProjects = (ListView) getView().findViewById(R.id.listView_projects);

        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("projectname", "Project 1");
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("projectname", "Project 2");
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("projectname", "Project 3");
        listItem.add(map);

/*        SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.item_project,
                new String[]{"projectname"}, new int[]{R.id.item_projectname});*/

        SpecialAdapter mSchedule = new SpecialAdapter(getActivity(), listItem, R.layout.item_project,
                new String[]{"projectname"}, new int[]{R.id.item_projectname});


        parametersListProjects.setAdapter(mSchedule);

        parametersListProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long myLong) {

                String selectedFromList = parametersListProjects.getItemAtPosition(myItemInt).toString();
                String[] morceaux = selectedFromList.split("=");

                System.out.println("Project selected: "+ selectedFromList );
                System.out.println("01"+morceaux[0]);
                System.out.println("Project name: "+morceaux[1].substring(0, morceaux[1].length() -1));

                PROJECT_NAME = morceaux[1].substring(0, morceaux[1].length() -1);


                // Create new fragment and transaction
                Fragment newFragment = new SessionsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                transaction.replace(R.id.fragment_projects_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

                ((MainActivity) getActivity()).setActionBarTitle("Sessions");
            }
        });

/*        for(int i=0;i<parametersListProjects.getChildCount();i++){
            String item = parametersListProjects.getItemAtPosition(i).toString();
            String[] morceaux = item.split("=");

            String ProjectName = morceaux[1].substring(0, morceaux[1].length() -1);
            if(ProjectName == PROJECT_NAME){
                parametersListProjects.getChildAt(i).setBackgroundColor(Color.GREEN);
            }
            else {
                parametersListProjects.getChildAt(i).setBackgroundColor(Color.RED);
            }

        }*/
    }

    public class SpecialAdapter extends SimpleAdapter {
        private int[] colors = new int[] { 0x30FF0000, 0x300000FF };

        public SpecialAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
            super(context, items, resource, from, to);
        }
/*        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            int colorPos = position % colors.length;
            view.setBackgroundColor(colors[colorPos]);
            return view;
        }*/
    }
}
