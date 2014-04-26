package com.android.cofits;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectsActivity extends ActionBarActivity {
    private TabHost tabHost;
    private ListView parametersListProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        tabHost.setCurrentTabByTag("tab_local");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String arg0) {

                if ("tab_local".equals(tabHost.getCurrentTabTag())) {

                    parametersListProjects = (ListView) findViewById(R.id.listView_projects);

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

                    SimpleAdapter mSchedule = new SimpleAdapter(ProjectsActivity.this.getBaseContext(), listItem, R.layout.item_project,
                            new String[]{"projectname"}, new int[]{R.id.item_projectname});

                    parametersListProjects.setAdapter(mSchedule);
                }
            }
        });

        TabHost.TabSpec spec = tabHost.newTabSpec("tab_local");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.tab_project));
        tabHost.addTab(spec);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_projects, container, false);
            return rootView;
        }
    }

}
