package utc.bsfile.main;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.controlorb.ControlOrb;

public class ProjectChoiceScene extends CofitsDesignScene {

	public ProjectChoiceScene(AbstractMTApplication mtApplication, String name) {
		this(mtApplication,name,new ArrayList<ControlOrb>(), false);
	}
	
	
	public ProjectChoiceScene(AbstractMTApplication mtApplication, String name, List<ControlOrb> orbs, boolean doClearOrbGestures) {
		super(mtApplication, name);
		
		//Add the orbs to the list of Orbs
		for (ControlOrb orb : orbs){
			if (doClearOrbGestures){
				clearAllGestures(orb);
				orb.setDefaultGestureActions();
			}
			
			addOrb(orb);
		}
		
		//Manage the admin Orb
		if (!m_orbs.isEmpty()){
			ControlOrb adminOrb = m_orbs.get(0);
			
			processInputForOrb(adminOrb);
			
			getCanvas().addChild(adminOrb);
		}
		
	}


	protected void processInputForOrb(final ControlOrb orb) {
		//Tap long on the orb
		orb.registerInputProcessor(new TapAndHoldProcessor(getMTApplication(),1000));
		orb.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(getMTApplication(), orb));
		orb.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent th = (TapAndHoldEvent)ge;
				switch (th.getId()) {
				case TapAndHoldEvent.GESTURE_STARTED:
					break;
				case TapAndHoldEvent.GESTURE_UPDATED:
					break;
				case TapAndHoldEvent.GESTURE_ENDED:
					if (th.isHoldComplete()){
						//Create a keyboard if does not exist
						playProjectAndSessionsList(orb.getCenterPointGlobal(), orb);
					}
					break;
				default:
					break;
				}
				return false;
			}
		});		
	}


	/**
	 * @param position - The position where the list will be drawn
	 * @param orb - The orb which list will be attached to
	 */
	protected void playProjectAndSessionsList(Vector3D position, ControlOrb orb) {
		//TODO
		//Create the model from Json File
		System.out.println("Create the model from JSON File");
	}
	
	
	@Override
	protected void close() {
		// TODO Auto-generated method stub
		super.close();
	}
	
	
	
	//Members
	

}
