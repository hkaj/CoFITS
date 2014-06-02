package utc.bsfile.gui.widget.keyboard;

import java.util.HashSet;

import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.bsfile.gui.Theme;
import utc.bsfile.util.ImageManager;

public class ValidateKeyboard extends AttachmentKeyboard {
	public static String NAME = "Validate mt-keyboard";
	public static int choiceViewHeight = 40;
	private  int iconWidth = 40;
	private  int iconHeight = 40;
	private  int spacing = 10;
	private int validateButtonWidth = 70;
	//private MTImageButton validateButton;
	private MTSvgButton validateButton;
	private HashSet<ValidateKBListener> vlisteners;
	
	public ValidateKeyboard(PApplet pApplet, String login) {
		this(pApplet, 0, 0, 0, 700, 245, 30, 30,login);
	}
	
	public ValidateKeyboard(PApplet pApplet,float x, float y, float z, float width, float height, float arcWidth, float arcHeight, String login) {
		super(pApplet, x, y, z, width, height, arcWidth, arcHeight);
		this.setName(login);
		vlisteners = new HashSet<ValidateKBListener>();
		
		// new validate button (SVG format)
		Vector3D validateButtonPosition = new Vector3D(x + width - (validateButtonWidth/2 + spacing*2), y + height - ( validateButtonWidth/2 + 2 * spacing));
		this.validateButton = new MTSvgButton(pApplet, MT4jSettings.getInstance().getDefaultSVGPath() + "KeybValidate-green.svg");
		this.validateButton.setPositionGlobal(validateButtonPosition);
		this.validateButton.setSizeXYGlobal(validateButtonWidth, validateButtonWidth);
		this.validateButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				if (ge instanceof TapEvent)
				{
					TapEvent tapEvent = (TapEvent) ge;

					if (tapEvent.isTapped() && tapEvent.getTarget() == validateButton) {
						ValidateKBEvent evt = new ValidateKBEvent(ValidateKeyboard.this);
						for (ValidateKBListener listener : vlisteners)
						  listener.validate(evt);
					}
				}

				return false;
			}
		});
//		this.validateButton = createIconButton(validateButtonPosition, "checked-icon.png", new IGestureEventListener() {
//			public boolean processGestureEvent(MTGestureEvent ge)
//			{
//				if (ge instanceof TapEvent)
//				{
//					TapEvent tapEvent = (TapEvent) ge;
//
//					if (tapEvent.isTapped() && tapEvent.getTarget() == validateButton) {
//						ValidateKBEvent evt = new ValidateKBEvent(ValidateKeyboard.this);
//						for (ValidateKBListener listener : vlisteners)
//						  listener.validate(evt);
//					}
//				}
//
//				return false;
//			}
//		});
		
		//this.validateButton.setNoStroke(true);
		addChild(this.validateButton);
		setVisible(false);
	}
	
	protected MTImageButton createIconButton(Vector3D position, String imageFilename, IGestureEventListener listener)
	{
		MTImageButton imageButton = new MTImageButton(this.getRenderer(), ImageManager.getInstance().load(imageFilename));
		imageButton.setAnchor(PositionAnchor.UPPER_LEFT);
		imageButton.setWidthXYGlobal(iconWidth);
		imageButton.setHeightXYGlobal(choiceViewHeight);
		imageButton.setPositionGlobal(new Vector3D(position.x, position.y));
		Theme.getTheme().applyStyle("TRANSPARENT_IMAGE_BUTTON", imageButton);
		imageButton.addGestureListener(TapProcessor.class, listener);

		return imageButton;
	}
	
	
	public void addValidateKBListener (ValidateKBListener listener) {
		vlisteners.add(listener);
	}
	
	
	
	
	public class ValidateKBEvent	{
		/**
		 * @uml.property  name="listMenu"
		 * @uml.associationEnd  
		 */
		private ValidateKeyboard vKB;
		
		public ValidateKBEvent(ValidateKeyboard vKB) {
			this.vKB = vKB;
		}

		public ValidateKeyboard getValidateKB () {
			return vKB;
		}
	}
	
	
	public interface ValidateKBListener {
		public void validate(ValidateKBEvent evt);
	}	
}
