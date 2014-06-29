package utc.bsfile.gui.widget.image;

import java.io.File;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.util.animation.AnimationUtil;

import processing.core.PApplet;
import processing.core.PImage;
import utc.bsfile.model.image.IMAGEModel;
import utc.bsfile.util.PropertyManager;

public class MTIMAGE extends MTRectangle {

	/**
	 * @uml.property name="image"
	 */
	private IMAGEModel image;

	private MTTextArea fileName;
	
	private PApplet pApplet;

	public MTIMAGE(PApplet renderer, IMAGEModel image) {
		super(renderer, 0, 0);
		pApplet = renderer;
		PImage texture = ((MTApplication) renderer).loadImage(image.getFile()
				.getAbsolutePath());

		// this.setAnchor(PositionAnchor.CENTER);
		while (texture.width > 2048 || texture.height > 2048) {
			int x = (int) ((float) (texture.width) * 0.7f);
			int y = (int) ((float) (texture.height) * 0.7f);
			texture.resize(x, y);
			System.out.println("###  XY = " + x + " " + y);
		}
		setTexture(texture);
		setSizeLocal(texture.width, texture.height);
		this.image = image;
		setAnchor(PositionAnchor.UPPER_LEFT);
		setStrokeWeight(2f);
		setGestureClassic();
		addFileName();
		addOrientationListener(this);
	}
	
	float angle = 0;
	
	public void updateOrientation(float x, float y) {
		float width = pApplet.width;
		float height = pApplet.height;
		
		float leftDistance =  x;
		float rightDistance = width - x;
		float topDistance = y;
		float bottomDistance = height - y;	
		
		if(leftDistance <= rightDistance && leftDistance <= topDistance && leftDistance <= bottomDistance) {
			setAngle(new Vector3D(x, y), 90);
		}
		else if(topDistance <= rightDistance && topDistance <= leftDistance && topDistance <= bottomDistance) {
			setAngle(new Vector3D(x, y), 180);
		}
		else if(rightDistance <= leftDistance && rightDistance <= topDistance && rightDistance <= bottomDistance) {
			setAngle(new Vector3D(x, y), 270);
		}
		else if(bottomDistance <= leftDistance && bottomDistance <= topDistance && bottomDistance <= rightDistance) {
			setAngle(new Vector3D(x, y), 0);
		}
	}
	
	public void updateOrientation() {
		float width = pApplet.width;
		float height = pApplet.height;
		
		float leftDistance =  getCenterPointGlobal().x;
		float rightDistance = width - getCenterPointGlobal().x;
		float topDistance = getCenterPointGlobal().y;
		float bottomDistance = height - getCenterPointGlobal().y;	
		
		if(leftDistance <= rightDistance && leftDistance <= topDistance && leftDistance <= bottomDistance) {
			setAngle(getCenterPointGlobal(), 90);
		}
		else if(topDistance <= rightDistance && topDistance <= leftDistance && topDistance <= bottomDistance) {
			setAngle(getCenterPointGlobal(), 180);
		}
		else if(rightDistance <= leftDistance && rightDistance <= topDistance && rightDistance <= bottomDistance) {
			setAngle(getCenterPointGlobal(), 270);
		}
		else if(bottomDistance <= leftDistance && bottomDistance <= topDistance && bottomDistance <= rightDistance) {
			setAngle(getCenterPointGlobal(), 0);
		}
	}
	
	protected void setAngle(Vector3D centerPoint, float newAngle) {
		rotateZGlobal(centerPoint, -angle);
		rotateZGlobal(centerPoint, newAngle);
		angle = newAngle;
	}

	public void addOrientationListener(MTComponent component) {
		
		component.addGestureListener(DragProcessor.class, new IGestureEventListener() {
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			String device = PropertyManager.getInstance().getProperty(PropertyManager.DEVICE);
			if (device.equals("table")){
				
				 DragEvent th = (DragEvent) ge;
                 switch (th.getId()) {
                 case DragEvent.GESTURE_STARTED:
                               break;
                 case DragEvent.GESTURE_UPDATED:
                	 updateOrientation(th.getDragCursor().getPosition().x, th.getDragCursor().getPosition().y);
                               break;
                 case DragEvent.GESTURE_ENDED:
                               break;
                 default:
                               break;
                 }
			}
			return false;
		}
		});
	}
	
	//--------------------------------------------------------------------

	public void setGestureClassic() {
		registerInputProcessor(new TapAndHoldProcessor(
				(AbstractMTApplication) getRenderer(), 1000));

		addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(
				(AbstractMTApplication) getRenderer(), this));
		setGestureMenu();
	}

	public void setGestureMenu() {
		addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent th = (TapAndHoldEvent) ge;
						switch (th.getId()) {
						case TapAndHoldEvent.GESTURE_STARTED:
							break;
						case TapAndHoldEvent.GESTURE_UPDATED:
							break;
						case TapAndHoldEvent.GESTURE_ENDED:
							if (th.isHoldComplete()) {
								remove();
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
	}

	private void addFileName() {
		fileName = new MTTextArea(getRenderer(), FontManager.getInstance()
				.createFont(getRenderer(), "calibri", 16, MTColor.WHITE, true));
		fileName.setAnchor(PositionAnchor.CENTER);
		fileName.removeAllGestureEventListeners();
		fileName.setText(image.getFile().getName());
		fileName.setFillColor(new MTColor(128, 128, 128, 50));
		fileName.setStrokeColor(new MTColor(100, 100, 100, 75));
		addChild(fileName);
		fileName.setPositionRelativeToParent(new Vector3D(
				getWidthXY(TransformSpace.LOCAL) / 2f,
				getHeightXY(TransformSpace.LOCAL)
						+ (fileName.getHeightXY(TransformSpace.LOCAL) / 2f)));
		delegateProcessor(fileName, this, DragProcessor.class);
	}

	private void delegateProcessor(MTComponent from, final MTComponent to,
			Class<? extends IInputProcessor> processor) {
		from.addGestureListener(processor, new IGestureEventListener() {

			@Override
			public boolean processGestureEvent(MTGestureEvent gestureEvent) {
				gestureEvent.setTarget(to);
				return to.processGestureEvent(gestureEvent);
			}
		});
	}

	public File getFile() {
		return image.getFile();
	}

	public void remove() {
		AnimationUtil.bounceOut(this, true);
	}
}
