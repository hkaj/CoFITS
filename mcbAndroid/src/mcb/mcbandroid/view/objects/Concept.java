package mcb.mcbandroid.view.objects;


import mcb.mcbandroid.R;
import android.content.Context;
import android.widget.TextView;

public class Concept extends TextView {

	public static int MAX_LENGTH = 20;
	
	public Concept(Context context){
		this(context, new mcb.model.Memorae.Concept());
	}
	
	public Concept(Context context, mcb.model.Memorae.Concept c) {
		super(context);
		this.setBackgroundResource(R.drawable.concept);
		this.setTextColor(getResources().getColor(android.R.color.black));
		this.setHeight(90);
		this.setWidth(90);
		this.setPadding(3, 3, 3, 3);
		this.setText(c.nom);
	}
	
}
