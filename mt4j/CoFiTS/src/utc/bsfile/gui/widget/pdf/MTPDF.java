package utc.bsfile.gui.widget.pdf;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent.FlickDirection;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.util.animation.AnimationUtil;

import processing.core.PApplet;
import processing.core.PImage;
import utc.bsfile.gui.widget.pdf.PDFListener;
import utc.bsfile.model.pdf.PDFModel;

public class MTPDF extends MTRectangle implements PDFListener {
	private static int LARGE_MOVE = 5;
	/**
	 * @uml.property name="pApplet"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private PApplet pApplet;
	/**
	 * @uml.property name="sizeLimitX"
	 */
	private int sizeLimitX = 1280;
	/**
	 * @uml.property name="autoUpdate"
	 */
	private boolean autoUpdate = true;

	/**
	 * @uml.property name="pdf"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private PDFModel pdf;

	/**
	 * @uml.property name="gestureArea"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MTRectangle gestureArea;
	/**
	 * @uml.property name="pageNumbers"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MTTextArea pageNumbers;

	/**
	 * @uml.property name="outline"
	 * @uml.associationEnd
	 */
	private PDFOutline outline = null;

	/**
	 * @uml.property name="menu"
	 * @uml.associationEnd inverse="mtpdf:utc.tatinpic.gui.widgets.pdf.PDFMenu"
	 */
	private PDFMenu menu = null;

	/**
	 * @uml.property name="menuClone"
	 * @uml.associationEnd 
	 *                     inverse="mtpdf:utc.tatinpic.gui.widgets.pdf.PDFCloneMenu"
	 */

	public MTPDF(PApplet pApplet, PDFModel pdfFile) {
		super(pApplet, 0, 0, 0, 0, 0);
		this.pApplet = pApplet;
		this.pdf = pdfFile;
		this.setAnchor(PositionAnchor.LOWER_LEFT);
		setStrokeColor(MTColor.BLACK);

		PImage img = null;

		if (this.pdf.isPdfValid()) {
			img = new PImage(pdf.getImagePage(1d));

			this.setWidthLocal(img.width);
			this.setHeightLocal(img.height);
			this.setTexture(img);
			setGestureClassic();
			createGestureArea();
		}

	}

