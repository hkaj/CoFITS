package mcb.mcbandroid.view.objects;

import java.util.UUID;

import android.view.View;
import mcb.mcbandroid.Application;
import mcb.util.i18nString.Language;
import mcb.mcbandroid.R;
import mcb.mcbandroid.listener.DropTarget;

/**
 * View object for a mcb.model.Cluster
 */
public class Cluster extends mcb.mcbandroid.view.objects.BaseObject {

	public Cluster(final Application app, final mcb.model.Cluster bo) {
		super(app,bo);
		this.setBackgroundResource(R.drawable.cluster);
		
		Language language = app.getCurrentLanguage();
		
		// use auto trad
		if ( bo.getContent().getContent().containsKey(language) == false ) {
			this.setTextColor( getResources().getColor(R.color.autotrad) );
		}

		if ( app.getUseTranslator() )
			this.setText(bo.getContent().get(language));
		else
			this.setText(bo.getContent().getValue());
		
		this.setOnDragListener(new DropTarget() {
            final protected UUID thisId = bo.getId();
			
			@Override
			protected void action(View v, UUID droppedtem) {
                app.onDropOnCluster(thisId, droppedtem);
			}
		});
	}

	public Cluster(Application app, mcb.model.Cluster cluster, boolean singleLine) {
		this(app, cluster);
		
		if ( singleLine ) {
			this.setSingleLine();
		}
	}
	
	@Override
	public void setSelectedWithSave(boolean byMe, boolean byOther) {
		super.setSelectedWithSave(byMe, byOther);
		if ( byOther )
			this.setBackgroundResource(R.drawable.bcluster);
		else
			this.setBackgroundResource(R.drawable.cluster);
	}
}
