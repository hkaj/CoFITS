package com.android.cofits;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashMap;

public class FilesActivity extends ActionBarActivity {

    private TabHost tabHost;
    private ListView parametersListFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        tabHost.setCurrentTabByTag("tab_local");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String arg0) {

                if ("tab_local".equals(tabHost.getCurrentTabTag())) {

                    parametersListFiles = (ListView) findViewById(R.id.listView_files);

                    ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("filename", "File 1");
                    listItem.add(map);

                    map = new HashMap<String, String>();
                    map.put("filename", "File 2");
                    listItem.add(map);

                    map = new HashMap<String, String>();
                    map.put("filename", "File 3");
                    listItem.add(map);

                    SimpleAdapter mSchedule = new SimpleAdapter(FilesActivity.this.getBaseContext(), listItem, R.layout.item_file,
                            new String[]{"filename"}, new int[]{R.id.item_filename});

                    parametersListFiles.setAdapter(mSchedule);
                }
            }
        });

        TabHost.TabSpec spec = tabHost.newTabSpec("tab_local");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.tab_file));
        tabHost.addTab(spec);

        parametersListFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long myLong) {
                //String selectedFromList = (String) (parametersListSessions.getItemAtPosition(myItemInt));
                Intent intent = new Intent(FilesActivity.this, FileViewActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.files, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(FilesActivity.this, AddFileActivity.class);
            startActivity(intent);
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
            View rootView = inflater.inflate(R.layout.fragment_files, container, false);
            return rootView;
        }
    }

}
