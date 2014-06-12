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
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.model.menu.ProjectArchitectureModel;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.FileExtensionIconManager;
import utc.bsfile.util.PositionSequencer;
import utc.bsfile.util.PositionSequencer.Orientation;
import utc.bsfile.util.PropertyManager;



public class FileChooser extends ListMenu
{

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
			IMenuModel model, TwoLinkedJsonNode start) {
		super(applet, x, y, width, nbItem, model);
		
		m_start = start;
		
		if (model instanceof ProjectArchitectureModel){
			model.setCurrentMenu(m_start);
		}
		
		//---------------------------------------------------------------------------
		this.pathField = new MTTextArea(applet,  x + getSpacing() , y + getSpacing() + getSpacedIconHeight(),(int) width, getPathFieldHeight());
		if ( pathString.length() > 30 ) {
			pathString = pathString.substring(0, 30) + "...";
		}
		String sfont = PropertyManager.getInstance().getProperty(PropertyManager.PICK_FONT);
		pathField.setFont(FontManager.getInstance().createFont(getRenderer(),sfont, 16, MTColor.BLACK, true));
		pathField.setFontColor(new MTColor(255, 255, 255, 255));
		pathField.setText(pathString);
		pathField.setNoFill(true);
		pathField.setNoStroke(true);
		pathField.removeAllGestureEventListeners();
		//---------------------------------------------------------------------------

		this.applet = applet;
		
		PositionSequencer position = new PositionSequencer(new Vector3D(x + getSpacing(), y + getSpacing()), getSpacing(), Orientation.HORIZONTAL);
		ButtonListener listener = new ButtonListener();
		parentButton = createIconButton(position.getPosition(), "parent-icon.png", listener);
		parentButton.setSizeXYGlobal(40, 40);
		position.nextPosition(parentButton);
		Vector3D savedPosition = position.getPosition();
		actionButton = createIconButton(savedPosition, "action-icon-on.png", listener);
		actionButton.setWidthXYGlobal(75);
		actionButton.setHeightXYGlobal(35);
		cancelActionButton = createIconButton(savedPosition, "action-icon-off.png", listener);
		cancelActionButton.setWidthXYGlobal(75);
		cancelActionButton.setHeightXYGlobal(35);
		
		
		PositionSequencer disabledBottomPosition = new PositionSequencer(new Vector3D(x + getSpacing(), y + getSpacing() + calcHeightUntilListBottom(nbItem) -4 ), Orientation.HORIZONTAL);
		disabledShareButton = createIconButton(disabledBottomPosition.getPosition(), "disabledShare-icon.png", listener);
		disabledBottomPosition.nextPosition(disabledShareButton);
		disabledDeleteButton = createIconButton(disabledBottomPosition.getPosition(), "disabledDelete-icon.png", listener);
		disabledBottomPosition.nextPosition(disabledDeleteButton);
		
		PositionSequencer bottomPosition = new PositionSequencer(new Vector3D(x + getSpacing(), y + getSpacing() + calcHeightUntilListBottom(nbItem) -4 ), Orientation.HORIZONTAL);
		shareButton = createIconButton(bottomPosition.getPosition(), "share-icon.png", listener);
		bottomPosition.nextPosition(shareButton);
		deleteButton = createIconButton(bottomPosition.getPosition(), "delete-icon.png", listener);
		bottomPosition.nextPosition(deleteButton);
		
		addChild(actionButton);
		addChild(parentButton);
		addChild(pathField);

	}
	
	
	public FileChooser(PApplet applet, int x, int y, float width, int nbItem,
			File path, FileFilter filter) {
		this(applet, x, y, width, nbItem, new FileChooserModel(path, filter), null);

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
		TwoLinkedJsonNode nodeChoice = (TwoLinkedJsonNode) choice;
		try {
			
			MTRectangle fileExtIcon = new MTRectangle(getRenderer(),
					getSpacing(), 3, iconWidth, iconHeight);
			//TODO Find why icon is not printed
			PImage icon = FileExtensionIconManager.getInstance().getIcon(nodeChoice.getName());
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
		textArea.setFont(FontManager.getInstance().createFont(getRenderer(), sfont, 16, MTColor.BLACK, true));
		
		if ( nodeChoice.getName().length() <= 20 ){
			textArea.setText(nodeChoice.getName());
		} else {
			textArea.setText(nodeChoice.getName().substring(0, 20)+"...");
		}
		
		Theme.getTheme().applyStyle(StyleID.TRANSPARENT, textArea);
		component.addChild(textArea);

		return component;
	}

	public FileChooserModel getFileChooserModel() {
		return (FileChooserModel) this.getModel();
	}

	
	/**
	 * Define the behavior for tapping on any button on fileChooser
	 *
	 */
	protected class ButtonListener implements IGestureEventListener {
		
		public boolean processGestureEvent(MTGestureEvent ge) {

			if (ge instanceof TapEvent) {
				TapEvent tapEvent = (TapEvent) ge;

				if (tapEvent.isTapped()) {
					if (tapEvent.getTarget() == parentButton) {
						Object parent = getModel().getParentMenu(FileChooser.this.getModel().getCurrentMenu());
						//System.out.println("CURRENT : " + ((File)getModel().getCurrentMenu()).getPath());
						
						File fileChoice;
						
						if (getModel().getCurrentMenu() instanceof TwoLinkedJsonNode){
							TwoLinkedJsonNode node = (TwoLinkedJsonNode) getModel().getCurrentMenu();
							fileChoice = new File(node.getName());
						} else {
							fileChoice = (File) getModel().getCurrentMenu();
						}
						
						//We can go to parent only if we are under FILE_PATH property root
						if (parent != null && !fileChoice.getAbsolutePath().equals(PropertyManager.getInstance().getProperty(PropertyManager.FILE_PATH))) {
							
							File fileChoiceParent;
							
							if (getModel().getCurrentMenu() instanceof TwoLinkedJsonNode){
								TwoLinkedJsonNode node = (TwoLinkedJsonNode) parent;
								fileChoiceParent = new File(node.getName());
							} else {
								fileChoiceParent = (File) parent;
							}
							
							FileChooser.this.getModel().setCurrentMenu(parent);
							updateList();
							//-----------------------------------------------------------------------
							// Max : set the current path with the parent's path (update the path textField)
							setPath(fileChoiceParent);
							//-----------------------------------------------------------------------
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
	
	
	//Members
	private TwoLinkedJsonNode m_start;
}
