package utc.bsfile.main;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.transition.FadeTransition;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.gui.widget.menu.ProjectChoiceListMenu;
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.model.menu.ProjectArchitectureModel;

public class ProjectChoiceScene extends CofitsDesignScene {

	private final static boolean DO_CLEAN_GESTURES = true;
	
	
	public ProjectChoiceScene(AbstractMTApplication mtApplication, String name) {
		this(mtApplication,name,new ArrayList<ControlOrb>(), false);
	}
	
	
	public ProjectChoiceScene(AbstractMTApplication mtApplication, String name, List<ControlOrb> orbs, boolean doClearOrbGestures) {
		super(mtApplication, name, orbs, doClearOrbGestures);
		
		//Manage the admin Orb
		if (!m_orbs.isEmpty()){
			ControlOrb adminOrb = m_orbs.get(0);
			processInputForOrb(adminOrb);
		}
		
		
		for (int i = 1; i < m_orbs.size(); ++i){
			m_orbs.get(i).setVisible(false);
			m_orbs.get(i).setEnabled(false);
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
		//Create the model from Json File
		IMenuModel model = new ProjectArchitectureModel(new File("rsc/config/structure.json"), ProjectArchitectureModel.SESSION_LEVEL);
		final ProjectChoiceListMenu projectList = new ProjectChoiceListMenu(getMTApplication(), (int) position.x, (int) position.y, 200, 5, model);
		projectList.setMustBeDestroy(false);
		
		projectList.getConfirmButton().addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent evt) {
				switch (evt.getId()) {
				case TapEvent.GESTURE_ENDED :
					System.out.println("OK");
					launchProjectChoiceScene();
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		projectList.getLaunchAgentsButton().addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent evt) {
				switch (evt.getId()) {
				case TapEvent.GESTURE_ENDED :
					StartCofitsEntities.launchAgentContainer(ProjectChoiceScene.this);
					projectList.getLaunchAgentsButton().setEnabled(false);
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		getCanvas().addChild(projectList);

	}
	
	
	protected void launchProjectChoiceScene() {
		setTransition(new FadeTransition(getMTApplication(), 1700));	//Set a fade transition between the two scenes
		//Save the current scene on the scene stack before changing
		MTBSFileScene mtbsFileScene = new MTBSFileScene(getMTApplication(), "Project Choice Scene", m_orbs, DO_CLEAN_GESTURES);
		//Add the scene to the mt application
		getMTApplication().addScene(mtbsFileScene);
		
		//Do the scene change
		getMTApplication().changeScene(mtbsFileScene);
		
		//Close the scene
		close();
	}


	@Override
	protected void close() {
		// TODO Auto-generated method stub
		super.close();
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);		
	}


	@Override
	protected void processFileDownloaded(String filename) {
		super.processFileDownloaded(filename);
		
	}
	
	
	
	//Members
	

}
