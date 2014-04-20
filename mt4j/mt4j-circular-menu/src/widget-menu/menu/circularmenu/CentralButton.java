package utc.tatinpic.gui.widgets.menu.circularmenu;

import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.tatinpic.util.manager.PropertyManager;

public class CentralButton extends MTEllipse {
	private WorkbenchHandler menu;
	private boolean dragProceeding = false; 
	private MTTextArea mtTextArea;
	
	public CentralButton(WorkbenchHandler wbh, Vector3D centerPointLocal, float f, String title)
	{
		super(wbh.getPApplet(), centerPointLocal, f, f);
		this.menu = wbh;
		this.unregisterAllInputProcessors();
		this.setName("central button");
		this.setFillColor(new MTColor(245, 215, 10)); // Color is RED
		this.setNoStroke(false);

		// Configure the text area
		mtTextArea = new MTTextArea(menu.getPApplet());
		mtTextArea.setText(title);
		PropertyManager pm = PropertyManager.getInstance();
		IFont font = FontManager.getInstance().createFont(null,
				pm.getProperty(PropertyManager.CIRCULAR_MENU_FONT), 
				pm.getIntProperty(PropertyManager.CIRCULAR_MENU_SIZE), // Font size
		        new MTColor(0, 0, 0, 255), // Font fill color
		        true);
		mtTextArea.setFont(font);
		mtTextArea.setPositionGlobal(new Vector3D(this.getCenterPointLocal().x, this.getCenterPointLocal().y));
		mtTextArea.setNoFill(true);
		mtTextArea.setNoStroke(true);
		mtTextArea.removeAllGestureEventListeners();

		this.addChild(mtTextArea);
		// Add default inputEventListener
		final IGestureEventListener tapListener = new IGestureEventListener()
		{
			@Override
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				if (ge instanceof TapEvent)
				{
					switch (ge.getId())
					{
					case TapEvent.GESTURE_ENDED:
						if (!dragProceeding)
						{ // if there's no drag and drop in the same time
							boolean hasSegments = false;
							menu.toogleSegmentsVisible();		 							
						}
						break;
					}
				}
				return false;
			}
		};
		
		this.registerInputProcessor(new TapProcessor(menu.getPApplet()));
		this.addGestureListener(TapProcessor.class, tapListener);
		mtTextArea.registerInputProcessor(new TapProcessor(menu.getPApplet()));
		mtTextArea.addGestureListener(TapProcessor.class, tapListener);

		final IGestureEventListener gestureListener = new IGestureEventListener()
		{
			private Vector3D initPos;
			@Override
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				IMTComponent3D eventTarget = ge.getTarget();
				if (eventTarget == CentralButton.this || eventTarget == mtTextArea)
				{
					if (ge instanceof DragEvent)
					{
						DragEvent de = (DragEvent) ge;
						switch (de.getId())
						{
						case DragEvent.GESTURE_STARTED:
							initPos = menu.getCenterPointGlobal();
							break;
						case DragEvent.GESTURE_UPDATED:
							dragProceeding = true;
							if (acceptablePosition(de.getTranslationVect()))
								menu.translateGlobal(de.getTranslationVect());
							break;
						case DragEvent.GESTURE_ENDED:
							dragProceeding = false;
							break;
						default:
							break;
						}
					}
				}
				return false;
			}
		};

		this.removeAllGestureEventListeners(DragProcessor.class);
		this.registerInputProcessor(new DragProcessor(menu.getPApplet()));
		this.addGestureListener(DragProcessor.class, gestureListener);
		mtTextArea.removeAllGestureEventListeners(DragProcessor.class);
		mtTextArea.addGestureListener(DragProcessor.class, gestureListener);
	}
private boolean acceptablePosition(Vector3D vtrans) {
	float width = (float) menu.getPApplet().getSize().getWidth();
	float height = (float) menu.getPApplet().getSize().getHeight();
	Vector3D initPos = menu.getCenterPointGlobal();
	boolean hpos = initPos.x + vtrans.x < width && initPos.x + vtrans.x > 0;
	boolean vpos = initPos.y + vtrans.y < height && initPos.y + vtrans.y > 0;
	return hpos && vpos;
}
}
