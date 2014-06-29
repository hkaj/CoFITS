package mcb.mcbandroid.utils;

import java.util.ArrayList;
import java.util.List;

import mcb.mcbandroid.Application;

import mcb.model.BaseObject;
import mcb.model.Cluster;
import mcb.model.PostIt;
import mcb.mcbandroid.R;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Basically, this is the object observed by the GidFragment/GridView
 * 
 * When this object change, the view is updated accordingly
 * 
 * @see android.widget.BaseAdapter
 */
public class ObjectAdapter extends BaseAdapter {

	protected Application mApp;
    protected List<BaseObject> items = new ArrayList<BaseObject>();
	
    public ObjectAdapter(Application app) {
        mApp = app;
    }
    
    public void updateContent(List<BaseObject> items) {
    	this.items.clear();
    	
    	for ( BaseObject bo : items ) {
    		if ( bo instanceof Cluster )
    			this.items.add(bo);
    	}
    	for ( BaseObject bo : items ) {
    		if ( !(bo instanceof Cluster) )
    			this.items.add(bo);
    	}
    	
    	this.notifyDataSetChanged();
    }
    
	public int getCount() {
		// +1 for the virtual object "ADD"
		return items.size() + 1;
	}

	public Object getItem(int index) {
		return null;
	}

	public long getItemId(int index) {
		return 0;
	}

	public View getView(final int index, View arg1, ViewGroup arg2) {
		
		// Object
		if ( items.size() > index ) { 
			mcb.mcbandroid.view.objects.BaseObject o = null;

			if ( items.get(index) instanceof Cluster ) 
				o = new mcb.mcbandroid.view.objects.Cluster(mApp, (Cluster) items.get(index));
			else if ( items.get(index) instanceof PostIt )
				o = new mcb.mcbandroid.view.objects.PostIt(mApp, (PostIt) items.get(index));
			else
				o = new mcb.mcbandroid.view.objects.BaseObject(mApp, items.get(index));
			
			
			boolean selectedByMe = items.get(index).getSelectedBy().contains(mApp.getUser());
			boolean selectedByOther = (selectedByMe && items.get(index).getSelectedBy().size() > 1) || (!selectedByMe && items.get(index).getSelectedBy().size() >= 1);
			
			o.setSelectedWithSave(
					selectedByMe, 
					selectedByOther
				);
						
			return o;
		}
		
		// ADD button
		else {
			final TextView tv = new TextView(arg2.getContext());
			tv.setText("+");
			tv.setHeight(90);
			tv.setWidth(90);
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(tv.getResources().getColor(android.R.color.black));
			tv.setBackgroundColor(tv.getResources().getColor(R.color.gbackground));

			class AddClickListener implements OnClickListener {
				protected final View myView;

				public void onClick(View v) {
					mApp.onClickNew(myView);
				}
				
				public AddClickListener(View v) {
					myView = v;
				}
			}
			AddClickListener acl = new AddClickListener(arg2.getRootView().findViewById(R.id.main));
			tv.setOnClickListener(acl);
			
			return tv;
		}
	}
}
