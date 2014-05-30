package utc.bsfile.gui.widget.menu;

import utc.bsfile.gui.widget.menu.ListMenu.ChoiceEvent;

public interface ChoiceListener {
	public void choiceSelected(ChoiceEvent choiceEvent);

	public void choiceCancelled(ChoiceEvent choiceEvent);
}