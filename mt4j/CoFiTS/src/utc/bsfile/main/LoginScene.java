package utc.bsfile.main;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.gui.widget.keyboard.ValidateKeyboard;
import utc.bsfile.gui.widget.keyboard.ValidateKeyboard.ValidateKBEvent;
import utc.bsfile.gui.widget.keyboard.ValidateKeyboard.ValidateKBListener;

public class LoginScene extends CofitsDesignScene implements ValidateKBListener {

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

			private ValidateKeyboard playKeyboard(Vector3D locationOnScreen) {
				//Construction
				ValidateKeyboard keyboard = new ValidateKeyboard(getMTApplication(),"LAMBDA USER");
				m_keyboards.add(keyboard);
				
				//Visibility and location
				keyboard.translate(locationOnScreen, TransformSpace.GLOBAL);
				keyboard.setVisible(true);
				
				//Listeners
				keyboard.addValidateKBListener(LoginScene.this);
				
				getCanvas().addChild(keyboard);
				
				return keyboard;
			}
		});
	}

	@Override
	public void validate(ValidateKBEvent evt) {
		ValidateKeyboard keyboard = evt.getValidateKB();
		ControlOrb orb = keyboard.getControlOrb();
		
		//Process the keyboard/orb relation
		if (orb == null) {
			
			//Create a new orb
			orb = playOrb(new Vector3D(), keyboard);
			orb.setPositionRelativeToOther(keyboard, new Vector3D(0,-10));
			
		} else {
			
			//Update the login in the orb
			orb.setLogin("UNNAMED UPDATED");
			
		}
		
	}

	/**
	 * @param orbLocation - Global location of the orb
	 * @param keyboard - Keyboard to be attached to the new orb
	 * @return the new orb
	 */
	protected ControlOrb playOrb(Vector3D orbLocation, ValidateKeyboard keyboard) {
		ControlOrb orb = playOrb(orbLocation);
		orb.setKeyboard(keyboard);
		keyboard.setControlOrb(orb);
		
		return orb;
	}
	
	
	/**
	 * @param orbLocation - Global location of the orb
	 * @return the new orb
	 * Creates an orb and add it to the list of orbs
	 */
	protected ControlOrb playOrb(Vector3D orbLocation){
		ControlOrb orb = new ControlOrb(getMTApplication(), orbLocation, "UNNAMED");
		m_orbs.add(orb);
		
		//Gesture Listeners
		orb.addGestureListener(DragProcessor.class, new InertiaDragAction());
		
		getCanvas().addChild(orb);
		
		return orb;
	}
	
	//Members
	List<ControlOrb> m_orbs = new ArrayList<ControlOrb>();
	List<ValidateKeyboard> m_keyboards = new ArrayList<ValidateKeyboard>();

}


