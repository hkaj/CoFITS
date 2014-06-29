package mcb.mcbandroid.view.objects;

import mcb.mcbandroid.Application;
import mcb.util.i18nString.Language;
import mcb.mcbandroid.R;

/**
 * View object for a mcb.model.PostIt
 */
public class PostIt extends mcb.mcbandroid.view.objects.BaseObject {

	public static int MAX_LENGTH = 20;
	
	public PostIt(Application app, mcb.model.PostIt bo) {
		super(app,bo);
		this.setBackgroundResource(R.drawable.postit);
		
		Language language = app.getCurrentLanguage();
		
		if ( bo.getTitle().getValue() != null && bo.getTitle().getValue().isEmpty() == false ) {
			this.setText(bo.getTitle().getValue());
			
			if ( bo.getTitle().getContent().containsKey(language) == false ) {
				this.setTextColor( getResources().getColor(R.color.autotrad) );
				
				if ( app.getUseTranslator() )
					this.setText(bo.getTitle().get(language));
			}
		}
		else {
			String str = bo.getContent().getValue();
			
			if ( bo.getContent().getContent().containsKey(language) == false ) {
				this.setTextColor( getResources().getColor(R.color.autotrad) );
				
				if ( app.getUseTranslator() )
					str = bo.getContent().get(language);
			}
			
			if ( str.length() > MAX_LENGTH ) {
				str = str.substring(0, MAX_LENGTH);
				str += "...";
			}
			
			this.setText(str);
		}
	}
	
	@Override
	public void setSelectedWithSave(boolean byMe, boolean byOther) {
		super.setSelectedWithSave(byMe, byOther);
		if ( byOther )
			this.setBackgroundResource(R.drawable.bpostit);
		else
			this.setBackgroundResource(R.drawable.postit);
	}
}
