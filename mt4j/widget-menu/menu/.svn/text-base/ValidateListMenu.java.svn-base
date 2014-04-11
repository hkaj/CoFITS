package utc.tatinpic.gui.widgets.menu;

import java.util.Collection;
import java.util.HashSet;

import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.tatinpic.model.menu.DefaultMenuModel;
import utc.tatinpic.model.menu.IMenuModel;

public class ValidateListMenu extends ListMenu {

	private MTImageButton validateButton;
	private HashSet<ValidateListener> vlisteners;
	public ValidateListMenu(PApplet applet, int x, int y, float width,
			int nbItem, IMenuModel model) {
		super(applet, x, y, width, nbItem, model);
		vlisteners = new HashSet<ValidateListener>();
		Vector3D validateButtonPosition = new Vector3D(x + width - 2 * (iconWidth + getSpacing()), y + getSpacing());
		this.validateButton = createIconButton(validateButtonPosition, "IMG:/start-flag-icon.png", new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				if (ge instanceof TapEvent)
				{
					TapEvent tapEvent = (TapEvent) ge;

					if (tapEvent.isTapped() && tapEvent.getTarget() == validateButton) {
						ValidateEvent evt = new ValidateEvent(ValidateListMenu.this);
						for (ValidateListener listener : vlisteners)
							listener.validate(evt);
					}
				}

				return false;
			}
		});
		this.validateButton.setNoStroke(true);
		addChild(this.validateButton);
	}

	public ValidateListMenu(PApplet applet, int x, int y, float width,
			int nbItem, Object... choices) {
		this(applet, x, y, width, nbItem, new DefaultMenuModel(null, choices));
	}
public ValidateListMenu(PApplet applet, int x, int y, float width,
			int nbItem, Collection<Object> choices) {
	this(applet, x, y, width, nbItem, choices.toArray());
	}

public void addValidateListener (ValidateListener listener) {
	vlisteners.add(listener);
}
public class ValidateEvent
{
	/**
	 * @uml.property  name="listMenu"
	 * @uml.associationEnd  
	 */
	private ValidateListMenu vlistMenu;
	
	public ValidateEvent(ValidateListMenu menu) {
		this.vlistMenu = menu;
	}

	/**
	 * @return
	 * @uml.property  name="listMenu"
	 */
	public ValidateListMenu getValidateListMenu () {
		return vlistMenu;
	}

}

public interface ValidateListener {
	public void validate(ValidateEvent evt);
}
}
