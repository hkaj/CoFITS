package mcb.mcbandroid.view;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import mcb.debug.Logger;
import mcb.mcbandroid.Application;
import mcb.mcbandroid.R;
import mcb.mcbandroid.SettingsActivity;
import mcb.mcbandroid.utils.MemoraeHandler;
import mcb.model.Memorae.Concept;
import mcb.model.Memorae.Memoire;
import mcb.model.Memorae.MemoraeConnecter;
import mcb.model.Memorae.MemoraeConnecterInterface;
import mcb.model.Memorae.Organization;
import mcb.model.Memorae.User;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class ConceptGridFragment extends Fragment {
    
	private class ConceptGridAdapter extends BaseAdapter{
		
		private final List<Concept> concepts = new ArrayList<Concept>();
		
		public void setContent(Collection<Concept> c){
			Logger.debug("MemoraeAdapter", "displaying", c.size(), "concepts");
			concepts.clear();
			concepts.addAll(c);
			notifyDataSetChanged();
		}

		public int getCount() {
			return concepts.size();
		}

		public Object getItem(int position) {
			return concepts.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return new mcb.mcbandroid.view.objects.Concept(parent.getContext(), (Concept) getItem(position));
		}
		
	}
	
	
	ConceptGridAdapter myAdapter;
	View.OnClickListener myListener;
	MemoraeConnecterInterface connecter;
	
	Organization organization = null;
	Memoire memoire = null;
	User user = null;

	
    public synchronized void sendErrorToUI(Throwable e){
        Application app = (Application) getActivity().getApplication();
    	Intent broadcast = new Intent(MemoraeHandler.INTENT);
        broadcast.putExtra(MemoraeHandler.EVENT, MemoraeHandler.EVENT_ERROR);
        broadcast.putExtra(MemoraeHandler.PARAM, e.getMessage());
        app.getApplicationContext().sendBroadcast(broadcast);
    }

    public synchronized void sendSuccessToUI(String event){
        Application app = (Application) getActivity().getApplication();
        Intent broadcast = new Intent(MemoraeHandler.INTENT);
		broadcast.putExtra(MemoraeHandler.EVENT, event);
		app.getApplicationContext().sendBroadcast(broadcast);
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.memorae_fragment, container, false);
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
        
    	if ( connecter == null ) {
	    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ConceptGridFragment.this.getActivity());
			MemoraeConnecter.gatewayURL = sharedPref.getString(SettingsActivity.KEY_MEMORAE_HOST, MemoraeConnecter.gatewayURL);
			
	    	connecter = new MemoraeConnecter();
	    	myAdapter = new ConceptGridAdapter();
				
			doLogin();
    	}
    	
    	GridView gv = (GridView) getActivity().findViewById(R.id.conceptgridview);
    	gv.setAdapter( myAdapter );
    	gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Concept concept = (Concept) myAdapter.getItem(arg2);
				Application app = (Application)getActivity().getApplication();
				app.onConceptSelect(memoire, concept);
			}
		});
		gv.setBackgroundResource(R.drawable.gridbackground);
    }
    
    public void doLogin(){
    	
    	new AsyncTask<Void, Void, User>() {
        	final ProgressDialog spinner = ProgressDialog.show(getActivity(), getResources().getText(R.string.memorae_wait_title), getResources().getText(R.string.memorae_wait_login), true);

			@Override
			protected User doInBackground(Void... nothing) {
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ConceptGridFragment.this.getActivity());
		        
				String username = sharedPref.getString(SettingsActivity.KEY_MEMORAE_USERNAME, "");
				String password = sharedPref.getString(SettingsActivity.KEY_MEMORAE_PASSWORD, "");
				
				return connecter.checkUserPassword(username, password);
			}
			
			protected void onPostExecute(User result) {
				spinner.dismiss();
				
				if(result == null) {
					sendErrorToUI(new RuntimeException( getResources().getText(R.string.toast_loginfailure).toString() ));
				}
				else{
					user = result;
					OrganizationAndUserBarrier();
					showSelectOrganization();
				}
			}
    	}.execute();
	}
    
    public void showSelectOrganization(){
    	
    	final ProgressDialog spinner = ProgressDialog.show(getActivity(), getResources().getText(R.string.memorae_wait_title), getResources().getText(R.string.memorae_wait_organization), true);
    	
    	new AsyncTask<Void, Void, Collection<Organization>>() {

			@Override
			protected Collection<Organization> doInBackground(Void... params) {
				return connecter.getAllOrganizations();
			}
			
			protected void onPostExecute(Collection<Organization> result) {
				spinner.dismiss();

				int i = 0, l = result.size();
				String[] labels = new String[l];
				final Organization[] values = new Organization[l];
				for(Iterator<Organization> it = result.iterator(); it.hasNext(); i++){
					values[i] = it.next();
					labels[i] = values[i].nom;
				}
				
				if ( labels.length == 0 ) {
					sendErrorToUI(new RuntimeException( getResources().getText(R.string.memorae_no_organization).toString() ));
					return;
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			    builder.setTitle("Choose Organization")
			           .setItems(labels, 
	        		   new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	  organization = values[which];
			            	  OrganizationAndUserBarrier();
			               }
			           })
			           .setCancelable(false)
			           .show();
			};

		}.execute();
    	
    	
    }
    
    public void OrganizationAndUserBarrier(){
    	if(organization == null || user == null) return;
    	showSelectMemoire();
    	loadConcepts();
    }
    
    public void showSelectMemoire(){
    	
    	final ProgressDialog spinner = ProgressDialog.show(getActivity(), getResources().getText(R.string.memorae_wait_title), getResources().getText(R.string.memorae_wait_memory), true);
    	
    	new AsyncTask<Void, Void, Collection<Memoire>>() {

			@Override
			protected Collection<Memoire> doInBackground(Void... params) {
				return connecter.getMemoires(organization, user);
			}
			
			protected void onPostExecute(Collection<Memoire> result) {
				spinner.dismiss();

				int i = 0, l = result.size();
				String[] labels = new String[l];
				final Memoire[] values = new Memoire[l];
				for(Iterator<Memoire> it = result.iterator(); it.hasNext(); i++){
					values[i] = it.next();
					labels[i] = values[i].nom;
				}
				
				if ( labels.length == 0 ) {
					sendErrorToUI(new RuntimeException( getResources().getText(R.string.memorae_no_memory).toString() ));
					return;
				}
		
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			    builder.setTitle("Choose Memoire")
			           .setCancelable(false)
			           .setItems(labels, 
	        		   new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	  memoire = values[which];
			               }
			           })
			           .show();
			};

		}.execute();
    	
    	
    }
    
    public void loadConcepts(){
    	
    	
    	final ProgressDialog spinner = ProgressDialog.show(getActivity(), getResources().getText(R.string.memorae_wait_title), getResources().getText(R.string.memorae_wait_concept), true);
    	
    	new AsyncTask<Void, Void, Collection<Concept>>() {

			@Override
			protected Collection<Concept> doInBackground(Void... nothing) {
				return connecter.getAllConcepts(organization, user);
			}
			
			protected void onPostExecute(Collection<Concept> result) {
				spinner.dismiss();
				myAdapter.setContent(result);
			}

		}.execute();
    }
    
}
