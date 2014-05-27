package utc.bsfile.gui.widget.keyboard;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.bsfile.util.PropertyManager;

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
		IFont textFont = FontManager.getInstance().createFont(pApplet, textFontStr, 15, MTColor.BLACK);
		MTTextField textEntry = new MTTextField(pApplet, 0, 0, 400, 30, textFont);
		
		textEntry.setPositionRelativeToOther(textEntry, new Vector3D(getWidthXY(TransformSpace.LOCAL) / 2, -5));
		textEntry.setPickable(false);
		textEntry.setVisible(true);
		
		addChild(textEntry);
		addTextInputListener(textEntry);
		
	}
	
	//Members
	MTTextField m_textEntry;
}
