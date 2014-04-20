package utc.tatinpic.gui.widgets.browser;

import processing.core.PApplet;
import utc.tatinpic.gui.widgets.keyboard.DefaultKeyboard;

public class BrowserKeyboard extends DefaultKeyboard{

	public BrowserKeyboard(PApplet pApplet) {
		super(pApplet);
	}
	
	protected void onCloseButtonClicked()
	{
		this.setVisible(false);
	}

}
