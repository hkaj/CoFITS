package utc.bsfile.gui.scenes;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
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
import utc.bsfile.gui.widget.menu.ChoiceListener;
import utc.bsfile.gui.widget.menu.ListMenu.ChoiceEvent;
import utc.bsfile.gui.widget.menu.ProjectChoiceListMenu;
import utc.bsfile.model.CofitsModel;
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.model.menu.ProjectArchitectureModel;
import utc.bsfile.model.menu.TwoLinkedJsonNode;

public class ProjectChoiceScene extends CofitsDesignScene implements ChoiceListener {

	private final static boolean DO_CLEAN_GESTURES = true;
	
	
	public ProjectChoiceScene(AbstractMTApplication mtApplication, String name, CofitsModel model) {
		this(mtApplication,name, model,new ArrayList<ControlOrb>(), false);
	}
	
	
	public ProjectChoiceScene(AbstractMTApplication mtApplication, String name, CofitsModel model, List<ControlOrb> orbs, boolean doClearOrbGestures) {
		super(mtApplication, name, model, orbs, doClearOrbGestures);
		
		// set orbs orientables for the present scene
		for (ControlOrb orb : orbs) {
			orb.setApplication(mtApplication);
			orb.addOrientationListener(orb);
		}
		
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
		orb.registerInputProcessor(new TapAndHoldProcessor(getMTApplication(), 500));
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
		m_list = new ProjectChoiceListMenu(getMTApplication(), (int) position.x, (int) position.y, 200, 5, model);
		m_list.setMustBeDestroy(false);
		m_list.addChoiceListener(this);
		
		m_list.getConfirmButton().addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent evt) {
				switch (evt.getId()) {
				case TapEvent.GESTURE_ENDED :
					launchProjectChoiceScene(m_list.getModel());
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		//Manage connection button
		boolean isConnected = m_model.isConnected();
		m_list.getLaunchAgentsButtonOff().setEnabled(!isConnected);
		m_list.getLaunchAgentsButtonOff().setVisible(!isConnected);
		m_list.getLaunchAgentsButtonOn().setVisible(isConnected);
		m_list.getLaunchAgentsButtonOn().setEnabled(false);
		
		m_list.getLaunchAgentsButtonOff().addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent evt) {
				switch (evt.getId()) {
				case TapEvent.GESTURE_ENDED :
					m_model.launchAgentContainer();
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		getCanvas().addChild(m_list);

	}
	
	
	protected void launchProjectChoiceScene(IMenuModel model) {
		setTransition(new FadeTransition(getMTApplication(), 1500));	//Set a fade transition between the two scenes
		//Save the current scene on the scene stack before changing
		MTBSFileScene mtbsFileScene = new MTBSFileScene(getMTApplication(), "Project Choice Scene", m_model, m_orbs, DO_CLEAN_GESTURES, m_projectChosenName, m_sessionChosenName);
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
		if (evt.getPropertyName().equals("Architecture changed")){
			ProjectArchitectureModel model = new ProjectArchitectureModel((TwoLinkedJsonNode)evt.getNewValue(), (TwoLinkedJsonNode)evt.getNewValue(), ProjectArchitectureModel.SESSION_LEVEL);
			m_list.changeModel(model);
		} else if (evt.getPropertyName().equals("Project changed")){
			ProjectArchitectureModel model = new ProjectArchitectureModel((TwoLinkedJsonNode)evt.getNewValue(), (TwoLinkedJsonNode)evt.getNewValue(), ProjectArchitectureModel.SESSION_LEVEL);
			m_list.changeModel(model);
		} else if (evt.getPropertyName().equals("Agent created")) {
			m_list.getLaunchAgentsButtonOff().setEnabled(false);
			m_list.getLaunchAgentsButtonOff().setVisible(false);
			
			m_list.getLaunchAgentsButtonOn().setVisible(true);
		}
	}

	
	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		m_sessionChosenName = choiceEvent.getChoice();
		if(((TwoLinkedJsonNode) m_list.getCurrentNode()).getParent()!= null){
			m_projectChosenName = ((TwoLinkedJsonNode) m_list.getCurrentNode()).getName();		
		}
	}


	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void processFileDownloaded(int id) {
		super.processFileDownloaded(id);
		
	}
	
	//Members
	ProjectChoiceListMenu m_list;
	String m_sessionChosenName = null;
	String m_projectChosenName = null;
}