	public void createGestureArea() {
		NavigationFlickController ctrlFlick = new NavigationFlickController();
		gestureArea = new MTRectangle(pApplet, 0, 0);
		gestureArea.setAnchor(PositionAnchor.UPPER_LEFT);
		gestureArea.setFillColor(new MTColor(128, 128, 128, 50));
		gestureArea.setStrokeColor(new MTColor(100, 100, 100, 75));
		gestureArea.setVisible(true);
		gestureArea.removeAllGestureEventListeners();
		addChild(gestureArea);

		Vector3D pos = this.getPosition(TransformSpace.LOCAL);
		System.out.println(gestureArea.getWidthXY(TransformSpace.LOCAL) + " "
				+ gestureArea.getHeightXY(TransformSpace.LOCAL));
		gestureArea.setSizeLocal(Math.round(this.getWidthXYGlobal()), 60);
		gestureArea.setPositionRelativeToParent(pos);

		pageNumbers = new MTTextArea(pApplet);
		pageNumbers.setText(pdf.getPageNumber() + " / "
				+ pdf.getNumberOfPages());
		gestureArea.setAnchor(PositionAnchor.CENTER);

		gestureArea.addChild(pageNumbers);
		Vector3D position = gestureArea.getPosition(TransformSpace.LOCAL);
		pageNumbers.setPositionRelativeToParent(position);
		pageNumbers.removeAllGestureEventListeners();
		pageNumbers.registerInputProcessor(new FlickProcessor(300, 5));
		pageNumbers.addGestureListener(FlickProcessor.class, ctrlFlick);

		MTTextArea next = new MTTextArea(pApplet);
		next.setText(">");
		next.removeAllGestureEventListeners();
		gestureArea.addChild(next);

		position.setX(position.getX()
				+ (Math.round(this.getWidthXYGlobal() / 5)));
		next.setPositionRelativeToParent(position);
		next.registerInputProcessor(new TapProcessor(pApplet, 25, true, 350));
		next.addGestureListener(TapProcessor.class,
				new NavigationTapController(NavigationTapController.NEXT_PAGE));
		next.registerInputProcessor(new FlickProcessor(300, 5));
		next.addGestureListener(FlickProcessor.class, ctrlFlick);

		MTTextArea doubleNext = new MTTextArea(pApplet);
		doubleNext.setText(">>");
		doubleNext.removeAllGestureEventListeners();
		gestureArea.addChild(doubleNext);

		position.setX(position.getX()
				+ (Math.round(this.getWidthXYGlobal() / 5)));
		doubleNext.setPositionRelativeToParent(position);
		doubleNext.registerInputProcessor(new TapProcessor(pApplet, 25, true,
				350));
		doubleNext.addGestureListener(TapProcessor.class,
				new NavigationTapController(
						NavigationTapController.DOUBLE_NEXT_PAGE));
		doubleNext.registerInputProcessor(new FlickProcessor(300, 5));
		doubleNext.addGestureListener(FlickProcessor.class, ctrlFlick);

		position = gestureArea.getPosition(TransformSpace.LOCAL);

		MTTextArea previous = new MTTextArea(pApplet);
		previous.setText("<");
		previous.removeAllGestureEventListeners();
		gestureArea.addChild(previous);

		position.setX(position.getX()
				- (Math.round(this.getWidthXYGlobal() / 5)));
		previous.setPositionRelativeToParent(position);
		previous.registerInputProcessor(new TapProcessor(pApplet, 25, true, 350));
		previous.addGestureListener(TapProcessor.class,
				new NavigationTapController(
						NavigationTapController.PREVIOUS_PAGE));
		previous.registerInputProcessor(new FlickProcessor(300, 5));
		previous.addGestureListener(FlickProcessor.class, ctrlFlick);

		MTTextArea doubleprevious = new MTTextArea(pApplet);
		doubleprevious.setText("<<");
		doubleprevious.removeAllGestureEventListeners();
		gestureArea.addChild(doubleprevious);

		position.setX(position.getX()
				- (Math.round(this.getWidthXYGlobal() / 5)));
		doubleprevious.setPositionRelativeToParent(position);
		doubleprevious.registerInputProcessor(new TapProcessor(pApplet, 25,
				true, 350));
		doubleprevious.addGestureListener(TapProcessor.class,
				new NavigationTapController(
						NavigationTapController.DOUBLE_PREVIOUS_PAGE));
		doubleprevious.registerInputProcessor(new FlickProcessor(300, 5));
		doubleprevious.addGestureListener(FlickProcessor.class, ctrlFlick);

		gestureArea.registerInputProcessor(new FlickProcessor(300, 5));
		gestureArea.addGestureListener(FlickProcessor.class, ctrlFlick);
	}

