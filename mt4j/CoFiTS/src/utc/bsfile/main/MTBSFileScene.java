package utc.bsfile.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.gui.widget.pick.PickFileChooser;
import utc.bsfile.model.Constants;
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.util.PropertyManager;

public class MTBSFileScene extends CofitsDesignScene implements PropertyChangeListener {
	
	public MTBSFileScene(AbstractMTApplication mtApplication, String name, List<ControlOrb> orbs) {
		this(mtApplication, name, orbs, false, null);
	}
	
	public MTBSFileScene(AbstractMTApplication mtApplication, String name, List<ControlOrb> orbs, boolean doCleanGestures, IMenuModel model) {
		super(mtApplication, name, orbs, doCleanGestures);
		this.setClearColor(new MTColor(120, 120, 120, 255));
		//this.registerGlobalInputProcessor(new CursorTracer(app, this));

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
		m_pick = new PickFileChooser(getMTApplication());
		//location.translate(new Vector3D(-100,0));
		m_pick.translate(location, TransformSpace.GLOBAL);
		((PickFileChooser)m_pick).updateOrientation(location.x, location.y);
		//((PickFileChooser)m_pick).setModel(m_model);
		getCanvas().addChild(m_pick);
	}

	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Constants.OPEN_PICK))	
			playPickFileChooser(new Vector3D(400, 200));
	}
	
	//Members
	AbstractShape m_pick;
	IMenuModel m_model;

}
