package utc.bsfile.gui.widget.controlorb;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.keyboard.DefaultKeyboard;
import utc.bsfile.util.PropertyManager;

public class ControlOrb extends MTEllipse implements ITextInputListener {
	
	private static final float RADIUS = 50f;

	public ControlOrb(AbstractMTApplication mtApplication, Vector3D centerPoint, String login) {
		this(mtApplication, centerPoint, RADIUS, RADIUS, login, null);
	}
	
	public ControlOrb(AbstractMTApplication mtApplication, Vector3D centerPoint, String login, DefaultKeyboard keyboard) {
		this(mtApplication, centerPoint, RADIUS, RADIUS, login, keyboard);
	}
	
	public ControlOrb(AbstractMTApplication mtApplication, Vector3D centerPoint, float radiusX, float radiusY, String login, DefaultKeyboard keyboard) {
		super(mtApplication, centerPoint, radiusX, radiusY);
		m_app = mtApplication;
		m_keyboard = keyboard;
		
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
	}
	
	
	///////////////////////////////////////////////////////////////
	//	Implement InputTextListener method, listening to keyboard
	///////////////////////////////////////////////////////////////
	
	@Override
	public void appendCharByUnicode(String arg0) {
		m_loginTextField.appendCharByUnicode(arg0);		
	}

	@Override
	public void appendText(String arg0) {
		m_loginTextField.appendText(arg0);		
	}

	@Override
	public void clear() {
		m_loginTextField.clear();		
	}

	@Override
	public void removeLastCharacter() {
		m_loginTextField.removeLastCharacter();
	}

	@Override
	public void setText(String arg0) {
		m_loginTextField.setText(arg0);		
	}
	
	
	
	
	/**
	 * Method to use when the Orb is destroyed
	 */
	protected void closeOrb(){
		if (m_keyboard != null)
			m_keyboard.close();
	}

	
	
	//Getters & Setters
	public String getLogin() {return m_loginTextField.getText();}
	public DefaultKeyboard getKeyboard() {return this.m_keyboard;}
	public AbstractMTApplication getApplication() {return this.m_app;}
	
	public void setLogin(String login) {this.m_loginTextField.setText(login);}
	public void setKeyboard(DefaultKeyboard keyboard){this.m_keyboard = keyboard;}
	
		
	
	//Members
	private MTTextArea m_loginTextField;
	private MTColor m_color;
	private AbstractMTApplication m_app;
	private DefaultKeyboard m_keyboard;

}
