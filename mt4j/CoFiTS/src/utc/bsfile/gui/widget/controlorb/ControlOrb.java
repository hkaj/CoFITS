package utc.bsfile.gui.widget.controlorb;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.util.PropertyManager;

public class ControlOrb extends MTEllipse {
	
	private static final float RADIUS = 50f;

	public ControlOrb(AbstractMTApplication mtApplication, Vector3D centerPoint, String login) {
		this(mtApplication, centerPoint, RADIUS, RADIUS, login);
	}
	
	public ControlOrb(AbstractMTApplication mtApplication, Vector3D centerPoint, float radiusX, float radiusY, String login) {
		super(mtApplication, centerPoint, radiusX, radiusY);
		m_app = mtApplication;
		
		//Create login text
		MTColor textColor = new MTColor(255, 255, 255); //white
		String textFontStr = PropertyManager.getInstance().getProperty(PropertyManager.MAIN_FONT);
		IFont textFont = FontManager.getInstance().createFont(mtApplication, textFontStr, 15, textColor);
		m_loginTextField = new MTTextArea(mtApplication, textFont);
		m_loginTextField.setPositionGlobal(new Vector3D(centerPoint.x - login.length() * textFont.getOriginalFontSize() / 4, centerPoint.y));
		m_loginTextField.setNoFill(true);
		m_loginTextField.setNoStroke(true);
		m_loginTextField.setPickable(false);
		m_loginTextField.setText(login);
		
		//Set the main color
		m_color = MTColor.randomColor();
		setFillColor(m_color);
		setStrokeColor(m_color.getCopy());
		setStrokeWeight(500);
		getStrokeColor().setAlpha(m_color.getAlpha() + 100);
		
		addChild(m_loginTextField);
		
		setVisible(true);
		
		eventsListeningProcess();
		
	}

	protected void eventsListeningProcess() {
		//Tap long on the orb
		registerInputProcessor(new TapAndHoldProcessor(m_app,1000));
		addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(m_app, this));
		addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent th = (TapAndHoldEvent)ge;
				switch (th.getId()) {
				case TapAndHoldEvent.GESTURE_STARTED:
					break;
				case TapAndHoldEvent.GESTURE_UPDATED:
					break;
				case TapAndHoldEvent.GESTURE_ENDED:
					if (th.isHoldComplete()){
						
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}
	
	//Getters & Setters
	public String getLogin() {
		return m_loginTextField.getText();
	}
	
	
	public void setLogin(String login) {
		this.m_loginTextField.setText(login);
	}
	
	
	//Members
	private MTTextArea m_loginTextField;
	private MTColor m_color;
	private AbstractMTApplication m_app;

}
