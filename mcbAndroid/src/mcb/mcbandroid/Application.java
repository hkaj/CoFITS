package mcb.mcbandroid;


import jade.core.AID;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mcb.debug.Logger;
import mcb.mcbandroid.agents.PersonalAgent;
import mcb.mcbandroid.utils.AndroidLogger;
import mcb.mcbandroid.utils.AsyncTranslator;
import mcb.mcbandroid.utils.HandlerInterface;
import mcb.mcbandroid.utils.LocalHandler;
import mcb.mcbandroid.utils.MeetingHandler;
import mcb.mcbandroid.utils.MemoraeHandler;
import mcb.mcbandroid.utils.ObjectAdapter;
import mcb.mcbandroid.utils.Tagger;
import mcb.mcbandroid.view.ConceptGridFragment;
import mcb.mcbandroid.view.PostItFragment;
import mcb.mcbandroid.view.objects.Cluster;
import mcb.mcbandroid.view.objects.PostIt;
import mcb.model.Brainstorming;
import mcb.model.ParentInterface;
import mcb.model.PostItWithTags;
import mcb.model.Topic;
import mcb.model.User;
import mcb.model.Memorae.Concept;
import mcb.model.Memorae.Memoire;
import mcb.translator.Bing;
import mcb.util.JSONMapper;
import mcb.util.i18nString;
import mcb.util.i18nString.Language;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This is the main class for the Android application
 * 
 * It makes the link between the model and the interface (acting like a controller)
 * It also handles the persistence mechanism, the settings, etc.
 */
public class Application extends android.app.Application implements PropertyChangeListener {
	// data handling
	protected User user = null;
	protected LocalHandler local = null;
	protected MemoraeHandler memorae = null;
	protected MeetingHandler meeting = null;
	
	// GUI handling
	protected enum WorkingMode { LOCAL, MEETING, MEMORAE };
	protected WorkingMode workingMode = WorkingMode.LOCAL;	
	protected ObjectAdapter objectAdapter = null;
	protected UUID objectRoot = null;
	protected String filterByTags = "";
	protected ArrayList<mcb.model.BaseObject> droppedOnMeeting = new ArrayList<mcb.model.BaseObject>();
	protected ArrayList<mcb.model.BaseObject> droppedOnMemorae = new ArrayList<mcb.model.BaseObject>();
	protected boolean meetingReady = false;
	protected ProgressDialog progressDialog;
	protected ConceptGridFragment memoraeFragment = null;

	protected FragmentManager fragmentManager = null;
	protected View mainView = null;
	
	protected static String LOCK_FAILURE = "lock-failure";
	
	// constants
	final public static String FILENAME_USER = "user.json";
	final public static String FILENAME_MODEL = "model.json";
		
	/**
	 * This method is called when the application is first displayed
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		onSettingsUpdated();
		
		Logger.setInstance( new AndroidLogger() );
		
		local = new LocalHandler(this);
		objectAdapter = new ObjectAdapter(this);
		local.addListener(this);
		
		updateAdapters();
	}
	
	/**
	 * Clean the application when quitting
	 * 
	 * @TODO This should be called when the application quit
	 * 
	 * Disconnect from the meeting, if any
	 * Disconnect from memorae, if any
	 */
	public void onDestroy() {
		Logger.debug("application", "onDestroy called");
		if ( meeting != null ) {
			meeting.doDisconnect();
			meeting = null;
		}
	}
	
	/**
	 * Set application's FragmentManager and root view
	 * 
	 * The FragmentManager and view will be used to handle the GUI.
	 * hey both should always be valid.
	 * 
	 * @param fragmentManager
	 * @param view
	 */
	public void setFragmentManager(FragmentManager fragmentManager, View view) {
		this.fragmentManager = fragmentManager;
		this.mainView = view;
	}

	
	/**
	 * Method called when an object (PostIt or Cluster) is edited in the grid
	 * 
	 * For a cluster, go inside (update the GridFragment's view)
	 * It means that getRoot() will return this cluster's id
	 * 
	 * For a post-it, show content (edit mode, show the PostItFragment)
	 * Will ask for a lock on the PostIt, and if it fail, cancel the edition
	 * 
	 * @param v the object on which the tap was made
	 */
	public void onEditObject(final View v) {
		Logger.debug("application", "onEditObject");
		final Context context = v.getRootView().findViewById(R.id.main).getContext();	
		
		if ( v instanceof PostIt ) {
			PostIt bo = (PostIt) v;
			updateAdapters();
			
			// ask for a lock
			getCurrentHandler().lock(bo.getModelId());
			
			if ( fragmentManager.findFragmentByTag(MainActivity.FRAGMENTMANAGER_EDIT) == null ) {
				Fragment postItFragment = new PostItFragment(getCurrentHandler().getModel().get(bo.getModelId()));
				fragmentManager.beginTransaction()
					.add(R.id.mainFragment, postItFragment, MainActivity.FRAGMENTMANAGER_EDIT)
					.addToBackStack(null)
					.commit();
			}
		}
		else if ( v instanceof Cluster ) {
			Cluster bo = (Cluster) v;
			objectRoot = bo.getModelId();
			updateAdapters();
			
			v.getRootView().findViewById(R.id.gridClusterTitleLL).setVisibility(View.VISIBLE);
			v.getRootView().findViewById(R.id.gridClusterTitleLL).setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			EditText et = (EditText)v.getRootView().findViewById(R.id.gridClusterTitleET);
				et.setText( getCurrentHandler().getModel().get(objectRoot).getContent().getValue() );
		}
	}
	
