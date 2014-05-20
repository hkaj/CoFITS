package utc.bsfile.main;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.keyboard.ValidateKeyboard;

public class LoginScene extends CofitsDesignScene {

	public LoginScene(AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);

		addLoginMenuProcess();
	}

	private void addLoginMenuProcess() {
		getCanvas().registerInputProcessor(new TapAndHoldProcessor(getMTApplication(), 1000));
		getCanvas().addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(getMTApplication(), getCanvas()));
	
		getCanvas().addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent th = (TapAndHoldEvent)ge;
				switch (th.getId()) {
				case TapAndHoldEvent.GESTURE_STARTED:
					break;
				case TapAndHoldEvent.GESTURE_UPDATED:
					break;
				case TapAndHoldEvent.GESTURE_ENDED:
					if (th.isHoldComplete()){
						playKeyboard(th.getLocationOnScreen());
					}
					break;
				default:
					break;
				}
				return false;
			}

			private void playKeyboard(Vector3D locationOnScreen) {
				m_keyboard = new ValidateKeyboard(getMTApplication(),"LAMBDA USER");
				m_keyboard.translate(locationOnScreen, TransformSpace.GLOBAL);
				m_keyboard.setVisible(true);
				getCanvas().addChild(m_keyboard);				
			}
		});
	}
	
	//Members
	private AbstractShape m_keyboard;

}


