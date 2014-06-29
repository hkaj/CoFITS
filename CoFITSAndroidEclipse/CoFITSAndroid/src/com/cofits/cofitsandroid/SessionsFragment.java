package com.cofits.cofitsandroid;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class SessionsFragment extends Fragment {

    private ListView parametersListSessions;

    public static String SESSION_NAME = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sessions, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        parametersListSessions = (ListView) getView().findViewById(R.id.listView_sessions);

        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("sessionname", "Session 1");
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("sessionname", "Session 2");
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("sessionname", "Session 3");
        listItem.add(map);

        SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.item_session,
                new String[]{"sessionname"}, new int[]{R.id.item_sessionname});

        parametersListSessions.setAdapter(mSchedule);

        parametersListSessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long myLong) {
                String selectedFromList = parametersListSessions.getItemAtPosition(myItemInt).toString();
                String[] morceaux = selectedFromList.split("=");

                System.out.println("Session selected: "+ selectedFromList );
                System.out.println("01"+morceaux[0]);
                System.out.println("Session name: "+morceaux[1].substring(0, morceaux[1].length() -1));

                SESSION_NAME = morceaux[1].substring(0, morceaux[1].length() -1);

                // Create new fragment and transaction
                Fragment newFragment = new FilesFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragment_sessions_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                ((MainActivity) getActivity()).setActionBarTitle("Files");

            }
        });
    }
}
