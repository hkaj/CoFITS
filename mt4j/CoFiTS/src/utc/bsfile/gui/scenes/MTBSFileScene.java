package utc.bsfile.gui.scenes;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultPanAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.gui.widget.menu.ChoiceListener;
import utc.bsfile.gui.widget.menu.ListMenu.ChoiceEvent;
import utc.bsfile.gui.widget.pick.PickFileChooser;
import utc.bsfile.model.CofitsFile;
import utc.bsfile.model.CofitsModel;
import utc.bsfile.model.Constants;
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.model.menu.ProjectArchitectureModel;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.PropertyManager;

public class MTBSFileScene extends CofitsDesignScene implements ChoiceListener{
	

	public MTBSFileScene(AbstractMTApplication mtApplication, String name, CofitsModel model, List<ControlOrb> orbs) {
		this(mtApplication, name, model, orbs, false);
	}
	
	public MTBSFileScene(AbstractMTApplication mtApplication, String name, CofitsModel model, List<ControlOrb> orbs, boolean doCleanGestures) {
		super(mtApplication, name, model, orbs, doCleanGestures);
		this.setClearColor(new MTColor(120, 120, 120, 255));
		this.registerGlobalInputProcessor(new CursorTracer(getMTApplication(), this));

		// set orbs orientables for the present scene
		for (ControlOrb orb : orbs) {
			orb.setApplication(mtApplication);
			orb.addOrientationListener(orb);
		}
		
		//Display the orbs
		for (ControlOrb orb : m_orbs){
			orb.setVisible(true);
		}		

		float verticalPad = 53;

		MTColor white = new MTColor(255, 255, 255);
		final MTColor textAreaColor = new MTColor(50, 50, 50, 255);

		//Font to use for any text component
		String sfont = PropertyManager.getInstance().getProperty(PropertyManager.MAIN_FONT);
		IFont font = FontManager.getInstance().createFont(getMTApplication(), sfont, 35,white);
		
		//Create the background text
		MTTextArea backgroundPan = new MTTextArea(mtApplication, font);
		backgroundPan.setFillColor(new MTColor(150, 150, 150));
		backgroundPan.setNoFill(true);
		backgroundPan.setNoStroke(true);

		backgroundPan.setText("CoFiTS");

		backgroundPan.setPickable(false);
		this.getCanvas().addChild(backgroundPan);
		backgroundPan.setPositionGlobal(new Vector3D(getMTApplication().width / 2f, getMTApplication().height / 2f + 1 * verticalPad, 0));
		
		//Listen the Input on the pan with two fingers
		getCanvas().registerInputProcessor(new PanProcessorTwoFingers(getMTApplication()));	//Give to component Canvas a two fingers listening
		getCanvas().addGestureListener(PanProcessorTwoFingers.class,new DefaultPanAction());		
		
		// Add component multi-touch gestures
		MTTextArea dragOnly = new MTTextArea(mtApplication, font);
		dragOnly.setFillColor(textAreaColor);
		dragOnly.setStrokeColor(textAreaColor);
		dragOnly.setText("Drag me!");
		
		
		//Gesture listeners for dragOnly Component
		this.clearAllGestures(dragOnly);	//Remove all the registered gesture for this component
		dragOnly.registerInputProcessor(new DragProcessor(getMTApplication()));
		dragOnly.addGestureListener(DragProcessor.class, new DefaultDragAction());
		dragOnly.addGestureListener(DragProcessor.class, new InertiaDragAction()); // Add inertia to dragging
		//this.getCanvas().addChild(dragOnly);
		
		for (ControlOrb cOrb : orbs ) {
			processInputForOrb(cOrb);
		}
		
		m_model = model;
	}
	
