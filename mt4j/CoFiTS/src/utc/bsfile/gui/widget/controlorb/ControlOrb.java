package utc.bsfile.gui.widget.controlorb;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.keyboard.DefaultKeyboard;
import utc.bsfile.model.CofitsUser;
import utc.bsfile.util.PropertyManager;

public class ControlOrb extends MTEllipse {
	
	private static final float RADIUS = 50f;
	
	public ControlOrb(AbstractMTApplication mtApplication, Vector3D centerPoint, CofitsUser user) {
		this(mtApplication, centerPoint, RADIUS, RADIUS, user, null);
	}
	
	public ControlOrb(AbstractMTApplication mtApplication, Vector3D centerPoint, CofitsUser user, DefaultKeyboard keyboard) {
		this(mtApplication, centerPoint, RADIUS, RADIUS, user, keyboard);
	}
	
	public ControlOrb(AbstractMTApplication mtApplication, final Vector3D centerPoint, float radiusX, float radiusY, CofitsUser user, DefaultKeyboard keyboard) {
		super(mtApplication, centerPoint, radiusX, radiusY);
		m_app = mtApplication;
		m_keyboard = keyboard;
		m_user = user;
		
		//Create login text
		MTColor textColor = new MTColor(255, 255, 255); //white
		String textFontStr = PropertyManager.getInstance().getProperty(PropertyManager.MAIN_FONT);
		IFont textFont = FontManager.getInstance().createFont(mtApplication, textFontStr, 15, textColor);
		m_loginTextField = new MTTextArea(mtApplication, textFont);
		m_loginTextField.setPositionGlobal(new Vector3D(centerPoint.x - m_user.getLogin().length() * textFont.getOriginalFontSize() / 4, centerPoint.y));
		m_loginTextField.setNoFill(true);
		m_loginTextField.setNoStroke(true);
		m_loginTextField.setPickable(false);
		m_loginTextField.setText(m_user.getLogin());
		centerLoginString();
		
		//Set the main color
		m_color = MTColor.randomColor();
		while( m_color.getR() > 150 && m_color.getG() > 150 && m_color.getB() > 150 ) {
			m_color = MTColor.randomColor();
		}
		setFillColor(m_color);
		setStrokeColor(m_color.getCopy());
		setStrokeWeight(500);
		getStrokeColor().setAlpha(m_color.getAlpha() + 100);
		
		addChild(m_loginTextField);
		
		addOrientationListener(this);
		updateOrientation(centerPoint.x, centerPoint.y);
		
		setVisible(true);
	}

	// ORIENTATION -------------------------------------------------------
	
	private float angle = 0;
	
	public void updateOrientation(float x, float y) {
		if (PropertyManager.getInstance().getProperty(PropertyManager.DEVICE).equals("table")) {
			float width = getApplication().width;
			float height = getApplication().height;
			
			float leftDistance =  x;
			float rightDistance = width - x;
			float topDistance = y;
			float bottomDistance = height - y;
			
			if(leftDistance <= rightDistance && leftDistance <= topDistance && leftDistance <= bottomDistance) {
				setAngle(new Vector3D(x, y), 90);
			}
			else if(topDistance <= rightDistance && topDistance <= leftDistance && topDistance <= bottomDistance) {
				setAngle(new Vector3D(x, y), 180);
			}
			else if(rightDistance <= leftDistance && rightDistance <= topDistance && rightDistance <= bottomDistance) {
				setAngle(new Vector3D(x, y), 270);
			}
			else if(bottomDistance <= leftDistance && bottomDistance <= topDistance && bottomDistance <= rightDistance) {
				setAngle(new Vector3D(x, y), 0);
			}
		}
	}
	
	public void updateOrientation() {
		if (PropertyManager.getInstance().getProperty(PropertyManager.DEVICE).equals("table")) {
			float width = getApplication().width;
			float height = getApplication().height;
			
			float leftDistance =  getCenterPointGlobal().x;
			float rightDistance = width - getCenterPointGlobal().x;
			float topDistance = getCenterPointGlobal().y;
			float bottomDistance = height - getCenterPointGlobal().y;	
			
			if(leftDistance <= rightDistance && leftDistance <= topDistance && leftDistance <= bottomDistance) {
				setAngle(getCenterPointGlobal(), 90);
			}
			else if(topDistance <= rightDistance && topDistance <= leftDistance && topDistance <= bottomDistance) {
				setAngle(getCenterPointGlobal(), 180);
			}
			else if(rightDistance <= leftDistance && rightDistance <= topDistance && rightDistance <= bottomDistance) {
				setAngle(getCenterPointGlobal(), 270);
			}
			else if(bottomDistance <= leftDistance && bottomDistance <= topDistance && bottomDistance <= rightDistance) {
				setAngle(getCenterPointGlobal(), 0);
			}
		}
	}
	
	protected void setAngle(Vector3D centerPoint, float newAngle) {
		rotateZGlobal(centerPoint, -angle);
		rotateZGlobal(centerPoint, newAngle);
		angle = newAngle;
	}

	public void addOrientationListener(MTComponent component) {
		
		component.addGestureListener(DragProcessor.class, new IGestureEventListener() {
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			String device = PropertyManager.getInstance().getProperty(PropertyManager.DEVICE);
			if (device.equals("table")){
				
				 DragEvent th = (DragEvent) ge;
                 switch (th.getId()) {
                 case DragEvent.GESTURE_STARTED:
                               break;
                 case DragEvent.GESTURE_UPDATED:
		     					updateOrientation();
                               break;
                 case DragEvent.GESTURE_ENDED:
                               break;
                 default:
                               break;
                 }
			}
			return false;
		}
		});
	}
	
	//--------------------------------------------------------------------
	
	
	@Override
	public void setDefaultGestureActions() {
		super.setDefaultGestureActions();
		
		//Add Events Listening Process
		addGestureListener(DragProcessor.class, new InertiaDragAction());
	}
	
	public void centerLoginString() {
		m_loginTextField.setPositionRelativeToOther(this, new Vector3D(0, 0));
	}

	/**
	 * Method to use when the Orb is destroyed
	 */
	protected void closeOrb(){
		if (m_keyboard != null)
			m_keyboard.close();
	}


	
	
	//Getters & Setters
	public String getLogin() {return m_user.getLogin();}
	public DefaultKeyboard getKeyboard() {return this.m_keyboard;}
	public AbstractMTApplication getApplication() {return this.m_app;}
	
	public void setKeyboard(DefaultKeyboard keyboard){this.m_keyboard = keyboard;}
	public void setApplication(AbstractMTApplication app){this.m_app = app;}
	
	public void changeLogin(String text) {m_loginTextField.setText(text); m_user.setLogin(text);}
	public boolean isAdmin() {return m_user.isAdmin();}
	
	//Members
	private MTTextArea m_loginTextField;
	private MTColor m_color;
	private AbstractMTApplication m_app;
	private DefaultKeyboard m_keyboard;
	private CofitsUser m_user;

}
