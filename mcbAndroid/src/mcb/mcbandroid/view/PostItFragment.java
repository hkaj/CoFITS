package mcb.mcbandroid.view;

import java.util.HashMap;

import mcb.mcbandroid.Application;
import mcb.mcbandroid.listener.FakeDropTarget;
import mcb.mcbandroid.utils.Tagger;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import mcb.model.BaseObject;
import mcb.model.PostIt;
import mcb.model.PostItWithTags;
import mcb.util.i18nString;
import mcb.util.i18nString.Language;
import mcb.mcbandroid.R;

public class PostItFragment extends Fragment {
	
	protected PostIt model;
	protected Language selectedLang = null;
	protected Language originalLanguage;
	protected boolean edited = false;
	protected boolean restoreLanguage = false;
	protected HashMap<Language, String> titles = new HashMap<i18nString.Language, String>();
	protected HashMap<Language, String> contents = new HashMap<i18nString.Language, String>();
	
	public PostItFragment(BaseObject baseObject) {
		super();
		
		if ( baseObject instanceof PostIt )
			this.model = (PostIt) baseObject;
		else
			this.model = null;
	}
	
	public PostItFragment() {
		this(null);
	}
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		return inflater.inflate(R.layout.postit_fragment, container, false);
    }
    
    @Override
    public void onDestroy() {
    	if ( restoreLanguage ) {
    		Button bLang = (Button) getActivity().findViewById(R.id.langB);
    		bLang.setText(originalLanguage.getName().getValue());
    	}
    	super.onDestroy();
    }
    
    public void setLanguage(Language language) {
    	restoreLanguage = false;
    	if ( language == selectedLang ) return;
    	
    	// save edited state to restore it
    	boolean tmpEdited = edited;
   	
    	EditText etTitle = (EditText) getActivity().findViewById(R.id.title);
    	EditText etContent = (EditText) getActivity().findViewById(R.id.content);
    	   	
    	// temporary save current if edited
    	if (edited) {
	    	titles.put(selectedLang, etTitle.getText().toString());
	    	contents.put(selectedLang, etContent.getText().toString());
    	}
    	
    	// change selected language and update view
    	selectedLang = language;
     	etTitle.setText("");
     	etContent.setText("");
  	
    	if ( titles.containsKey(selectedLang) && contents.containsKey(selectedLang)  ) {
	    	etTitle.setText(titles.get(selectedLang));
	    	etContent.setText(contents.get(selectedLang));
    	}
    	else if ( model != null ) {
    		String title = model.getTitle().get(selectedLang);
     		String content = model.getContent().get(selectedLang);
     		if ( title != null )
    			etTitle.setText(title);
    		if ( content != null )
		    	etContent.setText(content);
    	}
    	
    	updateHint(selectedLang);
    	
		// restore edited state
		edited = tmpEdited;
    }
    
    private void updateHint(Language selectedLang) {
    	String tmp;
    	
    	Button bLang = (Button) getActivity().findViewById(R.id.langB);
    	EditText etTitle = (EditText) getActivity().findViewById(R.id.title);
    	EditText etContent = (EditText) getActivity().findViewById(R.id.content);
    	
    	tmp = getResources().getText(R.string.ph_postit_title).toString();
    	tmp = tmp.replace("$LANG$", selectedLang.getName().getValue());
    	etTitle.setHint(tmp);
    	
    	tmp = getResources().getText(R.string.ph_postit_content).toString();
    	tmp = tmp.replace("$LANG$", selectedLang.getName().getValue());
    	etContent.setHint(tmp);
    	
    	bLang.setText(selectedLang.getName().getValue());
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	EditText etTags = (EditText) getActivity().findViewById(R.id.tags);
    	
    	originalLanguage = i18nString.languagePreferencies.get(0);
   	
    	if ( model != null ) {
        	
	    	if ( model instanceof PostItWithTags ) {
		    	etTags.setText(Tagger.toString(((PostItWithTags) model).getTags()));	
	    	}
	    	else {
	    		etTags.setVisibility(View.INVISIBLE);
	    	}
	    	
	    	setLanguage(model.getContent().getFirstLanguage());
	    	restoreLanguage = true;
    	}
    	else {
	    	setLanguage(originalLanguage);
    	}

    	Button bCancel = (Button) getActivity().findViewById(R.id.cancel);
    	bCancel.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
		    	final Application app = (Application) getActivity().getApplication();
		    	
				if ( edited == false ) {
					app.onCancelEditing(model);
				}
				else {
		            AlertDialog.Builder builder = new AlertDialog.Builder(PostItFragment.this.getActivity());
		            builder.setMessage(R.string.confirm_exit_edit_description)
		                   .setPositiveButton(R.string.confirm_exit_edit, new DialogInterface.OnClickListener() {
		                       public void onClick(DialogInterface dialog, int id) {
		       						app.onCancelEditing(model);
		                       }
		                   })
		                   .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		                       public void onClick(DialogInterface dialog, int id) {
		                       }
		                   })
		                   .show();
				}
			}
		});
    	bCancel.setBackgroundResource(R.drawable.button);
 	
    	Button bSave = (Button) getActivity().findViewById(R.id.save);
    	bSave.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
		    	Application app = (Application) getActivity().getApplication();
		    	
		    	EditText etTitle = (EditText) getActivity().findViewById(R.id.title);
		    	EditText etContent = (EditText) getActivity().findViewById(R.id.content);
		    	EditText etTags = (EditText) getActivity().findViewById(R.id.tags);
		    	
		    	titles.put(selectedLang, etTitle.getText().toString());
		    	contents.put(selectedLang, etContent.getText().toString());
		    	String tags = etTags.getText().toString();

		    	app.onSaveEditing(model, titles, contents, tags);
			}
		});
    	bSave.setBackgroundResource(R.drawable.button);
    	
    	TextWatcher tw = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) { edited = true; }
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		};
    	EditText etTitle = (EditText) getActivity().findViewById(R.id.title);
    	EditText etContent = (EditText) getActivity().findViewById(R.id.content);
    	etTitle.addTextChangedListener(tw);
    	etContent.addTextChangedListener(tw);
    	
    	etTitle.setOnDragListener(new FakeDropTarget());
    	etContent.setOnDragListener(new FakeDropTarget());
    	etTags.setOnDragListener(new FakeDropTarget());
    }
}
