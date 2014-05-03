package utc.tatinpic.gui.widgets.browser;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import utc.tatinpic.gui.Theme;
import utc.tatinpic.gui.Theme.StyleID;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceEvent;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceListener;
import utc.tatinpic.util.manager.PropertyManager;

public class BookmarkSelecter extends MTRoundRectangle implements ChoiceListener{

	private MTList list;

	BookmarkSelecter(AbstractMTApplication mtApp,
			int width, int height, MTColor color) {
		super(mtApp, 200, 150, 0, width + 50, height + 100, 25, 25);
		this.setFillColor(color);
		
		
		MTComponent clippedChildContainer = new MTComponent(mtApp);
		MTRoundRectangle clipShape = new MTRoundRectangle(mtApp, 225, 250, 0,
				width, height, 1, 1);
		clipShape.setNoStroke(true);
		clippedChildContainer.setChildClip(new Clip(clipShape));

		this.addChild(clippedChildContainer);
		
		/*
		 * BOUTON GO BACK
		 */
		MTImageButton leftButton = new MTImageButton(mtApp,
				mtApp.loadImage(PropertyManager.getInstance().getProperty(
						PropertyManager.IMAGE_DIR)
						+ "arrow_left_32x32.png"));
		leftButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							destroy();
						}
						return false;
					}
				});
		leftButton.setNoStroke(true);
		this.addChild(leftButton);
		leftButton.translate(new Vector3D(225, 200, 0));
		
		
		list = new MTList(mtApp, 10, 10 , width, height);
		Theme.getTheme().applyStyle(StyleID.STROKE_ONLY, list);

		this.addChild(list);
	}

	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		System.out.println("choix :"+choiceEvent.getChoice());
	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
		// TODO Auto-generated method stub
		
	}
}
