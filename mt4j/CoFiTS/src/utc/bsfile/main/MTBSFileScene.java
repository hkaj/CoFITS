package utc.bsfile.main;

import jade.gui.GuiEvent;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.List;

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
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.gui.widget.menu.ChoiceListener;
import utc.bsfile.gui.widget.menu.ListMenu.ChoiceEvent;
import utc.bsfile.gui.widget.pick.PickFileChooser;
import utc.bsfile.model.Constants;
import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.util.PropertyManager;

public class MTBSFileScene extends CofitsDesignScene implements ChoiceListener{
	
	public MTBSFileScene(AbstractMTApplication mtApplication, String name, List<ControlOrb> orbs) {
		this(mtApplication, name, orbs, false);
	}
	
	public MTBSFileScene(AbstractMTApplication mtApplication, String name, List<ControlOrb> orbs, boolean doCleanGestures) {
		super(mtApplication, name, orbs, doCleanGestures);


		//this.setClearColor(new MTColor(126, 130, 168, 255));
		this.setClearColor(new MTColor(120, 120, 120, 255));
		this.registerGlobalInputProcessor(new CursorTracer(getMTApplication(), this));

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
		
		addPickFileChooser();
	}
	
	
	/**
	 * Defines the process in order to create a FileChooser on the screen
	 */
	private void addPickFileChooser() {
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
						playPickFileChooser(th.getLocationOnScreen());
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
	 * @param location - coordinate where the FileChooser will be drawn
	 * @brief Draw a FileChooser on the Canvas at the given location
	 */
	public void playPickFileChooser(Vector3D location) {
		m_pick = new PickFileChooser(getMTApplication());
		location.translate(new Vector3D(-100,0));
		m_pick.translate(location, TransformSpace.GLOBAL);
		getCanvas().addChild(m_pick);
	}

	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (evt.getPropertyName().equals(Constants.OPEN_PICK))	
			playPickFileChooser(new Vector3D(400, 200));
	}
	
	
	@Override
	protected void processFileDownloaded(String filename) {
				
	}
	
	//Members
	AbstractShape m_pick;

	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		final File file = new File(choiceEvent.getChoice());
		PickFileChooser fileChooser = (PickFileChooser) choiceEvent.getListMenu();
		
		if (file.exists()){	
			fileChooser.createFileViewer(file);
		} else {
			String filename = file.getAbsolutePath();	//TODO Check whether the filename is good or not
			int fileId = 0;	//TODO Find the ID
			fileChooser.addFileToOpen(filename);
			
			//Send a gui event to the agent
			GuiEvent event = new GuiEvent(this, CofitsGuiAgent.DOWNLOAD_FILE);
			event.addParameter(fileId);
			if (m_agent != null){
				m_agent.postGuiEvent(event);
			} else {
				System.err.println("No Agent running");
			}
		}

	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
		// TODO Auto-generated method stub
		
	} 

}
