package utc.bsfile.gui.widget.keyboard;

import processing.core.PApplet;
import utc.bsfile.gui.widget.keyboard.DefaultKeyboard;

public class BrowserKeyboard extends DefaultKeyboard{

	public BrowserKeyboard(PApplet pApplet) {
		super(pApplet);
	}
	
	protected void onCloseButtonClicked()
	{
		this.setVisible(false);
	}

}
