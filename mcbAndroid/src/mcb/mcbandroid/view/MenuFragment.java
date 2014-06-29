package mcb.mcbandroid.view;

import java.util.UUID;

import mcb.mcbandroid.Application;
import mcb.mcbandroid.listener.DropTarget;
import mcb.util.i18nString;

import mcb.mcbandroid.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MenuFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_fragment, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	final Button bLang = (Button) getActivity().findViewById(R.id.langB);
    	bLang.setBackgroundResource(R.drawable.button);
    	bLang.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
			    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			    builder.setTitle(R.string.pref_language)
			           .setItems(SettingsFragment.getLanguageList(), 
	        		   new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	   Application app = (Application) getActivity().getApplication();
			            	   app.onSetLanguage(getFragmentManager(), bLang, SettingsFragment.getLanguageValueList()[which]);
			               }
			           })
			           .show();
			}
		});
    	bLang.setText( i18nString.languagePreferencies.get(0).getName().getValue() );
    	
    	Button bCreatePostIt = (Button) getActivity().findViewById(R.id.createPostIt);
    	bCreatePostIt.setBackgroundResource(R.drawable.button);
    	bCreatePostIt.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				Application app = (Application)getActivity().getApplication();
				app.onClickNew(view);
			}
		});    
    	    	
    	Button bRemove = (Button) getActivity().findViewById(R.id.remove);
    	bRemove.setBackgroundResource(R.drawable.button);
    	bRemove.setOnDragListener(new DropTarget() {
            public void action(View v, UUID id) {
				((Application)getActivity().getApplication()).onDropOnTrash(id);
			}
		});
  	
    	Button bLocal = (Button) getActivity().findViewById(R.id.local);
    	bLocal.setBackgroundResource(R.drawable.button);
    	bLocal.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				Application app = (Application)getActivity().getApplication();
				app.onGoLocalMode(view);
			}
		});
    	bLocal.setOnDragListener(new DropTarget() {
            public void action(View v, UUID id) {
				((Application)getActivity().getApplication()).onDropOnLocal(v,id);
			}
		});
    	bLocal.setSelected(true);

    	
    	Button bMeeting = (Button) getActivity().findViewById(R.id.meeting);
    	bMeeting.setBackgroundResource(R.drawable.button);
    	bMeeting.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				Application app = (Application)getActivity().getApplication();
				app.onGoMeetingMode(view);
			}
		});
    	bMeeting.setOnDragListener(new DropTarget() {
            public void action(View v, UUID id) {
				((Application)getActivity().getApplication()).onDropOnMeeting(v,id);
			}
		});
   	
    	Button bMemorae = (Button) getActivity().findViewById(R.id.memorae);
    	bMemorae.setBackgroundResource(R.drawable.button);
    	bMemorae.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				Application app = (Application)getActivity().getApplication();
				app.onGoMemoraeMode(view);
			}
		});    	
    	bMemorae.setOnDragListener(new DropTarget() {
            public void action(View v, UUID id) {
				((Application)getActivity().getApplication()).onDropOnMermorae(v,id);
			}
		});

    }
}
