package utc.bsfile.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
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
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.pick.PickFileChooser;
import utc.bsfile.model.Constants;
import utc.bsfile.util.FileExtensionIconManager;
import utc.bsfile.util.ImageManager;
import utc.bsfile.util.PropertyManager;

/**
 * @author chris
 *
 */
public class MTBSFileScene extends AbstractScene implements PropertyChangeListener {
	
	public MTBSFileScene(AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.app = mtApplication;
		this.setClearColor(new MTColor(126, 130, 168, 255));
		this.registerGlobalInputProcessor(new CursorTracer(app, this));

		float verticalPad = 53;

		MTColor white = new MTColor(255, 255, 255);
		final MTColor textAreaColor = new MTColor(50, 50, 50, 255);

		String sfont = PropertyManager.getInstance().getProperty(PropertyManager.MAIN_FONT);
//		IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 35,	white);
		IFont font = FontManager.getInstance().createFont(app, sfont, 35,white);
		
		////////////////////////
		// 2 finger pan gesture
		////////////////////////
		
		//Create the background text
		MTTextArea backgroundPan = new MTTextArea(mtApplication, font);
		backgroundPan.setFillColor(new MTColor(150, 150, 150));
		backgroundPan.setNoFill(true);
		backgroundPan.setNoStroke(true);
		backgroundPan.setText("Pan anywhere on the background!");
		backgroundPan.setPickable(false);
		this.getCanvas().addChild(backgroundPan);
		backgroundPan.setPositionGlobal(new Vector3D(app.width / 2f, app.height / 2f + 1 * verticalPad, 0));
		
		//Listen the Input on the pan with two fingers
		getCanvas().registerInputProcessor(new PanProcessorTwoFingers(app));	//Give to component Canvas a two fingers listening
		getCanvas().addGestureListener(PanProcessorTwoFingers.class,new DefaultPanAction());

		
		
		// Add component multi-touch gestures
		MTTextArea dragOnly = new MTTextArea(mtApplication, font);
		dragOnly.setFillColor(textAreaColor);
		dragOnly.setStrokeColor(textAreaColor);
		dragOnly.setText("Drag me!");
		
		//Gesture listeners
		this.clearAllGestures(dragOnly);	//Remove all the registered gesture for this component
		dragOnly.registerInputProcessor(new DragProcessor(app));
		dragOnly.addGestureListener(DragProcessor.class, new DefaultDragAction());
		dragOnly.addGestureListener(DragProcessor.class, new InertiaDragAction()); // Add inertia to dragging
		this.getCanvas().addChild(dragOnly);
		
		initManager();
		addPickFileChooser();
		//playPickFileChooser(new Vector3D(200, 200));
	}

	private void clearAllGestures(MTComponent comp) {
		comp.unregisterAllInputProcessors();
		comp.removeAllGestureEventListeners();
	}

	public void onEnter() {
	}

	public void onLeave() {
	}

	private void initManager() {
		ImageManager.getInstance().init(getMTApplication());
		FileExtensionIconManager.getInstance().init(getMTApplication());
	}

	
	
	/**
	 * Defines the process in order to create a FileChooser on the screen
	 */
	private void addPickFileChooser() {
		getCanvas().registerInputProcessor(new TapAndHoldProcessor(app, 1000));
		getCanvas().addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(app, getCanvas()));
	
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
		pick = new PickFileChooser(getMTApplication());
		location.translate(new Vector3D(-100,0));
		pick.translate(location, TransformSpace.GLOBAL);
		getCanvas().addChild(pick);
	}

	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Constants.OPEN_PICK))	
			playPickFileChooser(new Vector3D(400, 200));
	}
	
	//Members
	private AbstractMTApplication app;
	AbstractShape pick;

}