	protected void processInputForOrb(final ControlOrb orb) {
		//Tap long on the orb
		orb.registerInputProcessor(new TapAndHoldProcessor(getMTApplication(),500));
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
						playPickFileChooser(orb.getCenterPointGlobal());
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
	 * Defines the process in order to create a FileChooser on the screen
	 */
//	private void addPickFileChooser() {
//		getCanvas().registerInputProcessor(new TapAndHoldProcessor(getMTApplication(), 1000));
//		getCanvas().addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(getMTApplication(), getCanvas()));
//	
//		getCanvas().addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
//			public boolean processGestureEvent(MTGestureEvent ge) {
//				TapAndHoldEvent th = (TapAndHoldEvent)ge;
//				switch (th.getId()) {
//				case TapAndHoldEvent.GESTURE_STARTED:
//					break;
//				case TapAndHoldEvent.GESTURE_UPDATED:
//					break;
//				case TapAndHoldEvent.GESTURE_ENDED:
//					if (th.isHoldComplete()){
//						playPickFileChooser(th.getLocationOnScreen());
//					}
//					break;
//				default:
//					break;
//				}
//				return false;
//			}
//		});
//	}
	
	
	
	/**
	 * @param location - coordinate where the FileChooser will be drawn
	 * @brief Draw a FileChooser on the Canvas at the given location
	 */
	public void playPickFileChooser(Vector3D location) {
		//TODO pass project node chosen by user as last argument
		final PickFileChooser pick;
		m_model.getProjectsArchitectureRootNode().displayConsole(0);
		pick = new PickFileChooser(getMTApplication(), new ProjectArchitectureModel(m_model.getProjectsArchitectureRootNode(), m_model.getProjectsArchitectureRootNode(), ProjectArchitectureModel.FILE_LEVEL),m_model.getProjectsArchitectureRootNode());
		//location.translate(new Vector3D(-100,0));
		pick.translate(location, TransformSpace.GLOBAL);
		((PickFileChooser)pick).updateOrientation(location.x, location.y);
		getCanvas().addChild(pick);
		m_pickFileChoosers.add(pick);
		
		pick.getLaunchAgentsButton().addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent evt) {
				switch (evt.getId()) {
				case TapEvent.GESTURE_ENDED :
					m_model.launchAgentContainer();
					pick.getLaunchAgentsButton().setEnabled(false);
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		pick.addChoiceListener(this);
	}

	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (evt.getPropertyName().equals("Project changed")){
			for (PickFileChooser pick : m_pickFileChoosers){
				pick.projectHasToBeRefresh((TwoLinkedJsonNode) evt.getNewValue());
			}
		} else if (evt.getPropertyName().equals("Architecture changed")) {
			for (PickFileChooser pick : m_pickFileChoosers){
				pick.projectHasToBeRefresh((TwoLinkedJsonNode) evt.getNewValue());
			}
		}
	}
	
	
	@Override
	protected void processFileDownloaded(int id) {
		super.processFileDownloaded(id);
		
		CofitsFile coFile = m_model.getFile(id);
		String filename = coFile.getFilename();
		File file = new File(filename);
		if(m_filesToOpen.containsKey(filename)){
			m_filesToOpen.get(filename).createFileViewer(file);
			m_filesToOpen.remove(filename);
		}
	}
	
	
	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		final File file = new File(choiceEvent.getChoice());
		String filename = file.getPath();	//TODO Check whether the filename is good or not
		PickFileChooser fileChooser = (PickFileChooser) choiceEvent.getListMenu();
		
		System.out.println(filename);
		
		if (m_model.getFile(filename).isLocal()){	
			fileChooser.createFileViewer(file);
		} else {
			int fileId = m_model.getFile(filename).getId();
			addFileToOpen(filename, fileChooser);
			
			m_model.downloadFile(fileId);
		}

	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
		// TODO Auto-generated method stub
		
	} 
	
	
	//Getters & Setters
	public final Map<String, PickFileChooser> getFilesToOpen(){return m_filesToOpen;}
	public void addFileToOpen(String filename, PickFileChooser fileChooser) {m_filesToOpen.put(filename, fileChooser);}
	
	
	//Members
	protected List<PickFileChooser> m_pickFileChoosers = new ArrayList<PickFileChooser>();
	protected Map<String, PickFileChooser> m_filesToOpen = new HashMap<String, PickFileChooser>();

}
