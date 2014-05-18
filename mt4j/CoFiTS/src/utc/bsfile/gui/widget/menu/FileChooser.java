package utc.bsfile.gui.widget.menu;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;
import utc.bsfile.gui.Theme;
import utc.bsfile.gui.Theme.StyleID;
import utc.bsfile.model.menu.FileChooserModel;
import utc.bsfile.util.FileExtensionIconManager;
import utc.bsfile.util.PositionSequencer;
import utc.bsfile.util.PropertyManager;
import utc.bsfile.util.PositionSequencer.Orientation;


public class FileChooser extends ListMenu
{
	/**
	 * @uml.property  name="root"
	 */
	private File root;

	/**
	 * @uml.property  name="parentButton"
	 * @uml.associationEnd  
	 */
	private MTImageButton parentButton;
	/**
	 * @uml.property  name="startButton"
	 * @uml.associationEnd  
	 */
	//private MTImageButton startButton;
	/**
	 * @uml.property  name="homeButton"
	 * @uml.associationEnd  
	 */
	//private MTImageButton homeButton;
	/**
	 * @uml.property  name="desktopButton"
	 * @uml.associationEnd  
	 */
	//private MTImageButton desktopButton;
	/**
	 * @uml.property  name="rootButton"
	 * @uml.associationEnd  
	 */
	//private MTImageButton rootButton;
	/**
	 * @uml.property  name="dropboxButton"
	 * @uml.associationEnd  
	 */
	//private MTImageButton dropboxButton;
	/**
	 * @uml.property  name="actionButton"
	 * @uml.associationEnd  
	 */
	private MTImageButton actionButton;
	/**
	 * @uml.property  name="cancelActionButton"
	 * @uml.associationEnd  
	 */
	
	private MTImageButton cancelActionButton;
	/**
	 * @uml.property  name="deleteButton"
	 * @uml.associationEnd  
	 */
	private MTImageButton deleteButton;
	/**
	 * @uml.property  name="shareButton"
	 * @uml.associationEnd  
	 */
	private MTImageButton shareButton;
	/**
	 * @uml.property  name="disabledDeleteButton"
	 * @uml.associationEnd  
	 */
	private MTImageButton disabledDeleteButton;
	/**
	 * @uml.property  name="disabledShareButton"
	 * @uml.associationEnd  
	 */
	private MTImageButton disabledShareButton;

	private MTRectangle confirmationWindow;
	private MTTextArea deleteConfirmText;
	//private MTImageButton confirmDeleteButton;
	//private MTImageButton cancelDeleteButton;
	private MTTextArea confirmDeleteButton;
	private MTTextArea cancelDeleteButton;

	public PApplet applet;
	
	public FileChooser(PApplet applet, int x, int y, float width, int nbItem,
			File path, FileFilter filter) {
		super(applet, x, y, width, nbItem, new FileChooserModel(path, filter));

		root = path.getAbsoluteFile();

		this.applet = applet;
		
		PositionSequencer position = new PositionSequencer(new Vector3D(x + getSpacing(), y + getSpacing()), getSpacing(), Orientation.HORIZONTAL);
		ButtonListener listener = new ButtonListener();
		parentButton = createIconButton(position.getPosition(), "parent-icon.png", listener); 
		position.nextPosition(parentButton);
		Vector3D savedPosition = position.getPosition();
		actionButton = createIconButton(savedPosition, "action-icon.png", listener);
		actionButton.setWidthXYGlobal(70);
		actionButton.setHeightXYGlobal(30);
		cancelActionButton = createIconButton(savedPosition, "cancel.png", listener);
		
		
		PositionSequencer disabledBottomPosition = new PositionSequencer(new Vector3D(x + getSpacing(), y + getSpacing() + 250), Orientation.HORIZONTAL);
		disabledShareButton = createIconButton(disabledBottomPosition.getPosition(), "disabledShare-icon.png", listener);
		disabledBottomPosition.nextPosition(disabledShareButton);
		disabledDeleteButton = createIconButton(disabledBottomPosition.getPosition(), "disabledDelete-icon.png", listener);
		disabledBottomPosition.nextPosition(disabledDeleteButton);
		
		PositionSequencer bottomPosition = new PositionSequencer(new Vector3D(x + getSpacing(), y + getSpacing() + 250), Orientation.HORIZONTAL);
		shareButton = createIconButton(bottomPosition.getPosition(), "share-icon.png", listener);
		bottomPosition.nextPosition(shareButton);
		deleteButton = createIconButton(bottomPosition.getPosition(), "delete-icon.png", listener);
		bottomPosition.nextPosition(deleteButton);
		
		addChild(actionButton);
		addChild(parentButton);
	}
	
