package utc.tatinpic.gui.widgets.menu;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.tatinpic.gui.Theme;
import utc.tatinpic.gui.Theme.StyleID;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceEvent;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceListener;
import utc.tatinpic.util.manager.ImageManager;

public class OptionList extends MTRectangle {
	private static int spacing = 5;
	public static int choiceViewHeight = 40;
	public static int iconWidth = 40;
	public static int iconHeight = 40;
	
	private MTList list;
	private boolean mustBeDestroy;
	private MTImageButton closeButton;
	
	public OptionList(PApplet pApplet, int x, int y, float width, int nbElements) {
		super(pApplet, width, calcHeight(nbElements));
		float height = calcHeight(nbElements);
		Theme.getTheme().applyStyle(StyleID.DEFAULT, this);
		this.setAnchor(PositionAnchor.UPPER_LEFT);
         mustBeDestroy = true;

		Vector3D closeButtonPosition = new Vector3D(x + width - (iconWidth + spacing), y + spacing);
		closeButton = createIconButton(closeButtonPosition, "IMG:/close.png", new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				if (ge instanceof TapEvent)
				{
					TapEvent tapEvent = (TapEvent) ge;

					if (tapEvent.isTapped() && tapEvent.getTarget() == closeButton) {
						updateList();
						setVisible(false);
					}
				}

				return false;
			}
		});
		this.closeButton.setNoStroke(true);
		addChild(this.closeButton);
		this.list = new MTList(pApplet, x + spacing, y + getSpacedIconHeight(), width - spacing * 2, height - getSpacedIconHeight() * 2, 0);
		Theme.getTheme().applyStyle(StyleID.STROKE_ONLY, list);
		updateList();
		addChild(this.list);
	}

	private static float calcHeight (int visibleChoiceCount) {
		return (float)((visibleChoiceCount * choiceViewHeight) + (getSpacedIconHeight() * 2));
	}
	private static int getSpacedIconHeight () {
		return iconHeight + spacing * 2;
	}
	protected void updateList () {
		list.removeAllListElements();
	}
	public void addListElement(String element) {
		MTListCell cell = new MTListCell(getRenderer(), getWidthXYGlobal() - spacing * 2, choiceViewHeight);
		Theme.getTheme().applyStyle("LIST_CHOICE", cell);
		cell.addChild(makeChoiceView(element));
		list.addListElement(cell);
	}
	protected MTImageButton createIconButton(Vector3D position, String imageFilename, IGestureEventListener listener)
	{
		MTImageButton imageButton = new MTImageButton(this.getRenderer(), ImageManager.getInstance().get(imageFilename));
		imageButton.setAnchor(PositionAnchor.UPPER_LEFT);
		imageButton.setWidthXYGlobal(iconWidth);
		imageButton.setHeightXYGlobal(choiceViewHeight);
		imageButton.setPositionGlobal(new Vector3D(position.x, position.y));
		Theme.getTheme().applyStyle(Theme.TRANSPARENT_IMAGE_BUTTON, imageButton);
		imageButton.addGestureListener(TapProcessor.class, listener);

		return imageButton;
	}
	protected MTComponent makeChoiceView(Object choice) {
		MTTextArea textArea = new MTTextArea(getRenderer(), spacing, 0, getWidthXYGlobal() - spacing * 2, choiceViewHeight);
		textArea.setFont(FontManager.getInstance().createFont(getRenderer(), "calibri", 20, MTColor.BLACK, true));
		textArea.setText(choice.toString());
		textArea.setNoFill(true);
		textArea.setNoStroke(true);
		
		return textArea;
	}
	public void setCloseVisible (boolean visible) {
		this.closeButton.setVisible(visible);
		this.closeButton.setEnabled(visible);
	}
}
