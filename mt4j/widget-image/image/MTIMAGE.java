package utc.tatinpic.gui.widgets.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.util.animation.AnimationUtil;

import processing.core.PApplet;
import processing.core.PImage;

import utc.tatinpic.gui.LocationReference;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.model.image.IMAGEModel;

public class MTIMAGE extends MTRectangle {

	/**
	 * @uml.property name="image"
	 */
	private IMAGEModel image;

	/**
	 * @uml.property name="slave"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="utc.tatinpic.gui.widgets.pdf.MTPDF"
	 */
	private ArrayList<MTIMAGE> slave = new ArrayList<MTIMAGE>();

	/**
	 * Indicates if the current MTPDF is the master IMAGE viewer of the
	 * TatinIMAGE Component.
	 * 
	 * @uml.property name="isMaster"
	 */
	private boolean isMaster = true;

	/**
	 * @uml.property name="menu"
	 * @uml.associationEnd 
	 *                     inverse="mtimage:utc.tatinpic.gui.widgets.image.IMAGEMenu"
	 */
	private IMAGEMenu menu = null;
	/**
	 * @uml.property name="menuClone"
	 * @uml.associationEnd 
	 *                     inverse="mtimage:utc.tatinpic.gui.widgets.image.IMAGECloneMenu"
	 */
	private IMAGECloneMenu menuClone = null;

	private Workbench workbench;

	private List<Workbench> workbenchs;

	private MTTextArea fileName;

	public MTIMAGE(PApplet renderer, IMAGEModel image, boolean master,
			Workbench workbench, List<Workbench> workbenchs) {
		super(renderer, 0, 0);
		PImage texture = ((MTApplication) renderer).loadImage(image.getFile()
				.getAbsolutePath());
		
		//this.setAnchor(PositionAnchor.CENTER);
		while (texture.width > 2048 || texture.height > 2048) {
			int x = (int) ((float) (texture.width) * 0.7f);
			int y = (int) ((float) (texture.height) * 0.7f);
			texture.resize(x, y);
			System.out.println("###  XY = " + x + " " + y);
		}
		setTexture(texture);
		setSizeLocal(texture.width, texture.height);
		this.image = image;
		this.isMaster = master;
		this.workbench = workbench;
		this.workbenchs = workbenchs;
		setAnchor(PositionAnchor.UPPER_LEFT);
		setStrokeColor(workbench.getColor());
		setStrokeWeight(2f);
		setGestureClassic();
		addFileName();
	}

	public MTIMAGE(MTIMAGE mtimage) {
		this(mtimage.getRenderer(), mtimage.image, false, mtimage.workbench,
				mtimage.workbenchs);
	}

	public void cloneIMAGE() {
		MTIMAGE clone = new MTIMAGE(this);
		clone.scaleGlobal(.5f, .5f, .5f, getCenterPointGlobal());
		slave.add(clone);
		getParent().addChild(clone);
	}

	public void removeClone(MTIMAGE clone) {
		slave.remove(clone);
		AnimationUtil.bounceOut(clone, true);
	}

	public void setGestureClassic() {
		registerInputProcessor(new TapAndHoldProcessor(
				(AbstractMTApplication) getRenderer(), 1000));

		addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(
				(AbstractMTApplication) getRenderer(), this));
		if (isMaster) {
			setMasterGestureMenu();
		} else {
			setCloneGestureMenu();
		}
	}

	public void setMasterGestureMenu() {
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
								if (menu != null) {
									menu.destroy();
								}
								menu = new IMAGEMenu(getRenderer(),
										getParent(), 40, 120, MTIMAGE.this, th,
										workbench, workbenchs);
								LocationReference lr = LocationReference.getLocation(workbench.getRow());
								menu.rotateZGlobal(menu.getCenterPointGlobal(), lr.getDirection());
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
	}

	public void setCloneGestureMenu() {
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
								if (menuClone != null) {
									menuClone.destroy();
								}
								menuClone = new IMAGECloneMenu(getRenderer(),
										getParent(), MTIMAGE.this, th,
										workbench, workbenchs);
								LocationReference lr = LocationReference.getLocation(workbench.getRow());
								menuClone.rotateZGlobal(menuClone.getCenterPointGlobal(), lr.getDirection());
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
		if (!slave.isEmpty()) {
			for (int i = 0; i < slave.size(); i++) {
				slave.get(i).remove();
			}
			slave = new ArrayList<MTIMAGE>();
		}
	}
}
