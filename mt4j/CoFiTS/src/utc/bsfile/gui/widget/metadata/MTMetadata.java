package utc.bsfile.gui.widget.metadata;

import java.io.File;
import java.io.IOException;

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
import utc.bsfile.gui.Theme;
import utc.bsfile.gui.Theme.StyleID;
import utc.bsfile.model.metadata.UnknownFile;
import utc.bsfile.util.FileExtensionIconManager;

public class MTMetadata extends MTRectangle implements IGestureEventListener {

	private static int spacing = 5;
	public static int choiceViewHeight = 35;
	public static int iconWidth = 40;
	public static int iconHeight = 40;
	private static final int INITIAL_WIDTH = 400;
	private static final int INITIAL_HEIGHT = 200;
	private UnknownFile metaFile;
	private MTList listMetadata;
	private MTTextArea fileName;

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
			float height, UnknownFile unknownFile) {
		super(pApplet, x, y, width, height);
		this.metaFile = unknownFile;
		buildMetadataView(pApplet, x, y, width, height);
	}

	public MTMetadata(PApplet pApplet, UnknownFile unknownFile) {
		this(pApplet, 0, 0, MTMetadata.INITIAL_WIDTH,
				MTMetadata.INITIAL_HEIGHT, unknownFile);
	}

	public MTMetadata(MTMetadata mtMetadata) {
		this(mtMetadata.getRenderer(), mtMetadata.metaFile);
	}

	public void buildMetadataView(PApplet pApplet, float x, float y,
			float width, float height) {

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

		Theme.getTheme().applyStyle(StyleID.DEFAULT, this);
		this.setStrokeWeight(2f);
		this.setAnchor(PositionAnchor.UPPER_LEFT);

		this.listMetadata = new MTList(pApplet, x + 2 * width / 5, y
				+ getSpacedIconHeight() / 2, 3 * width / 5, height
				- getSpacedIconHeight(), 0);
		Theme.getTheme().applyStyle(StyleID.STROKE_ONLY, listMetadata);
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


	private void fillMetadata(float x, float y, float width) {

		float xChanged = x + 2 * width / 5;
		float yChanged = y + getSpacedIconHeight() / 4;
		addChild(makeChoiceView("Fichier:", this.metaFile.getNameFile(),
				xChanged, yChanged, 3 * width / 5));
		addChild(makeChoiceView("Date\ncr�ation:",
				this.metaFile.getDateCreation(), xChanged, yChanged
						+ choiceViewHeight, 3 * width / 5));
		addChild(makeChoiceView("Derni�re\nmodification:",
				this.metaFile.getLastModified(), xChanged, yChanged
						+ choiceViewHeight * 2, 3 * width / 5));
		addChild(makeChoiceView("Dernier\nacc�s:",
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

		// addCell("Cr�� par:", this.metaFile.getOwer());

		addCell("Date cr�ation:", this.metaFile.getDateCreation());

		addCell("Derni�re modification:", this.metaFile.getLastModified());

		addCell("Derni�re acc�s:", this.metaFile.getLastAccess());

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
	}


}
