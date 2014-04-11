package mcb.mcbandroid;


import java.beans.PropertyChangeEvent;

import mcb.debug.Logger;
import mcb.mcbandroid.agents.PersonalAgent;
import mcb.mcbandroid.utils.MemoraeHandler;
import mcb.mcbandroid.view.ConceptGridFragment;
import mcb.mcbandroid.view.GridFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import mcb.mcbandroid.R;

public class MainActivity extends Activity {

    public static final int SETTINGS_REQUEST 				= 1;
    public static final int CLEARMODEL_REQUEST 				= 2;
    public static final int DISCONNECTMEETING_REQUEST 		= 3;
    
    public static final String FRAGMENTMANAGER_GRIDTAG 		= "grid";
    public static final String FRAGMENTMANAGER_MEMORAE 		= "memorae";
	public static final String FRAGMENTMANAGER_EDIT 		= "postit";
    
    protected BroadcastReceiver receiver;
    protected IntentFilter iFilter;
    protected Application app = null;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
        app = ((Application)getApplication());
        app.setFragmentManager(getFragmentManager(),findViewById(R.id.main));
        
        if ( savedInstanceState == null ) {
	        GridFragment gridFragment = new GridFragment();
			getFragmentManager().beginTransaction()
				.replace(R.id.mainFragment, gridFragment, FRAGMENTMANAGER_GRIDTAG)
				.commit();
			
	        /* register custom receiver */
	        receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
	                Application app = (Application) MainActivity.this.getApplication();
	                String action = intent.getAction();
					
	                if ( action.equals(Application.LOCK_FAILURE) ) {
						View view = findViewById(R.id.mainFragment).getRootView();
						app.onLockFailureForEdit(view);
	                }
					else if ( action.equals(MemoraeHandler.INTENT) ) {
						String event = intent.getStringExtra(MemoraeHandler.EVENT);
						Logger.debug(MemoraeHandler.INTENT, "event: " + event);
						
						View view = findViewById(R.id.main).getRootView();
						app.onMemoraeEvent(view, event, intent.getExtras());
					}
					else if ( action.equals(PersonalAgent.INTENT) ) {
						String event = intent.getStringExtra(PersonalAgent.EVENT);
						Logger.debug(PersonalAgent.INTENT, "event: " + event);
						
						View view = findViewById(R.id.mainFragment).getRootView();
						app.onMeetingEvent(view, event, intent.getExtras());
					}
					else if ( action.equals(PersonalAgent.INTENT_MODEL_UPDATE) ) {
						PropertyChangeEvent pce = (PropertyChangeEvent) intent.getExtras().get(PersonalAgent.EVENT);
						Logger.debug(PersonalAgent.INTENT_MODEL_UPDATE, "event: " + pce.getPropertyName());
						app.propertyChange(pce);
					}
				}
	        };
	        iFilter = new IntentFilter();
	        iFilter.addAction(Application.LOCK_FAILURE);
	        iFilter.addAction(MemoraeHandler.INTENT);
	        iFilter.addAction(PersonalAgent.INTENT);
	        iFilter.addAction(PersonalAgent.INTENT_MODEL_UPDATE);
	        registerReceiver(receiver, iFilter);
        }
    }
    
    @Override
    protected void onResume() {
	    registerReceiver(receiver, iFilter);
    	
        Application app = ((Application)getApplication());
        app.onResume(findViewById(R.id.mainFragment));
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	unregisterReceiver(receiver);
    	super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);    	
        menu.findItem(R.id.menu_change_memorae).setVisible( sharedPref.getBoolean(SettingsActivity.KEY_MEMORAE_ENABLED, false) );
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_settings:
    		Logger.debug("menu","settings");
            Intent showSettings = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivityForResult(showSettings, SETTINGS_REQUEST);
            return true;
        case R.id.menu_about: {
    		Logger.debug("menu","about");
    		String str = getResources().getText(R.string.about) + " " + getResources().getText(R.string.version);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(str)
                   .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                       }
                   })
                   .show();
            return true;
        }
        case R.id.menu_clear_model: {
    		Logger.debug("menu","clear model");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.confirm_clear_local_model)
                   .setPositiveButton(R.string.confirm_clear, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                   			app.onClearModel();
                       }
                   })
                   .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                       }
                   })
                   .show();
            return true;
        }
        case R.id.menu_disconnect_meeting:
    		Logger.debug("menu","disconnect from meeting");
    		app.onDisconnectMeeting(findViewById(R.id.mainFragment));
            return true;
        case R.id.menu_change_memorae:
    		Logger.debug("menu","change memorae");
    		app.onConceptUnselect( findViewById(R.id.main) );
    		return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed() {
		Application app = (Application) getApplication();
    	
		if ( getFragmentManager().findFragmentById(R.id.mainFragment) instanceof GridFragment ) {
    		if ( app.onGoUp(findViewById(R.id.mainFragment)) == true )
    			return;
    	}
		else if ( getFragmentManager().findFragmentById(R.id.mainFragment) instanceof ConceptGridFragment ) {
    		if ( app.onGoUp(findViewById(R.id.mainFragment)) == false )
    			finish();
    		return;
    	}
    	
    	super.onBackPressed();
    }
}
