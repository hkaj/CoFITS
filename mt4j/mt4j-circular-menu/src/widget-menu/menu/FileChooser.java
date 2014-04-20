package utc.tatinpic.gui.widgets.menu;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
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
import utc.tatinpic.gui.Theme;
import utc.tatinpic.gui.Theme.StyleID;
import utc.tatinpic.model.menu.FileChooserModel;
import utc.tatinpic.util.manager.FileExtensionIconManager;
import utc.tatinpic.util.manager.PropertyManager;
import utc.tatinpic.util.positionSequencer.PositionSequencer;
import utc.tatinpic.util.positionSequencer.PositionSequencer.Orientation;

import utc.tatinpic.gui.widgets.fileshare.FileShareMenu;
import utc.tatinpic.gui.widgets.fileshare.MultiFileDeleteMenu;
import utc.tatinpic.gui.widgets.fileshare.MultiFileShareMenu;

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
	private MTImageButton dropboxButton;
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
	private MultiFileDeleteMenu mfd;
	private MultiFileShareMenu mfs;

	public PApplet applet;
	
	public FileChooser(PApplet applet, int x, int y, float width, int nbItem,
			File path, FileFilter filter) {
		super(applet, x, y, width, nbItem, new FileChooserModel(path, filter));

		root = path.getAbsoluteFile();

		while (root.getParentFile() != null)
			root = root.getParentFile();

		this.applet = applet;
		
		PositionSequencer position = new PositionSequencer(new Vector3D(x + getSpacing(), y + getSpacing()), getSpacing(), Orientation.HORIZONTAL);
		ButtonListener listener = new ButtonListener();
		parentButton = createIconButton(position.getPosition(), "IMG:/parent-icon.png", listener); 
		position.nextPosition(parentButton);
		dropboxButton = createIconButton(position.getPosition(), "IMG:/dropbox-icon.png", listener);
		position.nextPosition(dropboxButton);
		Vector3D savedPosition = position.getPosition();
		actionButton = createIconButton(savedPosition, "IMG:/action-icon.png", listener);
		actionButton.setWidthXYGlobal(70);
		actionButton.setHeightXYGlobal(30);
		cancelActionButton = createIconButton(savedPosition, "IMG:/cancel.png", listener);
		
		PositionSequencer disabledBottomPosition = new PositionSequencer(new Vector3D(x + getSpacing(), y + getSpacing() + 250), Orientation.HORIZONTAL);
		disabledShareButton = createIconButton(disabledBottomPosition.getPosition(), "IMG:/disabledShare-icon.png", listener);
		disabledBottomPosition.nextPosition(disabledShareButton);
		disabledDeleteButton = createIconButton(disabledBottomPosition.getPosition(), "IMG:/disabledDelete-icon.png", listener);
		disabledBottomPosition.nextPosition(disabledDeleteButton);
		
		PositionSequencer bottomPosition = new PositionSequencer(new Vector3D(x + getSpacing(), y + getSpacing() + 250), Orientation.HORIZONTAL);
		shareButton = createIconButton(bottomPosition.getPosition(), "IMG:/share-icon.png", listener);
		bottomPosition.nextPosition(shareButton);
		deleteButton = createIconButton(bottomPosition.getPosition(), "IMG:/delete-icon.png", listener);
		bottomPosition.nextPosition(deleteButton);
		
		addChild(parentButton);
		addChild(dropboxButton);
		addChild(actionButton);
	}
	
	public void displayDisabledBottomButtons() {
		removeChild(parentButton);
		removeChild(dropboxButton);
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
		addChild(parentButton);
		addChild(dropboxButton);
		addChild(actionButton);
	}
	
	public void displayShareMenu(FileShareMenu fsm) {
		addChild(fsm);
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
		textArea.setFont(FontManager.getInstance().createFont(getRenderer(),
				"calibri", 16, MTColor.BLACK, true));
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
			 File dropboxDir = new File(PropertyManager.getInstance()
					.getDirProperty(PropertyManager.DROPBOX_DIR) + getWorkbench().getTeamMember() + "-tatin-pic/");
			 

			if (ge instanceof TapEvent) {
				TapEvent tapEvent = (TapEvent) ge;

				if (tapEvent.isTapped()) {
					if (tapEvent.getTarget() == parentButton) {
						Object parent = getModel().getParentMenu(
								FileChooser.this.getModel().getCurrentMenu());
						System.out.println("CURRENT : " + ((File)getModel().getCurrentMenu()).getPath());
						System.out.println("DROPBOX : " + dropboxDir.getPath());
						if (parent != null && ( !((File)getModel().getCurrentMenu()).getPath().equals(dropboxDir.getPath()) || getWorkbench().getRow() == 0) ){
							FileChooser.this.getModel().setCurrentMenu(parent);
							updateList();
						}
					} else if (tapEvent.getTarget() == dropboxButton) {
						FileChooser.this.getModel().setCurrentMenu(
								dropboxDir);
						updateList();
					} else if (tapEvent.getTarget() == actionButton) {
						setSelectionList(FileChooser.this);
						displayDisabledBottomButtons();
					} else if (tapEvent.getTarget() == cancelActionButton) {
						updateList();
						removeBottomButtons();
						removeActiveBottomButtons();
						displayTopButtons();
						if (mfd != null) {
							mfd.close();
						}
						if (mfs != null) {
							mfs.close();
						}
					} else if (tapEvent.getTarget() == deleteButton) {
						mfd = new MultiFileDeleteMenu(FileChooser.this.applet, FileChooser.this.getWorkbench(), FileChooser.this.getWorkbenches(), FileChooser.this.getListFiles(), FileChooser.this);
						FileChooser.this.addChild(mfd);
					} else if (tapEvent.getTarget() == shareButton) {
						mfs = new MultiFileShareMenu(FileChooser.this.applet, FileChooser.this.getWorkbench(), FileChooser.this.getWorkbenches(), FileChooser.this.getListFiles());
						FileChooser.this.addChild(mfs);
					}
				}
			}

			return false;
		}
	}
}
