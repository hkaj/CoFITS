package utc.tatinpic.gui.widgets.menu.circularmenu;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.math.Vector3D;

public class UpdateWorkbenchPositionListener implements IGestureEventListener{

	WorkbenchHandler workbenchHandler;
	
	public UpdateWorkbenchPositionListener(WorkbenchHandler wrb)
	{
		workbenchHandler = wrb;
	}
	
	@Override
	public boolean processGestureEvent(MTGestureEvent ge) {

		DragEvent dragEvent = (DragEvent)ge;
		
		switch (dragEvent.getId()) {
		
		//case MTGestureEvent.GESTURE_STARTED:
		case MTGestureEvent.GESTURE_UPDATED:
		case MTGestureEvent.GESTURE_ENDED:
			
			//Vector3D global = workbenchHandler.getCenterPointGlobal();
			float [] pos = new float [2];
			pos[0] = dragEvent.getDragCursor().getCurrentEvtPosX() / workbenchHandler.getRenderer().width;
			pos[1] = dragEvent.getDragCursor().getCurrentEvtPosY() / workbenchHandler.getRenderer().height;
			workbenchHandler.getWorkbench().setPosition(pos);
			
		default:
			break;
			
		}

	return false;
	}

}
