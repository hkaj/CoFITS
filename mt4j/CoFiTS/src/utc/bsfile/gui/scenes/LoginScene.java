package utc.bsfile.gui.scenes;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.transition.FadeTransition;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.gui.widget.keyboard.TextEntryValidateKeyboard;
import utc.bsfile.gui.widget.keyboard.ValidateKeyboard;
import utc.bsfile.gui.widget.keyboard.ValidateKeyboard.ValidateKBEvent;
import utc.bsfile.gui.widget.keyboard.ValidateKeyboard.ValidateKBListener;
import utc.bsfile.gui.widget.menu.ListMenu;
import utc.bsfile.model.CofitsModel;
import utc.bsfile.model.CofitsUser;
import utc.bsfile.model.menu.DefaultMenuModel;
import utc.bsfile.util.PropertyManager;

public class LoginScene extends CofitsDesignScene implements ValidateKBListener {

	//Constants
	private static final boolean DO_CLEAN_GESTURES = true;
	AbstractMTApplication applet;
	
	//Constructors
	public LoginScene(AbstractMTApplication mtApplication, String name, CofitsModel model) {
		super(mtApplication, name, model);
		applet = mtApplication;
		m_listOfUsers.setCellsEnabled(false);
		
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

			
		});
	}

	@Override
	public void validate(ValidateKBEvent evt) {
		TextEntryValidateKeyboard keyboard = (TextEntryValidateKeyboard) evt.getValidateKB();
		ControlOrb orb = keyboard.getControlOrb();
		
		if (m_orbs.isEmpty()){
			playListOfUsers(new Vector3D());
			m_listOfUsers.setPositionRelativeToOther(keyboard, new Vector3D(10 + keyboard.getWidthXY(TransformSpace.LOCAL) + m_listOfUsers.getWidthXY(TransformSpace.LOCAL) / 2, keyboard.getHeightXY(TransformSpace.LOCAL) / 2));
			m_listOfUsers.updateOrientation();
		}
		
		//Process the keyboard/orb relation
		if (orb == null) {
			
			//Create a new orb
			orb = playOrb(new Vector3D(), keyboard);
			orb.setPositionRelativeToOther(keyboard, new Vector3D( 40, keyboard.getHeightXY(TransformSpace.LOCAL) - 40));
			keyboard.getColoredEllipse().setFillColor(orb.getFillColor());
			orb.updateOrientation();
			
		} else {
			
			//Update the login in the orb
			orb.changeLogin(keyboard.getText());
			orb.centerLoginString();
			
		}
		
		//We need to set a new model in order to update the list.
		//TODO Find a cleaner way (create new class different than ListMenu ?)
		m_orbsStrings.clear();
		for(ControlOrb orbs : m_orbs){
			m_orbsStrings.add(orbs.getLogin());
		}
		
		m_listOfUsers.setModel(new DefaultMenuModel(null,m_orbsStrings.toArray()));
		m_listOfUsers.updateList();		
	}
	
	//////////////////////////////////
	//	Draw components on the Scene
	//////////////////////////////////
	

	/**
	 * @param locationOnScreen - Location where the Keyboard will be drawn
	 * @return the new keyboard
	 * Creates a new Keyboard and add it to the list of Keyboards
	 */
	protected ValidateKeyboard playKeyboard(Vector3D locationOnScreen){
		return playKeyboard(locationOnScreen, null);
	}
	
	
	/**
	 * @param locationOnScreen - Location where the Keyboard will be drawn
	 * @param orb - The orb to be attached to the new keyboard
	 * @return the new keyboard
	 * Creates a new Keyboard and add it to the list of Keyboards
	 */
	protected ValidateKeyboard playKeyboard(Vector3D locationOnScreen, ControlOrb orb) {
		//Construction
		TextEntryValidateKeyboard keyboard;
		
		if (orb != null){
			keyboard = new TextEntryValidateKeyboard(getMTApplication(), orb.getLogin());
			keyboard.getColoredEllipse().setFillColor(orb.getFillColor());
			orb.setKeyboard(keyboard);
			orb.sendToFront();
		} else {
			keyboard = new TextEntryValidateKeyboard(getMTApplication(), "");
		}
		
		keyboard.setControlOrb(orb);
		m_keyboards.add(keyboard);
		
		//Visibility and location
		keyboard.translate(locationOnScreen, TransformSpace.GLOBAL);
		keyboard.updateOrientation(locationOnScreen.x, locationOnScreen.y);
		
		if (PropertyManager.getInstance().getProperty(PropertyManager.DEVICE).equals("table")) {
			switch (keyboard.getOrientation()) { // for right handed
				case BOTTOM :
					keyboard.translate(new Vector3D(- keyboard.getWidth(), 0), TransformSpace.GLOBAL);
					break;
				case LEFT :
					keyboard.translate(new Vector3D(0, - keyboard.getWidth()), TransformSpace.GLOBAL);
					break;
				case TOP :
					keyboard.translate(new Vector3D(keyboard.getWidth(), 0), TransformSpace.GLOBAL);
					break;
				case RIGHT :
					keyboard.translate(new Vector3D(0, keyboard.getWidth()), TransformSpace.GLOBAL);
					break;
				default :
					System.out.println("NO KEYBOARD ORIENTATION");
					break;
			}
		} else {
			keyboard.translate(new Vector3D(- keyboard.getWidth(), 0), TransformSpace.GLOBAL);
		}
		
		keyboard.setVisible(true);
		
		//Listeners
		keyboard.addValidateKBListener(LoginScene.this);
		
		//keyboard.updateOrientation(locationOnScreen.x, locationOnScreen.y);
		getCanvas().addChild(keyboard);
		
		//Improve the presentation
		if (orb != null){
			orb.sendToFront();
		}
		return keyboard;
	}
	
	
	/**
	 * @param orbLocation - Global location of the orb
	 * @param keyboard - Keyboard to be attached to the new orb
	 * @return the new orb
	 * Creates an orb and add it to the list of orbs
	 */
	protected ControlOrb playOrb(Vector3D orbLocation, TextEntryValidateKeyboard keyboard) {
		if (!m_model.hasUser(keyboard.getText())){
			//Create the user
			CofitsUser user = new CofitsUser(keyboard.getText(), m_orbs.isEmpty());
			m_model.addUser(user);
			
			//Create the orb
			final ControlOrb orb = new ControlOrb(getMTApplication(), orbLocation, user, keyboard);
			keyboard.setControlOrb(orb);
			addOrb(orb);
			getCanvas().addChild(orb);
			
			//Tap long on the orb
			orb.registerInputProcessor(new TapAndHoldProcessor(getMTApplication(), 500));
			orb.removeAllGestureEventListeners(ScaleProcessor.class);
			orb.removeAllGestureEventListeners(RotateProcessor.class);
			
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
							if (orb.getKeyboard() == null){
								playKeyboard(orb.getCenterPointGlobal(), orb);
							}
						}
						break;
					default:
						break;
					}
					return false;
				}
			});
			
			return orb;
			
		} else {
			return getOrb(keyboard.getText());					
		}
		
		 
	}
	
	
	/**
	 * @param orbLocation - Global location of the orb
	 * @return the new orb
	 * Creates an orb and add it to the list of orbs
	 */
	protected ControlOrb playOrb(Vector3D orbLocation){
		return playOrb(orbLocation, null);
	}
	
	
	/**
	 * @param location
	 * @return the list of users
	 * Creates a list that listens to the users' subscriptions
	 */
	protected void playListOfUsers(Vector3D location){		
		m_listOfUsers.setPositionGlobal(location);
		
		m_listOfUsers.setCloseVisible(false);
		
		//Add a title to the list Menu
		String textFontStr = PropertyManager.getInstance().getProperty(PropertyManager.MAIN_FONT);
		IFont textFont = FontManager.getInstance().createFont(getMTApplication(), textFontStr, 16, MTColor.WHITE);
		MTTextField title = new MTTextField(getMTApplication(), 2, 8, m_listOfUsers.getWidthXY(TransformSpace.LOCAL), 20, textFont);
		
		title.setNoStroke(true);
		title.setNoFill(true);
		title.setPickable(false);
		title.setText("List of users");
		
		m_listOfUsers.addChild(title);
		
		//Add a confirmation button
		//MTImageButton button = new MTImageButton(getMTApplication(), ImageManager.getInstance().load("confirm-button.png"));
		MTSvgButton validateButton = new MTSvgButton(applet, MT4jSettings.getInstance().getDefaultSVGPath() + "KeybValidate-green.svg");
		int buttonSize = 40;
		validateButton.setSizeXYGlobal(buttonSize, buttonSize);
		validateButton.setPositionGlobal(new Vector3D(m_listOfUsers.getWidthXY(TransformSpace.LOCAL) / 2, m_listOfUsers.getHeightXY(TransformSpace.LOCAL) - 30));
		
		//button.setPositionRelativeToOther(m_listOfUsers, new Vector3D(m_listOfUsers.getWidthXY(TransformSpace.LOCAL) / 2, m_listOfUsers.getHeightXY(TransformSpace.LOCAL) - 25));
		validateButton.setPickable(true);
		
		
		validateButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent evt) {
				switch (evt.getId()) {
				case TapEvent.GESTURE_ENDED :	
					launchProjectChoiceScene();
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		m_listOfUsers.addChild(validateButton);
		
		m_listOfUsers.setVisible(true);
		getCanvas().addChild(m_listOfUsers);
	}
	
	
	
	/**
	 * Switch to the Project Choice Scene and Close the current one
	 */
	protected void launchProjectChoiceScene() {
		setTransition(new FadeTransition(getMTApplication(), 1500));	//Set a fade transition between the two scenes
		//Save the current scene on the scene stack before changing
		ProjectChoiceScene projectChoiceScene = new ProjectChoiceScene(getMTApplication(), "Project Choice Scene", m_model, m_orbs, DO_CLEAN_GESTURES);
		//Add the scene to the mt application
		getMTApplication().addScene(projectChoiceScene);
		
		//Do the scene change
		getMTApplication().changeScene(projectChoiceScene);
		
		//Close the scene
		close();
	}

	
	@Override
	protected void addOrb(ControlOrb orb){
		super.addOrb(orb);
		m_orbsStrings.add(orb.getLogin());
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}
		

	protected void close() {
		for (TextEntryValidateKeyboard keyboard : m_keyboards){
			if (keyboard != null){
				keyboard.destroy();
			}
		}
		
		super.close();
	}
	
	
	@Override
	protected void processFileDownloaded(int id) {
		super.processFileDownloaded(id);	
	}
	
	
	//Members
	protected List<TextEntryValidateKeyboard> m_keyboards = new ArrayList<TextEntryValidateKeyboard>();
	protected List<Object> m_orbsStrings = new ArrayList<Object>();
	protected ListMenu m_listOfUsers = new ListMenu(getMTApplication(), 0, 0, 200, 5, m_orbsStrings);

}