	public void setGestureClassic() {
		final PApplet pa = pApplet;
		this.registerInputProcessor(new TapProcessor(pApplet, 25, true, 350));
		this.registerInputProcessor(new TapAndHoldProcessor(
				(AbstractMTApplication) pApplet, 1000));

		this.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer((AbstractMTApplication) pApplet, this));
		this.addGestureListener(ScaleProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						if (ge instanceof ScaleEvent && autoUpdate) {
							switch (ge.getId()) {
							case ScaleEvent.GESTURE_ENDED:
								((MTApplication) pa).invokeLater(new Thread() {
									public void run() {
										updateTexture();
									}
								});
								updateTexture();
								break;
							default:
								break;
							}
						}
						return false;
					}
				});

		this.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						if (ge instanceof TapEvent && autoUpdate) {
							if (((TapEvent) ge).isDoubleTap()) {
								pdf.nextPage();
							}
						}
						return false;
					}
				});
		setMasterGestureMenu();
	}

	/**
	 * Sets the configuration of the MTPDF master context menu.
	 */
	public void setMasterGestureMenu() {
		this.addGestureListener(TapAndHoldProcessor.class,
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
								menu = new PDFMenu(pApplet, getParent(), 40,
										120, MTPDF.this, th);

							}
							break;
						default:
							break;
						}
						return false;
					}
				});
	}
	@Override
	public synchronized void pageChanged(int newPage) {
		if (this.pdf != null) {
			updateTexture();
			pageNumbers.setText(newPage + " / " + pdf.getNumberOfPages());
		}
	}

	/**
	 * Updates the image of the PDF.
	 */
	public synchronized void updateTexture() {
		// round to full pixels
		int width = Math.round(this.getWidthXYGlobal());
		int height = Math.round(this.getHeightXYGlobal());

		int textureWidth, textureHeight;
		if (width > this.sizeLimitX) {
			double ratio = ((double) width) / ((double) height);
			textureWidth = sizeLimitX;
			textureHeight = (int) Math.round((double) sizeLimitX
					* (double) ratio);
		} else {
			textureWidth = width;
			textureHeight = height;
		}
		// resize to full pixels
		setSizeXYGlobal(width, height);
		PImage img = null;

		// use double image size as texture (better while scaling up) ?
		img = new PImage(pdf.getImagePage(textureWidth, textureHeight));
		setTexture(img);

	}

	public void showOutline() {
		if (outline != null) {
			AnimationUtil.bounceOut(outline, true);
			outline.destroy();
			outline = null;
		}

		Vector3D position = getPosition(TransformSpace.GLOBAL);
		outline = new PDFOutline((MTApplication) getRenderer(),
				position.x + 10, position.y + 10, 330, 350, pdf);
		getParent().addChild(outline);
	}

	public void remove() {
			AnimationUtil.bounceOut(this, true);

			if (outline != null) {
				AnimationUtil.bounceOut(outline, true);
				outline = null;
			}
	}


	/**
	 * Controller for navigating in the PDF with flick events.
	 * 
	 */
	public class NavigationFlickController implements IGestureEventListener {
		public boolean processGestureEvent(MTGestureEvent ge) {
			FlickEvent e = (FlickEvent) ge;
			if (e.getId() == MTGestureEvent.GESTURE_ENDED) {

				if (e.getDirection() == FlickDirection.EAST) {
					pdf.nextPage();
				}
				if (e.getDirection() == FlickDirection.WEST) {
					pdf.previousPage();
				}
			}
			return false;
		}
	}

	/**
	 * Controller for navigating in the PDF with tap events (when double tapping
	 * on button).
	 * 
	 */
	public class NavigationTapController implements IGestureEventListener {
		private int action;
		public static final int NEXT_PAGE = 1;
		public static final int PREVIOUS_PAGE = 2;
		public static final int DOUBLE_NEXT_PAGE = 3;
		public static final int DOUBLE_PREVIOUS_PAGE = 4;

		public NavigationTapController(int a) {
			action = a;
		}

		public boolean processGestureEvent(MTGestureEvent ge) {
			if (ge instanceof TapEvent
					&& ge.getId() == MTGestureEvent.GESTURE_ENDED) {
				switch (action) {
				case NEXT_PAGE:
					pdf.nextPage();
					break;
				case PREVIOUS_PAGE:
					pdf.previousPage();
					break;
				case DOUBLE_NEXT_PAGE:
					pdf.setPageNumber(pdf.getPageNumber() + LARGE_MOVE);
					break;
				case DOUBLE_PREVIOUS_PAGE:
					pdf.setPageNumber(pdf.getPageNumber() - LARGE_MOVE);
					break;
				}
			}
			return false;
		}
	}
}
