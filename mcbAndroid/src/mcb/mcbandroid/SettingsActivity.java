package mcb.mcbandroid;

import mcb.mcbandroid.view.SettingsFragment;
import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
    
	public static final String KEY_USERNAME = "pref_username";
	public static final String KEY_USERNAME_ALIAS = "pref_username_alias";
	public static final String KEY_LANGUAGE = "pref_language";
	
	public static final String KEY_JADE_HOST = "pref_jade_host";
	public static final String KEY_JADE_PORT = "pref_jade_port";
	
	public static final String KEY_MEMORAE_ENABLED = "pref_memorae_enabled";
	public static final String KEY_MEMORAE_USERNAME = "pref_memorae_username";
	public static final String KEY_MEMORAE_PASSWORD = "pref_memorae_password";
	public static final String KEY_MEMORAE_HOST = "pref_memorae_host";
	
	public static final String KEY_TRANSLATOR_BING = "pref_translator_bing";
	public static final String KEY_TRANSLATOR_BINGCLIENT = "pref_bing_client";
	public static final String KEY_TRANSLATOR_BINGSECRET = "pref_bing_secret";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getFragmentManager().beginTransaction()
        	.replace(android.R.id.content, new SettingsFragment(), "settings")
            .commit();
    }

    @Override
    protected void onStop() {
    	Application app = (Application) getApplication();
    	app.onSettingsUpdated();
    	super.onStop();
    }
}