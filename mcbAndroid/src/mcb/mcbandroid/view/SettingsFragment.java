package mcb.mcbandroid.view;

import mcb.mcbandroid.SettingsActivity;
import mcb.util.i18nString.Language;
import mcb.mcbandroid.R;
import android.os.Bundle;
import android.preference.ListPreference;

public class SettingsFragment extends android.preference.PreferenceFragment {
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        ListPreference lp = (ListPreference)findPreference(SettingsActivity.KEY_LANGUAGE);
        lp.setEntries(getLanguageList());
        lp.setEntryValues(getLanguageList());        
    }
    
    static public CharSequence[] getLanguageList() {
        CharSequence[] entries = new CharSequence[ Language.values().length ];
        
        for ( int i = 0 ; i < Language.values().length ; i++ ) {
        	Language l = Language.values()[i];
        	entries[i] = l.getName().getValue();
        }
        
        return entries;
    }
    
    static public CharSequence[] getLanguageValueList() {
        CharSequence[] entryValues = new CharSequence[ Language.values().length ];
        
        for ( int i = 0 ; i < Language.values().length ; i++ ) {
        	Language l = Language.values()[i];
        	entryValues[i] = l.toString();
        }
        
        return entryValues;
    }
}
