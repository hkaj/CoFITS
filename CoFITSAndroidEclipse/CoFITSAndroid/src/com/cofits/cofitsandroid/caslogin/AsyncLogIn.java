package com.cofits.cofitsandroid.caslogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import com.cofits.cofitsandroid.caslogin.AsyncLogIn;
import com.cofits.cofitsandroid.caslogin.CASLoginRequest;
import com.cofits.cofitsandroid.tools.user;

public class AsyncLogIn  extends AsyncTask<Void, Void, Void>{

	private Activity  mact;
	private ProgressDialog pbAsyncBackgroundTraitement;
	
	
	public AsyncLogIn(Activity act){
		super();
		mact = act;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		System.out.println("onPreExecute");
		this.pbAsyncBackgroundTraitement = new ProgressDialog(mact);
		this.pbAsyncBackgroundTraitement.setMessage("Connexion en cours");
		this.pbAsyncBackgroundTraitement.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		
						
			try {
					new CASLoginRequest(user.name, user.pswd, "http://www.google.fr/");
					
					if(this.pbAsyncBackgroundTraitement.isShowing())
			    		this.pbAsyncBackgroundTraitement.dismiss();
				
					
			} catch (IOException e ) {
	         	System.out.println("failed cause of the server");
					e.printStackTrace();
	         } catch ( IllegalStateException e) {
				System.out.println("failed cause haissam has a little wiener");
			}
		
		return null;
	}

	
}