	public void displayDisabledBottomButtons() {
		removeChild(actionButton);
		
		addChild(cancelActionButton);
		addChild(disabledShareButton);
		addChild(disabledDeleteButton);
	}
	
	public void displayBottomButtons() {
		addChild(shareButton);
		addChild(deleteButton);
	}
	
	public void removeBottomButtons() {		
		removeChild(cancelActionButton);
		removeChild(disabledShareButton);
		removeChild(disabledDeleteButton);
	}
	
	public void removeActiveBottomButtons() {
		removeChild(cancelActionButton);
		removeChild(shareButton);
		removeChild(deleteButton);
	}
	
	public void displayTopButtons() {
		addChild(actionButton);
	}
	

	public FileChooser(PApplet applet, int x, int y, float width, int nbItem,
			String path, FileFilter filter) {
		this(applet, x, y, width, nbItem, new File(path), filter);
	}

	public FileChooser(PApplet applet, int x, int y, float width, int nbItem,
			File path) {
		this(applet, x, y, width, nbItem, path, null);
	}

	public FileChooser(PApplet applet, int x, int y, float width, int nbItem,
			String path) {
		this(applet, x, y, width, nbItem, path, null);
	}

	protected MTComponent makeChoiceView(Object choice)
	{
		MTComponent component = new MTComponent(getRenderer());
		File file = (File) choice;
		try {
			MTRectangle fileExtIcon = new MTRectangle(getRenderer(),
					getSpacing(), 0, iconWidth, iconHeight);
			PImage icon = FileExtensionIconManager.getInstance().getIcon(file);
			if (icon != null) {
				fileExtIcon.setTexture(icon);
				fileExtIcon.setNoStroke(true);
				component.addChild(fileExtIcon);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		MTTextArea textArea = new MTTextArea(getRenderer(), getSpacing()
				+ iconWidth, 0,
				getWidthXYGlobal() - iconWidth - getSpacingX2(),
				choiceViewHeight);
		String sfont = PropertyManager.getInstance().getProperty(PropertyManager.PICK_FONT);
		textArea.setFont(FontManager.getInstance().createFont(getRenderer(),
				sfont, 16, MTColor.BLACK, true));
		textArea.setText(file.getName());
		Theme.getTheme().applyStyle(StyleID.TRANSPARENT, textArea);
		component.addChild(textArea);

		return component;
	}

	public FileChooserModel getFileChooserModel() {
		return (FileChooserModel) this.getModel();
	}

	protected class ButtonListener implements IGestureEventListener {
		
		public boolean processGestureEvent(MTGestureEvent ge) {

			if (ge instanceof TapEvent) {
				TapEvent tapEvent = (TapEvent) ge;

				if (tapEvent.isTapped()) {
					if (tapEvent.getTarget() == parentButton) {
						Object parent = getModel().getParentMenu(
								FileChooser.this.getModel().getCurrentMenu());
						System.out.println("CURRENT : " + ((File)getModel().getCurrentMenu()).getPath());
						
						//We can go to parent only if we are under FILE_PATH property root
						if (parent != null && !(((File) getModel().getCurrentMenu()).getAbsolutePath() + "/").equals(PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH))) {
							FileChooser.this.getModel().setCurrentMenu(parent);
							updateList();
						}
						
					} else if (tapEvent.getTarget() == actionButton) {
						setSelectionList(FileChooser.this);
						displayDisabledBottomButtons();
					} else if (tapEvent.getTarget() == cancelActionButton) {
						updateList();
						removeBottomButtons();
						removeActiveBottomButtons();
						displayTopButtons();
						
					} else if (tapEvent.getTarget() == deleteButton) {
						
					}
						
				}
			}

			return false;
		}
	}
}
