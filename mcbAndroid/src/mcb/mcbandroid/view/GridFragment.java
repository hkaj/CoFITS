package mcb.mcbandroid.view;


import java.util.UUID;

import mcb.mcbandroid.Application;
import mcb.mcbandroid.listener.DropTarget;
import mcb.mcbandroid.listener.FakeDropTarget;
import mcb.util.i18nString;

import mcb.mcbandroid.R;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class GridFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.grid_fragment, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	Application app = (Application) getActivity().getApplication();
        
    	GridView gv = (GridView) getActivity().findViewById(R.id.gridview);
    	gv.setAdapter( app.getObjectAdapter() );
		gv.setBackgroundResource(R.drawable.gridbackground);
  	
    	Button b = (Button) getActivity().findViewById(R.id.gridUpB);
    	b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Application app = (Application)getActivity().getApplication();
				app.onGoUp(v);
			}
		});
    	b.setOnDragListener(new DropTarget() {
            public void action(View v, UUID id) {
				((Application)getActivity().getApplication()).onDropOnRoot(v,id);
			}
		});
    	b.setBackgroundResource(R.drawable.button);

    	b = (Button) getActivity().findViewById(R.id.gridClusterTitleB);
    	b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Application app = (Application)getActivity().getApplication();
				EditText et = (EditText)v.getRootView().findViewById(R.id.gridClusterTitleET);
				app.onSetClusterTitle(et.getText().toString(), i18nString.languagePreferencies.get(0));
				Toast.makeText(v.getContext(), getResources().getText(R.string.toast_rename_cluster), Toast.LENGTH_SHORT).show();
			}
		});
    	b.setBackgroundResource(R.drawable.button);
   	
    	EditText et = (EditText) getActivity().findViewById(R.id.gridFilterTagsET);
    	et.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Application app = (Application)getActivity().getApplication();
				app.filterByTags(s.toString());
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});
		et.setOnDragListener(new FakeDropTarget());

    }
    
    public void LockGrid() {
    	getActivity().findViewById(R.id.gridClusterTitleLL).setVisibility(View.INVISIBLE);
    	getActivity().findViewById(R.id.gridFilterTagsTV).setVisibility(View.INVISIBLE);
    	getActivity().findViewById(R.id.gridFilterTagsET).setVisibility(View.INVISIBLE);
    	getActivity().findViewById(R.id.gridview).setSelected(true);
    }
    
    public void UnlockGrid() {
    	getActivity().findViewById(R.id.gridClusterTitleLL).setVisibility(View.VISIBLE);
    	getActivity().findViewById(R.id.gridFilterTagsTV).setVisibility(View.VISIBLE);
    	getActivity().findViewById(R.id.gridFilterTagsET).setVisibility(View.VISIBLE);
    	getActivity().findViewById(R.id.gridview).setSelected(false);
   }
}
