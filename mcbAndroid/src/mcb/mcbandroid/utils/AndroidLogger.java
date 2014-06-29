package mcb.mcbandroid.utils;

import mcb.debug.Logger;
import android.util.Log;


public class AndroidLogger extends Logger {
	public void out(String level, String tag, Object[] ss ){
		if(ignoreLevel.contains(level)) return;
		if(ignoreTags.contains(tag)) return;
		
		String stream = "";
		
		for(Object s:ss){ 
			if(s == null)  
				stream += "null ";
			else 
				stream += s.toString() + " ";
		}

		if ( level.equalsIgnoreCase("debug") )
			Log.d(tag, stream);
		else if ( level.equalsIgnoreCase("info") )
			Log.i(tag, stream);
		else if ( level.equalsIgnoreCase("warning") )
			Log.w(tag, stream);
		else if ( level.equalsIgnoreCase("error") )
			Log.e(tag, stream);
	}
}
