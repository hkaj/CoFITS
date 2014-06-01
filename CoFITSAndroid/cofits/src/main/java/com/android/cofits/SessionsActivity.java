package com.android.cofits;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SessionsActivity extends ActionBarActivity {

    private TabHost tabHost;
    private ListView parametersListSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        tabHost.setCurrentTabByTag("tab_local");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String arg0) {

                if ("tab_local".equals(tabHost.getCurrentTabTag())) {

                    parametersListSessions = (ListView) findViewById(R.id.listView_sessions);

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

                    SimpleAdapter mSchedule = new SimpleAdapter(SessionsActivity.this.getBaseContext(), listItem, R.layout.item_session,
                            new String[]{"sessionname"}, new int[]{R.id.item_sessionname});

                    parametersListSessions.setAdapter(mSchedule);
                }
            }
        });

        TabHost.TabSpec spec = tabHost.newTabSpec("tab_local");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.tab_session));
        tabHost.addTab(spec);

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

        parametersListSessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long myLong) {
                //String selectedFromList = (String) (parametersListSessions.getItemAtPosition(myItemInt));
                Intent intent = new Intent(SessionsActivity.this, FilesActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sessions, container, false);
            return rootView;
        }
    }

}
