package utc.bsfile.gui.widget.keyboard;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.bsfile.gui.Theme;
import utc.bsfile.util.PropertyManager;

/**
 * @author chris
 * A validate keyboard with a textfield listening to keyboard tapping.
 * The default constructors are defined to be used with LoginScene but this class can be used for other purpose
 *
 */
public class TextEntryValidateKeyboard extends ValidateKeyboard {

	public TextEntryValidateKeyboard(PApplet pApplet, String login) {
		super(pApplet, login);
		
		init(pApplet);
	}
	

	public TextEntryValidateKeyboard(PApplet pApplet, float x, float y, float z, float width, float height, float arcWidth, float arcHeight, String login) {
		super(pApplet, x, y, z, width, height, arcWidth, arcHeight, login);
		
		init(pApplet);
	}
	
	
	private void init(PApplet pApplet) {
		//Add a text area to enter a message
		String textFontStr = PropertyManager.getInstance().getProperty(PropertyManager.MAIN_FONT);
		IFont textFont = FontManager.getInstance().createFont(pApplet, textFontStr, 32, MTColor.BLACK);
		m_textEntry = new MTTextField(pApplet, 0, 0, 400, 40, textFont);
		
		m_textEntry.setPositionRelativeToOther(m_textEntry, new Vector3D(getWidthXY(TransformSpace.LOCAL) / 2, -30));
		m_textEntry.setPickable(false);
		m_textEntry.setVisible(true);
		m_textEntry.setText(getName());
		
		m_textEntry.setFillColor(Theme.ITEM_COLOR);
		m_textEntry.setStrokeColor(Theme.ITEM_BACKGROUND_COLOR);
		m_textEntry.setStrokeWeight(3);
		
		addChild(m_textEntry);
		addTextInputListener(m_textEntry);
		
	}
	
	//Getters & Setters
	public final String getText(){
		return m_textEntry.getText();
	}
	
	//Members
	MTTextField m_textEntry;
}
