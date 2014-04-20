package utc.tatinpic.gui.widgets.metadata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.util.animation.AnimationUtil;

import processing.core.PApplet;
import processing.core.PImage;
import utc.tatinpic.gui.LocationReference;
import utc.tatinpic.gui.Theme;
import utc.tatinpic.gui.Theme.StyleID;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.model.metadata.UnknownFile;
import utc.tatinpic.util.manager.FileExtensionIconManager;

public class MTMetadata extends MTRectangle implements IGestureEventListener {

	private static int spacing = 5;
	public static int choiceViewHeight = 35;
	public static int iconWidth = 40;
	public static int iconHeight = 40;
	private static final int INITIAL_WIDTH = 400;
	private static final int INITIAL_HEIGHT = 200;
	private UnknownFile metaFile;
	private MTList listMetadata;
	private Workbench workbench;
	private List<Workbench> workbenchs;
	private ArrayList<MTMetadata> slave = new ArrayList<MTMetadata>();
	private MTTextArea fileName;
	private boolean isMaster = true;
	private MetadataMenu menu = null;
	private MetadataCloneMenu menuClone = null;
	private PImage icon;

	public static int getSpacing() {
		return spacing;
	}

	public static int getSpacingX2() {
		return spacing * 2;
	}

	public static void setSpacing(int spacing) {
		MTMetadata.spacing = spacing;
	}

	public static int getSpacedIconWidth() {
		return iconWidth + getSpacingX2();
	}

	public static int getSpacedIconHeight() {
		return iconHeight + getSpacingX2();
	}

	private MTMetadata(PApplet pApplet, float x, float y, float width,
			float height, UnknownFile unknownFile, Workbench workbench,
			List<Workbench> workbenchs) {
		super(pApplet, x, y, width, height);
		this.workbench = workbench;
		this.workbenchs = workbenchs;
		this.metaFile = unknownFile;
		buildMetadataView(pApplet, x, y, width, height, workbench, workbenchs);
	}

	public MTMetadata(PApplet pApplet, UnknownFile unknownFile,
			Workbench workbench, List<Workbench> workbenchs) {
		this(pApplet, 0, 0, MTMetadata.INITIAL_WIDTH,
				MTMetadata.INITIAL_HEIGHT, unknownFile, workbench, workbenchs);
	}

	public MTMetadata(MTMetadata mtMetadata) {
		this(mtMetadata.getRenderer(), mtMetadata.metaFile,
				mtMetadata.workbench, mtMetadata.workbenchs);
	}

