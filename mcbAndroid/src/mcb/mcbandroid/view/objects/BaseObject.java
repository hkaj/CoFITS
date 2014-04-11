package mcb.mcbandroid.view.objects;

import java.util.UUID;

import mcb.mcbandroid.Application;

import android.content.ClipData;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Base class for the view of a mcb.model.BaseObject
 */
public class BaseObject extends TextView {

	protected final UUID modelId;
	protected boolean selected = false, selectedByOther = false;
	
	public BaseObject(final Application app, mcb.model.BaseObject bo) {
		super(app);
		this.modelId = bo.getId();
		
		this.setTextColor(getResources().getColor(android.R.color.black));
		this.setHeight(90);
		this.setWidth(90);
		this.setPadding(3, 3, 3, 3);
		
		
		this.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				app.onEditObject(v);
			}
		});
		
		this.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				app.onSelectObject(BaseObject.this.getModelId(), v);
				return true;
			}
		});
		
		this.setOnTouchListener(new View.OnTouchListener(){
			protected float x, y;
			protected long downevent;
			
			public boolean onTouch(View v, MotionEvent e){
				// WARNING this values are not OK for ACTION_DOWN
				long duration = Math.abs(downevent - e.getEventTime());
				float diff = Math.abs(e.getX() - x) + Math.abs(e.getY() - y);
				
				if ( e.getAction() == MotionEvent.ACTION_DOWN ) {
					x = e.getX();
					y = e.getY();
					downevent = e.getEventTime();
					return false;
				}
				else if ( e.getAction() == MotionEvent.ACTION_UP ) {
					if ( duration < 100 ) {
						v.performClick();
						return true;
					}
				}
				else if ( e.getAction() == MotionEvent.ACTION_MOVE ) {
					if ( duration > 20 && diff > 4) {
					    ClipData.Item item = new ClipData.Item(modelId.toString());
					    ClipData dragData = new ClipData("uuid", new String[]{"text/plain"}, item);
					    v.startDrag(dragData, new DragShadowBuilder(v), null, 0);
					    return true;
					}
				}
				
		    	return false;
			}
		});
	}
	
	public UUID getModelId() {
		return modelId;
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		super.setSelected(selected);
		super.onDraw(canvas);
	}

	public void setSelectedWithSave(boolean byMe, boolean byOther) {
		selected = byMe;
		setSelected(byMe);
		selectedByOther = byOther;
	}
}