	/**
	 * Method called when a lock can't be obtained for editing
	 * @param view View object needed to make change to the GUI
	 */
	public void onLockFailureForEdit(View view) {
		Toast.makeText(view.getContext(), getResources().getText(R.string.toast_lockfailure), Toast.LENGTH_LONG).show();
		fragmentManager.popBackStack();
	}
	
	/**
	 * Method called when an object (PostIt or Cluster) is selected/unselected in the grid
	 * @param view The selected/unselected object
	 */
	public void onSelectObject(UUID objectId, View view) {
		Logger.debug("application", "onSelectObject");
		
		if ( view.isSelected() )
			getCurrentHandler().unselect(getUser(),objectId);
		else
			getCurrentHandler().select(getUser(),objectId);
	}

	/**
	 * Go up in the cluster/concept hierarchy
	 * @warning currently, it means going back to the root
	 * @param view View object needed to make change to the GUI
	 * @return true if something changed, false otherwise
	 */
	public boolean onGoUp(View view) {
		Logger.debug("application", "onGoUp");
		
		// inside cluster, go up
		if ( objectRoot != null ) {
			objectRoot = null;
			updateAdapters();
			view.getRootView().findViewById(R.id.gridClusterTitleLL).setVisibility(View.INVISIBLE);
			view.getRootView().findViewById(R.id.gridClusterTitleLL).setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0));
			return true;
		}
		
		// inside MemoraeFragment
		if ( workingMode == WorkingMode.MEMORAE ) {
			if ( memoraeFragment.isVisible() == false ) {
				fragmentManager.beginTransaction()
					.add(R.id.mainFragment, memoraeFragment, MainActivity.FRAGMENTMANAGER_MEMORAE)
					.addToBackStack(null)
					.commit();
				memorae = null;
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}

	/**
	 * Called when the application is resumed (was stopped, and is reloaded)
	 * 
	 * This will recreate the GridView content, 
	 * hide/show some widget from the interface depending on the current context...
	 * 
	 * @param view A view object (it doesn't matter which one) 
	 */
	public void onResume(View view) {
		Logger.debug("application", "onResume");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		// root
		if ( objectRoot != null ) {
			view.getRootView().findViewById(R.id.gridClusterTitleLL).setVisibility(View.VISIBLE);
			view.getRootView().findViewById(R.id.gridClusterTitleLL).setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		
		// memorae
		if ( sharedPref.getBoolean(SettingsActivity.KEY_MEMORAE_ENABLED, false) == false ) {
			view.getRootView().findViewById(R.id.memorae).setVisibility(View.GONE);
		}
		else {
			view.getRootView().findViewById(R.id.memorae).setVisibility(View.VISIBLE);
			
			if ( workingMode == WorkingMode.MEMORAE && memoraeFragment.isVisible() == false && memorae == null) {
				fragmentManager.beginTransaction()
					.add(R.id.mainFragment, memoraeFragment, MainActivity.FRAGMENTMANAGER_MEMORAE)
					.addToBackStack(null)
					.commit();
			}
		}
		
		updateAdapters();
		
		view.getRootView().findViewById(R.id.local).setSelected(workingMode == WorkingMode.LOCAL);
		view.getRootView().findViewById(R.id.memorae).setSelected(workingMode == WorkingMode.MEMORAE);
		view.getRootView().findViewById(R.id.meeting).setSelected(workingMode == WorkingMode.MEETING);
	}
	
	/**
	 * Filter by tag
	 * 
	 * This will update the GridFragment to hide object not matching the tags selected
	 * 
	 * @param tags List of tags
	 */
	public void filterByTags(String tags) {
		filterByTags = tags;
		updateAdapters();
	}
	
	/**
	 * Go into local mode
	 * 
	 * Update the view to reflect the change of mode
	 * 
	 * @param view A View object to access the view
	 */
	public void onGoLocalMode(View view) {
		if ( workingMode == WorkingMode.LOCAL ) return;
		Logger.debug("application", "onGoLocalMode");
		
		// hide memorae's concept grid
		if ( memoraeFragment != null && memoraeFragment.isVisible() ) {
			fragmentManager.popBackStack();
		}
		
		workingMode = WorkingMode.LOCAL;
		updateAdapters();
		
		// update button
		onResume(view);
	}
	
	/**
	 * Go into meeting mode
	 * 
	 * Update the view to reflect the change of mode.
	 * If the connection to the meeting is not up yet, it will try to start it.
	 * If it fail, go back to local mode.
	 * 
	 * @param view A View object to access the view
	 */
	public void onGoMeetingMode(View view) {
		if ( workingMode == WorkingMode.MEETING ) return;
		Logger.debug("application", "onGoMeetingMode");
		
		// hide memorae's concept grid
		if ( memoraeFragment != null && memoraeFragment.isVisible() ) {
			fragmentManager.popBackStack();
		}

		// connect
		if ( meeting == null ) {
			onConnectMeeting(view);
			updateAdapters(null);
			workingMode = WorkingMode.MEETING;
		}
		else if ( meetingReady ) {
			workingMode = WorkingMode.MEETING;
			updateAdapters();
		}
		
		// update button
		onResume(view);
	}
	
	/**
	 * Go into memorae mode
	 * 
	 * Update the view to reflect the change of mode
	 * If it fail, go back to local mode
	 * 
	 * @param view A View object to access the view
	 */
	public void onGoMemoraeMode(View view) {
		if ( workingMode == WorkingMode.MEMORAE ) return;
		Logger.debug("application", "onGoMemoraeMode");
		
		if ( memoraeFragment == null )
			memoraeFragment = new ConceptGridFragment();
		
		if ( memorae == null ) {
			fragmentManager.beginTransaction()
				.add(R.id.mainFragment, memoraeFragment, MainActivity.FRAGMENTMANAGER_MEMORAE)
				.addToBackStack(null)
				.commit();
		}
		
		workingMode = WorkingMode.MEMORAE;
		
		// update button
		onResume(view);
	}

	/**
	 * Method called when the user drop an object on the local button
	 * 
	 * Make a copy of the object into the local mode, 
	 * unless we are already in local mode.
	 * 
	 * @param view A View object to access the view
	 * @param droppedtem The id of the dropped item
	 */
	public void onDropOnLocal(View view, UUID droppedtem) {
		if ( workingMode == WorkingMode.LOCAL ) return;
		
		mcb.model.BaseObject o = getCurrentHandler().getModel().get(droppedtem);
		local.addObject(o);
		Toast.makeText(getApplicationContext(), getResources().getText(R.string.toast_transfer_to_local), Toast.LENGTH_SHORT).show();
	}

	/**
	 * Method called when the user drop an object on the meeting button
	 * 
	 * Make a copy of the object and send it to the meeting agent,
	 * unless we are in meeting mode.
	 * 
	 * @param view A View object to access the view
	 * @param droppedtem The id of the dropped item
	 */
	public void onDropOnMeeting(View v, UUID droppedtem) {
		if ( workingMode == WorkingMode.MEETING ) return;
		
		mcb.model.BaseObject o = getCurrentHandler().getModel().get(droppedtem);
		
		if ( meeting == null ) {
			droppedOnMeeting.add(o);
			onConnectMeeting(v);
			return;
		}
		
		meeting.addObject(o);		
		Toast.makeText(getApplicationContext(), getResources().getText(R.string.toast_transfer_to_meeting), Toast.LENGTH_SHORT).show();
	}

	/**
	 * Method called when the user drop an object on the memorae button
	 * 
	 * @TODO what should it do ?
	 * 
	 * @param view A View object to access the view
	 * @param droppedtem The id of the dropped item
	 */
	public void onDropOnMermorae(View view, UUID droppedtem) {
		if ( workingMode == WorkingMode.MEMORAE ) return;
	
		mcb.model.BaseObject o = getCurrentHandler().getModel().get(droppedtem);
		
		if ( memorae == null ) {
			droppedOnMemorae.add(o);
			onGoMemoraeMode(view);
			return;
		}
		
		memorae.addObject(o);		
		Toast.makeText(getApplicationContext(), getResources().getText(R.string.toast_transfer_to_memorae), Toast.LENGTH_SHORT).show();
	}

	/**
	 * This method is called when an object is dropped on a cluster
	 * 
	 * It will affiliate the dropped item to the cluster, 
	 * if it's not already done, and if it will not create a loop.
	 * 
	 * @param clusterId		The drop target
	 * @param droppedtem	The object
	 */
	public void onDropOnCluster(UUID clusterId, UUID droppedtem) {
		if ( clusterId.equals(droppedtem) ) return;
		
		// check for loop
		mcb.model.BaseObject di = getCurrentHandler().getModel().get(droppedtem);
		if ( di instanceof mcb.model.Cluster ) {
			if ( ((mcb.model.Cluster) di).get(clusterId) != null ) {
				Toast.makeText(getApplicationContext(), getResources().getText(R.string.toast_affiliate_loop), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		// check for already-inside
		mcb.model.BaseObject ci = getCurrentHandler().getModel().get(clusterId);
		if ( ci instanceof mcb.model.Cluster ) {
			if ( ((mcb.model.Cluster) ci).get(droppedtem) != null ) {
				Toast.makeText(getApplicationContext(), getResources().getText(R.string.toast_affiliate_already), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		getCurrentHandler().affiliate(droppedtem, clusterId);
		Toast.makeText(getApplicationContext(), getResources().getText(R.string.toast_affiliate), Toast.LENGTH_SHORT).show();
		updateAdapters();
	}
	
	/**
	 * This method is called when an object is dropped on the Root/GoUp button
	 * 
	 * This will disaffiliate the object from the current cluster, and affiliate it back to 
	 * the root.
	 * 
	 * @param view A View object to access the view
	 * @param droppedtem	The object
	 */
	public void onDropOnRoot(View view, UUID droppedtem) {
		getCurrentHandler().disaffiliate(droppedtem, getRoot());
		Toast.makeText(getApplicationContext(), getResources().getText(R.string.toast_disaffiliate), Toast.LENGTH_SHORT).show();
		updateAdapters();
	}

	/**
	 * Method called to disconnect from meeting
	 * @param view A View object to access the view
	 */
	public void onDisconnectMeeting(View view) {
		if ( workingMode == WorkingMode.MEETING ) {
			onGoLocalMode(view);
		}
		
		if ( meeting != null ) {
			meeting.doDisconnect();
		}
	}

	/**
	 * Method called to connect to a meeting
	 * @param view A View object to access the view
	 */
	protected void onConnectMeeting(final View view) {
		if ( meeting != null ) return;
		
	    progressDialog = ProgressDialog.show(view.getContext(), getResources().getText(R.string.meeting_wait_title), getResources().getText(R.string.meeting_wait_content), true);
	    progressDialog.setCancelable(true);
	    progressDialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Application.this.onDisconnectMeeting(view);
			}
		});
	    
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
		String localName = user.getName() + "-pa";
		String host = sharedPref.getString(SettingsActivity.KEY_JADE_HOST, "");
		Integer port = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_JADE_PORT, "1099"));
		meeting = new MeetingHandler(this, localName, host, port);
	}
	
	/**
	 * Method called when the user switch the language
	 * @param fragmentManager The FragmentManager to access edit fragment
	 * @param view A View object to access the view
	 * @param language Newly selected language (string representation)
	 */
	public void onSetLanguage(FragmentManager fragmentManager, View view, CharSequence language) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.edit()
        	.putString(SettingsActivity.KEY_LANGUAGE, language.toString())
        	.apply();
        onLanguageSettingsUpdated();
        
        ((Button)view.getRootView().findViewById(R.id.langB)).setText(Language.valueOf(language.toString()).getName().getValue());
        
        // update post-it editing fragment if any
        PostItFragment pif = (PostItFragment) fragmentManager.findFragmentByTag(MainActivity.FRAGMENTMANAGER_EDIT);
        if ( pif != null ) {
        	pif.setLanguage(Language.valueOf(language.toString()));
        }
        
        // update cluster editing if any
        if ( getRoot() != null ) {
        	i18nString.Language l = i18nString.Language.valueOf(language.toString());
        	EditText et = (EditText) view.getRootView().findViewById(R.id.gridClusterTitleET);
        	et.setText( getCurrentHandler().getModel().get(getRoot()).getContent().get(l) );
        }
        
        updateAdapters();
	}
	
	/**
	 * This method is called when the user click on the "new object" button
	 * The user will be asked if he wants to create a post-it or a cluster 
	 * 
	 * @param view A View object to access the view
	 */
	public void onClickNew(final View view) {

		// select what to create
		new AlertDialog.Builder(view.getContext())
	    	.setTitle(R.string.create_new_object)
	    	.setItems(new CharSequence[]{"PostIt","Cluster"}, 
    		   new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   
	            	   // post it
	            	   if ( which == 0 ) {
		       				if ( fragmentManager.findFragmentByTag(MainActivity.FRAGMENTMANAGER_EDIT) == null ) {
		    					Fragment newFragment = new PostItFragment();
		    					fragmentManager.beginTransaction()
		    						.add(R.id.mainFragment, newFragment, "postit")
		    						.addToBackStack(null)
		    						.commit();
		    				}
	            	   }
	            	  
	            	   // cluster
	            	   else {
					    	String hint;
					    	hint = getResources().getText(R.string.ph_cluster_title).toString();
					    	hint = hint.replace("$LANG$", getCurrentLanguage().getName().getValue());
							final EditText input = new EditText(view.getContext());
					    	input.setHint(hint);
							
							new AlertDialog.Builder(view.getContext())
							    .setTitle( getResources().getText(R.string.create_cluster) )
							    .setView(input)
							    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int whichButton) {
							        	UUID newId = getCurrentHandler().createCluster( 
							        		getUser(),
							        		new i18nString(i18nString.languagePreferencies.get(0), input.getText().toString())
							        	); 
							        	
								    	// put in current cluster
								    	if ( getRoot() != null ) {
									    	getCurrentHandler().affiliate(newId, getRoot());
									    	updateAdapters();
								    	}
							        }
							    })
							    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int whichButton) {
							        }
							    })
							    .show();	            	   
						}
	               }
	           })
           .show();
	}
	/**
	 * Method called when the user select a meeting to connect to
	 * @param aid		The AID of the agent holding the meeting
	 */
	public void selectMeeting(AID aid) {
		if( meeting != null ) {
			meeting.selectMeeting(aid);
		}
	}

	/**
	 * Method called when the user click on Clear Local Model
	 */
	public void onClearModel() {
		local.newModel();
	}
	
	/**
	 * Method called when the settings (could be) are updated 
	 */
	public void onSettingsUpdated() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        onLanguageSettingsUpdated();

        // user settings
        boolean saveNeeded = false;
		if ( loadUserData() == false ) {
			user = new User(sharedPref.getString(SettingsActivity.KEY_USERNAME, ""), sharedPref.getString(SettingsActivity.KEY_USERNAME_ALIAS, ""));
			saveNeeded = true;
		}
		else {
			if ( user.getAlias().equals(sharedPref.getString(SettingsActivity.KEY_USERNAME_ALIAS, "")) == false ) {
				user.setAlias( sharedPref.getString(SettingsActivity.KEY_USERNAME_ALIAS, "") );
				saveNeeded = true;
			}
			if ( user.getName().equals(sharedPref.getString(SettingsActivity.KEY_USERNAME, "")) == false ) {
				user.setName( sharedPref.getString(SettingsActivity.KEY_USERNAME, "") );
				saveNeeded = true;
			}
		}
		if ( saveNeeded ) {
			saveUserData();
		}
		
		// translator settings
		if ( sharedPref.getBoolean(SettingsActivity.KEY_TRANSLATOR_BING, false) ) {
	        i18nString.translator = new AsyncTranslator(
	        		new Bing(sharedPref.getString(SettingsActivity.KEY_TRANSLATOR_BINGCLIENT, ""),sharedPref.getString(SettingsActivity.KEY_TRANSLATOR_BINGSECRET, "")),
	        		this
	        	);
		}
		else {
			i18nString.translator = null;
		}
		
		//
		// update gui
		//
		if ( this.mainView != null ) {
			onResume(this.mainView);
		}
		
		Logger.debug("application", "(re)load settings");
	}
	
	/**
	 * Method called when the language settings are changed
	 * 
	 * It will propagate the change to the 
	 * @ref mcb.util.i18nString.languagePreferencies "nString.languagePreferencies" 
	 * list of language.
	 */
	public void onLanguageSettingsUpdated() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        // language preferences
        i18nString.languagePreferencies.clear();
        
        Language preferedLang = null;
        try { preferedLang = Language.valueOf(sharedPref.getString(SettingsActivity.KEY_LANGUAGE, "ENGLISH")); }
        catch (IllegalArgumentException e) {}
        
        if ( preferedLang != null )
	        i18nString.languagePreferencies.add(preferedLang);
        
        if ( Language.ENGLISH.equals(preferedLang) == false )
	        i18nString.languagePreferencies.add(Language.ENGLISH);
        if ( Language.FRENCH.equals(preferedLang) == false )
	        i18nString.languagePreferencies.add(Language.FRENCH);
        if ( Language.JAPANESE.equals(preferedLang) == false )
	        i18nString.languagePreferencies.add(Language.JAPANESE);
	}
	
	public void onConceptSelect(Memoire memoire, Concept concept) {
		Logger.debug("application", "onConceptSelected");
		
		memorae = new MemoraeHandler(this, memoire, concept);
		memorae.addListener(this);
		
        fragmentManager.popBackStack();
        
		for ( mcb.model.BaseObject o : droppedOnMemorae ) {
			memorae.addObject(o);
		}
		droppedOnMemorae.clear();
		updateAdapters();
	}
	
	public void onConceptUnselect(View view) {
		Logger.debug("application", "onConceptUnselect");
		
		if ( memoraeFragment != null && memoraeFragment.isVisible() )
			fragmentManager.popBackStack();
		
		memorae = null;
		memoraeFragment = null;
		
		if ( workingMode == WorkingMode.MEMORAE ) {
			workingMode = WorkingMode.LOCAL;
		}
		onGoMemoraeMode(view);
	}

	public void onModelLoaded() {
		updateAdapters();
	}
	
	/**
	 * This method is called to handle event from the Memorae connection
	 * @param view		A view object to access the view
	 * @param event		The event
	 * @param bundle 
	 */
	public void onMemoraeEvent(final View view, String event, Bundle bundle) {
		/**
		 * In case of a @ref mcb.mcbandroid.utils.MemoraeHandler.EVENT_ERROR "MemoraeHandler.EVENT_ERROR",
		 * alert the user and go back to local mode
		 */
		if ( event.equals(MemoraeHandler.EVENT_ERROR) ) {
		    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder
				.setTitle(R.string.memorae_failure)
				.setMessage(bundle.getString(MemoraeHandler.PARAM))
				.show();
			
            fragmentManager.popBackStack();
            memoraeFragment = null;
			onGoLocalMode(view);
        }
	}
	
	/**
	 * This method is called to handle event from the Meeting connection
	 * @param view		A view object to access the view
	 * @param event		The event
	 * @param bundle 
	 */
	public void onMeetingEvent(final View view, String event, Bundle bundle) {
		/**
		 * In all cases, the progressDialog will be destroyed
		 */
		if ( progressDialog != null ) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		
		/**
		 * In case of a @ref mcb.mcbandroid.agents.PersonalAgent.EVENT_DISCONNECTED "PersonalAgent.EVENT_DISCONNECTED",
		 * the MeetingHandler object will be destroyed
		 */
		if ( event.equals(PersonalAgent.EVENT_DISCONNECTED) ) {
			meeting = null;
			meetingReady = false;
			droppedOnMeeting.clear();
		}
		
		/**
		 * In case of a @ref mcb.mcbandroid.agents.PersonalAgent.EVENT_ERROR "PersonalAgent.EVENT_ERROR",
		 * alert the user and disconnect from meeting
		 */
		else if ( event.equals(PersonalAgent.EVENT_ERROR) ) {
		    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder
				.setTitle(R.string.agent_failure)
				.setMessage(bundle.getString(PersonalAgent.PARAM))
				.show();
			
			onDisconnectMeeting(view);
        }

		/**
		 * In case of a @ref mcb.mcbandroid.agents.PersonalAgent.EVENT_MEETING_LIST "PersonalAgent.EVENT_MEETING_LIST",
		 * show the list of meeting to the user and let she choose
		 */
		else if ( event.equals(PersonalAgent.EVENT_MEETING_LIST) ) {
			@SuppressWarnings("unchecked")
			final HashMap<AID,Topic> meetings = (HashMap<AID,Topic>) bundle.get(PersonalAgent.PARAM);
		    CharSequence[] list = new CharSequence[meetings.size()];
		    
		    int i = 0;
		    for ( AID aid : meetings.keySet() ) {
		    	String name = aid.getName();
		    	name += "\n";
		    	name += meetings.get(aid).getTopic().getValue();
		    	list[i] = name;
		    	i++;
		    }
		    
		    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
			builder.setTitle(R.string.pick_meeting)
		           .setItems(list, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int index) {
		                   AID selectedAID = null;
		                   
						    int i = 0;
						    for ( AID aid : meetings.keySet() ) {
						    	selectedAID = aid;
						    	if ( i == index ) break;
						    }

		                   selectMeeting(selectedAID);
		               }
		           })
		           .setOnCancelListener(new OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
				    		onDisconnectMeeting(view);
						}
				})
				.show();
		}
		
		/**
		 * In case of a @ref mcb.mcbandroid.agents.PersonalAgent.EVENT_NO_MEETING_FOUND "PersonalAgent.EVENT_NO_MEETING_FOUND",
		 * alert the user that no meeting were found, and disconnect
		 */
		else if ( event.equals(PersonalAgent.EVENT_NO_MEETING_FOUND) ) {
		    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
			
		    builder
				.setTitle(R.string.no_meeting_found)
				.show();
			
			onDisconnectMeeting(view);
		}
		
		/**
		 * In case of a @ref mcb.mcbandroid.agents.PersonalAgent.EVENT_AMSFAILURE "PersonalAgent.EVENT_AMSFAILURE",
		 * alert the user and disconnect
		 */
		else if ( event.equals(PersonalAgent.EVENT_AMSFAILURE) ) {
		    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
			
		    builder
				.setTitle(R.string.ams_failure)
				.setMessage( (String)bundle.get(PersonalAgent.PARAM) )
				.show();
			
			onDisconnectMeeting(view);
		}
		
		/**
		 * In case of a @ref mcb.mcbandroid.agents.PersonalAgent.EVENT_LOCKFAILURE "PersonalAgent.EVENT_LOCKFAILURE",
		 * alert the user
		 */
		else if ( event.equals(PersonalAgent.EVENT_LOCKFAILURE) ) {
			if ( fragmentManager.findFragmentByTag(MainActivity.FRAGMENTMANAGER_EDIT) != null ) {
				onLockFailureForEdit(view);
			}
			else {
				Toast.makeText(view.getContext(), getResources().getText(R.string.toast_lockfailure), Toast.LENGTH_LONG).show();
			}
		}
		
		/**
		 * In case of a @ref mcb.mcbandroid.agents.PersonalAgent.EVENT_CONNECTED "PersonalAgent.EVENT_CONNECTED",
		 * do nothing
		 */
		else if ( event.equals(PersonalAgent.EVENT_CONNECTED) ) {
		}
		
		/**
		 * In case of a @ref mcb.mcbandroid.agents.PersonalAgent.EVENT_SUSCRIBED "PersonalAgent.EVENT_SUSCRIBED",
		 * set the meeting to a ready state, send object in the send-stack, and
		 * update the view
		 */
		else if ( event.equals(PersonalAgent.EVENT_SUSCRIBED) ) {
			meetingReady = true;
			for ( mcb.model.BaseObject o : droppedOnMeeting ) {
				meeting.addObject(o);
			}
			droppedOnMeeting.clear();
			updateAdapters();
		}
		
		/**
		 * In case of a @ref mcb.mcbandroid.agents.PersonalAgent.EVENT_UNSUBSCRIBED "PersonalAgent.EVENT_UNSUBSCRIBED",
		 * show an alert to inform the user, and disconnect from the meeting
		 */
		else if ( event.equals(PersonalAgent.EVENT_UNSUBSCRIBED) ) {					    
			AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
		
			builder
				.setTitle(R.string.meeting_unsubscribed_title)
				.setMessage(R.string.meeting_unsubscribed)
				.show();

			meetingReady = false;
			onDisconnectMeeting(view);
		}
	}

	/**
	 * This method is called when a translation becomes available
	 * It will update the view to show the new translation
	 */
	public void onTranslationUpdated() {
		updateAdapters();
	}

	/**
	 * This method is called when the user confirm/end a PostIt edition/creation
	 * 
	 * If there is a lock on the object (edition only), it will be released
	 * 
	 * @param model			The object being edited (can be null for a new one)
	 * @param titles		List of title (mapped to corresponding language)
	 * @param contents		List of content (mapped to corresponding language)
	 * @param tags			List of tags
	 */
	public void onSaveEditing(mcb.model.PostIt model, HashMap<Language, String> titles, HashMap<Language, String> contents, String tags) {
		UUID id = null;
		
		if ( model == null ) {
	    	i18nString title = new i18nString();
	    	i18nString content = new i18nString();
    		
	    	for ( Language l : titles.keySet() ) {
	    		title.set(l, titles.get(l));
	    		content.set(l, contents.get(l));
	    	}
	    	
	    	id = getCurrentHandler().createPostIt(user, title, content, tags);
	    	
	    	// put in current cluster
	    	if ( getRoot() != null ) {
		    	getCurrentHandler().affiliate(id,getRoot());
		    	updateAdapters();
	    	}
    	}
    	else {		    					    	
	    	for ( Language l : titles.keySet() ) {
		    	getCurrentHandler().updatePostIt(getUser(), model.getId(), l, titles.get(l), contents.get(l), tags);
	    	}
	    	
	    	id = model.getId();
    	}
    	
		fragmentManager.popBackStack();
		getCurrentHandler().unlock(id);
	}
	
	/**
	 * This method is called when a post-it edition/creation is canceled
	 * 
	 * It there is a lock on the edited PostIt, it will be released
	 * 
	 * @param model		The object being edited
	 */
	public void onCancelEditing(mcb.model.PostIt model) {
		if ( model != null ) {
			getCurrentHandler().unlock(model.getId());
		}
		
		fragmentManager.popBackStack();
	}

	/**
	 * This method is called when an object is drop on the remove button
	 * @param droppedItemId		The id of the dropped object
	 */
	public void onDropOnTrash(UUID droppedItemId) {
		getCurrentHandler().removeObject(droppedItemId);
	}

	/**
	 * This method is called when the user set a new title for the current cluster
	 * @param content		the new title
	 * @param language		the language of the new title
	 */
	public void onSetClusterTitle(String content, Language language) {
		if ( getRoot() != null )
			getCurrentHandler().updateCluster(getUser(), getRoot(), language, content);
	}

	//
	//
	// Model handling
	//
	//
	
	
	/**
	 * Handle external update to the model
	 * Refresh the view accordingly
	 * 
	 * @param e the event
	 */
	public void propertyChange(PropertyChangeEvent e) {
		// update the view in all cases
		updateAdapters();
	}


	//
	//
	// Getters
	//
	//

	/**
	 * Get the current language
	 * @return the current language
	 */
	public Language getCurrentLanguage() {
		return i18nString.languagePreferencies.get(0);
	}

	/**
	 * This method will return true if a translator is configured
	 * @return true if the auto-translator is configured
	 * @return false otherwise
	 */
	public boolean getUseTranslator() {
		return i18nString.translator != null;
	}
	
	/**
	 * Get the User object
	 * @return the User object
	 */
	public final User getUser() {
		return user;
	}
	
	/**
	 * Get the ObjectAdaptor to display element in the Grid
	 * @return ObjectAdaptor
	 */
	public ObjectAdapter getObjectAdapter() {
		return objectAdapter;
	}
	
	/**
	 * Get the id of the ParentInterface currently displayed in the grid
	 * @return Id of the root displayed, or null
	 */
	public UUID getRoot() {
		return objectRoot;
	}

	/**
	 * Get the current handler interface
	 * @return HandlerInterface
	 */
	protected HandlerInterface getCurrentHandler() {
		switch ( workingMode ) {
			case LOCAL:
				return local;
			case MEETING:
				return meeting;
			case MEMORAE:
				return memorae;
		}				
		
		return null;
	}
	
	//
	//
	// Others
	//
	//
	/**
	 * This method write a String in a file
	 * @param filename		Path to the file to write to
	 * @param data			New content of the file
	 */
	public void write(String filename, String data) {
		FileOutputStream fos;
		
		try {
			fos = openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(data.getBytes());
			fos.close();	
		} catch (FileNotFoundException e) {
			Logger.error("app", "write", e);
		} catch (IOException e) {
			Logger.error("app", "write", e);
		}
	}	
	
	/**
	 * This method read a file and return its content as a String
	 * @param filename		Path to the file to read
	 * @return the file's content, or null
	 */
	public String read(String filename) {
		FileInputStream in;
		
		try {
			in = openFileInput(filename);
		    InputStreamReader inputStreamReader = new InputStreamReader(in);
		    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = bufferedReader.readLine()) != null) {
		        sb.append(line);
		    }			
		    in.close();	
		    return sb.toString();
		} catch (FileNotFoundException e) {
			Logger.error("app", "write", e);
		} catch (IOException e) {
			Logger.error("app", "write", e);
		}
		
		return null;
	}

	/**
	 * Save user object to file
	 */
	protected void saveUserData() {
		String data = "";
		try {
			data = JSONMapper.toJson(user);
		} catch (JsonGenerationException e) {
			Logger.error("user", "saveUserData", e);
		} catch (JsonMappingException e) {
			Logger.error("user", "saveUserData", e);
		} catch (IOException e) {
			Logger.error("user", "saveUserData", e);
		}
		write(FILENAME_USER,data);
	}
	
	/**
	 * Load user object from file
	 * @return true on success
	 * @return false otherwise
	 */
	protected boolean loadUserData() {
		try {
			String json = read(FILENAME_USER);
			if(json == null) return false;
			user = JSONMapper.getInstance().readValue(json, User.class);
			return true;
		} catch (JsonParseException e) {
			Logger.error("user", "loadUserData", e);
		} catch (JsonMappingException e) {
			Logger.error("user", "loadUserData", e);
		} catch (IOException e) {
			Logger.error("user", "loadUserData", e);
		}
		
		return false;
	}

	
	/**
	 * This method is used to update the GridFragment's content
	 * 
	 * It uses the current HandlerInterface to retrieve the list of object, as well
	 * as the filterByTag value, and the current cluster (root).
	 */
	private void updateAdapters() {
		if ( workingMode.equals(WorkingMode.MEETING) && meetingReady == false ) {
			updateAdapters(null); 
			return; 
		}
		
		if(workingMode.equals(WorkingMode.MEMORAE) && memorae == null){
			updateAdapters(null); 
			return; 
		}
		
		Brainstorming model = getCurrentHandler().getModel();
		updateAdapters(model);
	}
	
	private void updateAdapters(Brainstorming model) {
		List<mcb.model.BaseObject> tmp = null;
		List<mcb.model.BaseObject> result = new ArrayList<mcb.model.BaseObject>();
		
		if ( model != null ) {
			if ( objectRoot == null )
				tmp = model.getChildren();
			else {
				mcb.model.BaseObject root = model.get(objectRoot);
				if ( root instanceof ParentInterface ) {
					ParentInterface pa = (ParentInterface) root;
					tmp = pa.getChildren();
				}
				else {
					objectRoot = null;
					updateAdapters();
					return;
				}
			}
			
			// filter
			if ( filterByTags.isEmpty() ) {
				result = tmp;
			}
			else
			{
				for ( mcb.model.BaseObject o : tmp ) {
					if ( o instanceof PostItWithTags ) {
						PostItWithTags ot = (PostItWithTags) o;
						if ( Tagger.match(ot.getTags(), filterByTags, true, true)) {
							result.add(ot);
						}
					}
				}
			}
			
			// go
			objectAdapter.updateContent(result);
		}
		else {
			result.clear();
			objectAdapter.updateContent(result);
		}
	}
}