	public void buildMetadataView(PApplet pApplet, float x, float y,
			float width, float height, Workbench workbench,
			List<Workbench> workbenchs) {

		// inicialization

		try {
			MTRectangle fileExtIcon = new MTRectangle(getRenderer(), x
					+ getSpacedIconHeight() / 4, y + getSpacedIconHeight() / 2,
					2 * width / 5 - getSpacedIconHeight() / 2, height
							- getSpacedIconHeight());
			icon = FileExtensionIconManager.getInstance().getIcon(
					this.metaFile.getFile());

			if (icon != null) {
				fileExtIcon.setTexture(icon);
				fileExtIcon.setNoStroke(true);
				fileExtIcon.setPickable(false);
				addChild(fileExtIcon);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setAnchor(PositionAnchor.UPPER_LEFT);
		// defaults used by segments
		// this.setFillColor(new MTColor(0, 0, 0, 255 - 32));

		// this.setNoStroke(true);

		Theme.getTheme().applyStyle(StyleID.DEFAULT, this);
		this.setStrokeColor(workbench.getColor());
		this.setStrokeWeight(2f);
		this.setAnchor(PositionAnchor.UPPER_LEFT);

		this.listMetadata = new MTList(pApplet, x + 2 * width / 5, y
				+ getSpacedIconHeight() / 2, 3 * width / 5, height
				- getSpacedIconHeight(), 0);
		Theme.getTheme().applyStyle(StyleID.STROKE_ONLY, listMetadata);
		// fillMetadata();
		// addChild(this.listMetadata);
		fillMetadata(x, y, width);
		addFileName();

		setGestureClassic();
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
								menu = new MetadataMenu(getRenderer(), icon,
										getParent(), 40, 120, MTMetadata.this,
										th, workbench, workbenchs);
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
								menuClone = new MetadataCloneMenu(
										getRenderer(), icon, getParent(),
										MTMetadata.this, th, workbench,
										workbenchs);
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
	}

	private void fillMetadata(float x, float y, float width) {

		float xChanged = x + 2 * width / 5;
		float yChanged = y + getSpacedIconHeight() / 4;
		addChild(makeChoiceView("Fichier:", this.metaFile.getNameFile(),
				xChanged, yChanged, 3 * width / 5));
		addChild(makeChoiceView("Date\ncréation:",
				this.metaFile.getDateCreation(), xChanged, yChanged
						+ choiceViewHeight, 3 * width / 5));
		addChild(makeChoiceView("Dernière\nmodification:",
				this.metaFile.getLastModified(), xChanged, yChanged
						+ choiceViewHeight * 2, 3 * width / 5));
		addChild(makeChoiceView("Dernier\naccès:",
				this.metaFile.getLastAccess(), xChanged, yChanged
						+ choiceViewHeight * 3, 3 * width / 5));
		addChild(makeChoiceView("Taille:", this.metaFile.getSize(), xChanged,
				yChanged + choiceViewHeight * 4, 3 * width / 5));
	}

	private MTComponent makeChoiceView(String label, String text, float x,
			float y, float width) {
		MTTextArea textArea = new MTTextArea(getRenderer(), x, y, width,
				choiceViewHeight);

		textArea.setFont(FontManager.getInstance().createFont(getRenderer(),
				"calibri", 16, MTColor.WHITE, true));
		textArea.setText(label);
		textArea.setNoFill(true);
		textArea.setNoStroke(true);
		textArea.setPickable(false);

		MTTextArea test = new MTTextArea(getRenderer(), 88, 0, width - 88,
				choiceViewHeight);

		test.setFont(FontManager.getInstance().createFont(getRenderer(),
				"calibri", 16, MTColor.WHITE, true));
		test.setText(text);
		test.setNoFill(true);
		test.setNoStroke(true);
		test.setPickable(false);
		textArea.addChild(test);

		return textArea;
	}

	private void fillMetadata() {

		addCell("Fichier:", this.metaFile.getNameFile());

		//addCell("Créé par:", this.metaFile.getOwer());

		addCell("Date création:", this.metaFile.getDateCreation());

		addCell("Dernière modification:", this.metaFile.getLastModified());

		addCell("Dernière accès:", this.metaFile.getLastAccess());

		addCell("Taille:", this.metaFile.getSize());

	}

	private void addCell(String label, String text) {
		MTListCell cell = new MTListCell(getRenderer(), getWidthXYGlobal()
				- getSpacingX2(), choiceViewHeight);
		Theme.getTheme().applyStyle("LIST_CHOICE", cell);
		// cell.setUserData(CHOICE, choice);
		cell.addChild(makeChoiceView(label, text));
		cell.registerInputProcessor(new TapProcessor(getRenderer()));
		cell.addGestureListener(TapProcessor.class, this);
		listMetadata.addListElement(cell);
	}

	private void addFileName() {
		fileName = new MTTextArea(getRenderer(), FontManager.getInstance()
				.createFont(getRenderer(), "calibri", 16, MTColor.WHITE, true));
		fileName.setAnchor(PositionAnchor.CENTER);
		fileName.removeAllGestureEventListeners();
		fileName.setText(metaFile.getFile().getName());
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

	private MTComponent makeChoiceView(String label, String text) {
		MTTextArea textArea = new MTTextArea(getRenderer(), 0, 0,
				getWidthXYGlobal() / 4, choiceViewHeight);

		textArea.setFont(FontManager.getInstance().createFont(getRenderer(),
				"calibri", 12, MTColor.BLACK, true));
		textArea.setText(label);
		textArea.setNoFill(true);
		textArea.setNoStroke(true);

		MTTextArea test = new MTTextArea(
				getRenderer(),
				textArea.getWidthXY(TransformSpace.RELATIVE_TO_PARENT),
				0,
				getWidthXYGlobal() - textArea.getWidthXY(TransformSpace.GLOBAL),
				choiceViewHeight);

		test.setFont(FontManager.getInstance().createFont(getRenderer(),
				"calibri", 12, MTColor.BLACK, true));
		Theme.getTheme().applyStyle("LIST_CHOICE", test);

		test.setText(text);
		textArea.setNoFill(true);
		textArea.setNoStroke(true);
		textArea.addChild(test);

		return textArea;
	}

	public File getFile() {
		return metaFile.getFile();
	}

	public void remove() {
		AnimationUtil.bounceOut(this, true);
		if (!slave.isEmpty()) {
			for (int i = 0; i < slave.size(); i++) {
				slave.get(i).remove();
			}
			slave = new ArrayList<MTMetadata>();
		}
	}

	public void cloneMetadata() {
		MTMetadata clone = new MTMetadata(this);
		clone.translate(getCenterPointGlobal().getScaled(0.5f));
		clone.isMaster = false;
		LocationReference lr = LocationReference.getLocation(workbench.getRow());
		clone.rotateZGlobal(clone.getCenterPointGlobal(), lr.getDirection());
		slave.add(clone);
		getParent().addChild(clone);
	}

	public void removeClone(MTMetadata clone) {
		slave.remove(clone);
		AnimationUtil.bounceOut(clone, true);
	}

}
