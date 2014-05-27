package utc.bsfile.main;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
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
import utc.bsfile.model.menu.DefaultMenuModel;
import utc.bsfile.util.PropertyManager;

public class LoginScene extends CofitsDesignScene implements ValidateKBListener {

	public LoginScene(AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		
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
		}
		
		//Process the keyboard/orb relation
		if (orb == null) {
			
			//Create a new orb
			orb = playOrb(new Vector3D(), keyboard);
			orb.setPositionRelativeToOther(keyboard, new Vector3D(0,-10));
			
		} else {
			
			//Update the login in the orb
			orb.setLogin(keyboard.getText());
			
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
			orb.setKeyboard(keyboard);
			orb.sendToFront();
		} else {
			keyboard = new TextEntryValidateKeyboard(getMTApplication(), "");
		}
		
		keyboard.setControlOrb(orb);
		m_keyboards.add(keyboard);
		
		//Visibility and location
		keyboard.translate(locationOnScreen, TransformSpace.GLOBAL);
		keyboard.setVisible(true);
		
		//Listeners
		keyboard.addValidateKBListener(LoginScene.this);		
		
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
		final ControlOrb orb = new ControlOrb(getMTApplication(), orbLocation, keyboard.getText(), keyboard);
		
		if (keyboard != null){
			keyboard.setControlOrb(orb);
		}
				
		addOrb(orb);
		getCanvas().addChild(orb);
		
		//Add Events Listening Process
		orb.addGestureListener(DragProcessor.class, new InertiaDragAction());
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
		
		m_listOfUsers.setVisible(true);
		m_listOfUsers.setCloseVisible(false);
		
		//Add a title to the list Menu
		MTColor textColor = new MTColor(255, 255, 255); //white
		String textFontStr = PropertyManager.getInstance().getProperty(PropertyManager.MAIN_FONT);
		IFont textFont = FontManager.getInstance().createFont(getMTApplication(), textFontStr, 15, textColor);
		MTTextField title = new MTTextField(getMTApplication(), 0, 0, m_listOfUsers.getWidthXY(TransformSpace.LOCAL), 20, textFont);
		
		title.setNoStroke(true);
		title.setNoFill(true);
		title.setPickable(false);
		title.setText("List of users");
		
		m_listOfUsers.addChild(title);
		
		getCanvas().addChild(m_listOfUsers);
	}
	
	
	
	protected void addOrb(ControlOrb orb){
		m_orbs.add(orb);
		m_orbsStrings.add(orb.getLogin());
	}
	
	
	//Members
	List<ControlOrb> m_orbs = new ArrayList<ControlOrb>();
	List<TextEntryValidateKeyboard> m_keyboards = new ArrayList<TextEntryValidateKeyboard>();
	List<Object> m_orbsStrings = new ArrayList<Object>();
	ListMenu m_listOfUsers = new ListMenu(getMTApplication(), 0, 0, 200, 5, m_orbsStrings);

}


