package mcb.mcbandroid.listener;

import java.util.UUID;

import android.content.ClipData;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

public abstract class DropTarget implements OnDragListener {
	public boolean onDrag(View v, DragEvent event) {
		if ( event.getAction() == DragEvent.ACTION_DROP ) {
            ClipData.Item item = event.getClipData().getItemAt(0);
            UUID droppedtem = UUID.fromString(item.getText().toString());
            action(v,droppedtem);
			v.setActivated(false);
		}
		else if ( event.getAction() == DragEvent.ACTION_DRAG_ENTERED ) {
			v.setActivated(true);
		}
		else if ( event.getAction() == DragEvent.ACTION_DRAG_EXITED ) {
			v.setActivated(false);
		}
		
		return true;
	}
	
	protected abstract void action(View v, UUID droppedtem);
}
