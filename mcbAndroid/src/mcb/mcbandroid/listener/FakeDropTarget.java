package mcb.mcbandroid.listener;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

public class FakeDropTarget implements OnDragListener {
	public boolean onDrag(View v, DragEvent event) {
		return true;
	}
}
