package mcb.mcbandroid.utils;

import java.util.HashMap;

import mcb.debug.Logger;
import mcb.mcbandroid.Application;
import mcb.translator.TranslatorInterface;
import mcb.util.i18nString.Language;

import android.os.AsyncTask;

public class AsyncTranslator implements TranslatorInterface {

	protected Application app = null;
	protected TranslatorInterface ti = null;
	
	protected class LLTKey {
		public Language from, to;
		public String text;
		
		@Override
		public boolean equals(Object o) {
			if ( o instanceof LLTKey ) {
				LLTKey oo = (LLTKey) o;
				return oo.from.equals(from) && oo.to.equals(to) && oo.text.equals(text);
			}
			
			return false;
		}
		
		@Override
		public int hashCode() {
			String s = from + "/" + to + "/" + text;
			return s.hashCode();
		}
	}
	
	protected HashMap<LLTKey, String> cache = new HashMap<LLTKey, String>();
	
	public AsyncTranslator(TranslatorInterface ti, Application app) {
		this.ti = ti;
		this.app = app;
	}
	
	public String translate(String text, Language from, Language to) {
		final LLTKey key = new LLTKey();
		key.from = from;
		key.to = to;
		key.text = text;
		
		if ( cache.containsKey(key) ) {
			return cache.get(key);
		}
		else {
			// Async translate
			AsyncTask<Void, Void, String> as = new AsyncTask<Void, Void, String>() {
				@Override
				protected void onPreExecute() {
					Logger.debug("AsyncTask", "onPreExecute for " + key.from + " to " + key.to + " text: {" + key.text + "}");
					super.onPreExecute();
					cache.put(key, null);
				}
				
				@Override
				protected String doInBackground(Void... params) {
					Logger.debug("AsyncTask", "doInBackground for " + key.from + " to " + key.to + " text: {" + key.text + "}");
					String result = ti.translate(key.text, key.from, key.to);
					cache.put(key, result);
					return result;
				}
				
				@Override
				protected void onPostExecute(String result) {
					Logger.debug("AsyncTask", "onPostExecute for " + key.from + " to " + key.to + " text: {" + key.text + "}");
					super.onPostExecute(result);
					app.onTranslationUpdated();
				}
			};
			as.execute();
		}
		
		return null;
	}
}
