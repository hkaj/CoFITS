package utc.bsfile.gui.widget.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.bsfile.gui.Theme;
import utc.bsfile.gui.Theme.StyleID;
import utc.bsfile.gui.widget.pick.PickMTList;
import utc.bsfile.model.menu.DefaultMenuModel;
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.util.ImageManager;
import utc.bsfile.util.PropertyManager;

public class ListMenu extends MTRectangle implements IGestureEventListener {
	private static final String CHOICE = "CHOICE";
	private static int spacing = 10;
	private static int pathFieldHeight = 40;
	public static int choiceViewHeight = 46;
	public static int iconWidth = 40;
	public static int iconHeight = 40;
	protected ArrayList<File> listCells = new ArrayList<File>();
	protected boolean m_areCellsEnabled = true;

	private int nbItems = 0;

	public void setCellsEnabled(boolean isEnabled){
		m_areCellsEnabled = isEnabled;
	}
	
	public final boolean areCellsEnabled(){
		return m_areCellsEnabled;
	}
	
	public void emptyListCells() {
		this.listCells.clear();
	}

	public ArrayList<File> getListFiles() {
		return this.listCells;
	}

	public static int getSpacing() {
		return spacing;
	}
	
	public static int getPathFieldHeight() {
		return pathFieldHeight;
	}

	public static int getSpacingX2() {
		return spacing * 2;
	}

	public static void setSpacing(int spacing) {
		ListMenu.spacing = spacing;
	}

	public static int getSpacedIconWidth() {
		return iconWidth + getSpacingX2();
	}

	public static int getSpacedIconHeight() {
		return iconHeight + getSpacingX2();
	}

	public static float calcHeight(int visibleChoiceCount) {
		return (float) ((visibleChoiceCount * choiceViewHeight) + (getSpacedIconHeight() * 2));
	}
	
	public static float calcHeightUntilListBottom(int visibleChoiceCount) {
		return (float) ((visibleChoiceCount * choiceViewHeight) + (getSpacedIconHeight()));
	}

	/**
	 * @uml.property name="menuModel"
	 * @uml.associationEnd
	 */
	private IMenuModel menuModel;
	/**
	 * @uml.property name="mustBeDestroy"
	 */
	private boolean mustBeDestroy;
	/**
	 * @uml.property name="list"
	 * @uml.associationEnd
	 */
	private PickMTList list;
	/**
	 * @uml.property name="closeButton"
	 * @uml.associationEnd
	 */
	private MTImageButton closeButton;
	/**
	 * @uml.property name="backButton"
	 * @uml.associationEnd
	 */
	private MTImageButton backButton;
	/**
	 * @uml.property name="listeners"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="utc.tatinpic.gui.widgets.menu.ListMenu$ChoiceListener"
	 */
	private HashSet<ChoiceListener> listeners;
	private PApplet applet;

	public ListMenu(PApplet applet, int x, int y, float width, int nbItem,
			IMenuModel model) {
		super(applet, x, y, width, calcHeight(nbItem));
		float height = calcHeight(nbItem);
		Theme.getTheme().applyStyle(StyleID.DEFAULT, this);
		this.setAnchor(PositionAnchor.UPPER_LEFT);

		this.menuModel = model;
		this.mustBeDestroy = true;

		this.applet = applet;

		Vector3D closeButtonPosition = new Vector3D(x + width
				- (iconWidth + 2*getSpacing()), y + getSpacing());
		this.closeButton = createIconButton(closeButtonPosition, "close.png",
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						if (ge instanceof TapEvent) {
							TapEvent tapEvent = (TapEvent) ge;

							if (tapEvent.isTapped()
									&& tapEvent.getTarget() == closeButton) {
								ChoiceEvent choiceEvent = new ChoiceEvent(
										ListMenu.this, null);
								for (ChoiceListener listener : ListMenu.this.listeners)
									listener.choiceCancelled(choiceEvent);
								if (ListMenu.this.mustBeDestroy())
									ListMenu.this.destroy();
							}
						}

						return false;
					}
				});
		this.closeButton.setNoStroke(true);
		addChild(this.closeButton);

		this.backButton = createIconButton(closeButtonPosition,
				"parent-icon.png", new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						if (ge instanceof TapEvent) {
							TapEvent tapEvent = (TapEvent) ge;

							if (tapEvent.isTapped()
									&& tapEvent.getTarget() == backButton) {
								Object parent = menuModel
										.getParentMenu(menuModel
												.getCurrentMenu());
								if (parent != null) {
									menuModel.setCurrentMenu(parent);
									updateList();
								}
							}
						}

						return false;
					}
				});
		addChild(this.backButton);

		this.backButton.setVisible(false);

		this.list = new PickMTList(applet, x + getSpacing(), y
				+ getSpacedIconHeight() + getPathFieldHeight(), width - getSpacingX2(), height
				- getSpacedIconHeight() * 2 - getPathFieldHeight(), 0);
		Theme.getTheme().applyStyle(StyleID.FILL_ONLY, list);
		updateList();
		addChild(this.list);

		list.setFillColor(new MTColor(22, 149, 163, 0)); // list background color
		this.setNoStroke(true);
		this.listeners = new HashSet<ChoiceListener>();
	}

	public ListMenu(PApplet applet, int x, int y, float width, int nbItem,
			Object[] choices) {
		this(applet, x, y, width, nbItem, new DefaultMenuModel(null, choices));
	}

	public ListMenu(PApplet applet, int x, int y, float width, int nbItem,
			Collection<Object> choices) {
		this(applet, x, y, width, nbItem, choices.toArray());
	}

	
	
	/**
	 * @param position
	 * @param imageFilename
	 * @param listener
	 * @return
	 * Construct the button component for a file choice in the list menu 
	 */
	protected MTImageButton createIconButton(Vector3D position, String imageFilename, IGestureEventListener listener) {
		MTImageButton imageButton = new MTImageButton(this.getRenderer(),ImageManager.getInstance().load(imageFilename));
		imageButton.setAnchor(PositionAnchor.UPPER_LEFT);
		imageButton.setWidthXYGlobal(iconWidth);
		imageButton.setHeightXYGlobal(choiceViewHeight);
		imageButton.setPositionGlobal(new Vector3D(position.x, position.y));
		Theme.getTheme().applyStyle(Theme.TRANSPARENT_IMAGE_BUTTON, imageButton);
		imageButton.addGestureListener(TapProcessor.class, listener);

		return imageButton;
	}

	public void updateList() {
		emptyList();

		Object[] choices = menuModel.getChoices(menuModel.getCurrentMenu());

		if (menuModel.onlyStartMenuCanCancel()) {
			if (menuModel.getCurrentMenu() == menuModel.getStartMenu()) {
				closeButton.setVisible(closeButton.isEnabled());
				backButton.setVisible(false);
			} else {
				closeButton.setVisible(false);
				backButton.setVisible(true);
			}
		}

		nbItems = 0;
		if (choices != null) {
			for (final Object choice : choices) {
				nbItems++;
				MTListCell cell = new MTListCell(getRenderer(), getWidthXYGlobal() - getSpacingX2(), choiceViewHeight);
				Theme.getTheme().applyStyle("LIST_CHOICE", cell);
				cell.setUserData(CHOICE, choice);
				cell.addChild(makeChoiceView(choice));
				cell.registerInputProcessor(new TapProcessor(getRenderer()));
				cell.addGestureListener(TapProcessor.class, this);
				//cell.setNoStroke(true);
				
				cell.setStrokeColor(new MTColor(34, 83, 120, 255)); // list element stroke color
				cell.setStrokeWeight(2);

				list.addListElement(cell);
			}
		}
		if (nbItems > 5) {
			this.list.createScrollBar(this.applet, nbItems);
		}
	}

	protected void emptyList() {
		list.removeAllListElements();
		list.removeScrollBar();
		emptyListCells();
	}

	protected void setSelectionList(final FileChooser fc) {
		list.removeAllListElements();
		list.removeScrollBar();

		Object[] choices = menuModel.getChoices(menuModel.getCurrentMenu());

		if (menuModel.onlyStartMenuCanCancel()) {
			if (menuModel.getCurrentMenu() == menuModel.getStartMenu()) {
				closeButton.setVisible(closeButton.isEnabled());
				backButton.setVisible(false);
			} else {
				closeButton.setVisible(false);
				backButton.setVisible(true);
			}
		}

		if (choices != null) {
			nbItems = 0;
			for (final Object choice : choices) {
				if (!((java.io.File) choice).isDirectory()) {
					nbItems++;
					final MTListCell cell = new MTListCell(getRenderer(), getWidthXYGlobal() - getSpacingX2(), choiceViewHeight);
					final MTImageButton checkedIcon = new MTImageButton(this.getRenderer(), ImageManager.getInstance().load("checked-icon.png"));
					checkedIcon.setNoStroke(true);
					// Theme.getTheme().applyStyle("LIST_CHOICE", cell);
					//cell.setFillColor(new MTColor(140, 210, 210, 240));
					cell.setFillColor(new MTColor(22, 149, 163, 255)); // cell color after action-button pushed
					cell.setStrokeColor(new MTColor(34, 83, 120, 255)); // the same that the filechooser background
					cell.setStrokeWeight(2);
					cell.setUserData(CHOICE, choice);
					cell.addChild(makeChoiceView(choice));
					cell.registerInputProcessor(new TapProcessor(getRenderer()));
					cell.addGestureListener(TapProcessor.class,	new IGestureEventListener() {
								public boolean processGestureEvent(MTGestureEvent ge) {
									if (ge instanceof TapEvent) {
										TapEvent tapEvent = (TapEvent) ge;
										
										if (tapEvent.isTapped()) {
											if (listCells
													.contains((File) choice)) {
												listCells.remove((File) choice);
												cell.removeChild(checkedIcon);
												cell.setFillColor(new MTColor(
														22, 149, 163, 255)); // unselected file color (select and unselect a file)
											} else {
												listCells.add((File) choice);
												cell.addChild(checkedIcon);
												checkedIcon.setPositionRelativeToParent(new Vector3D(
														getWidthXYGlobal() -  getSpacedIconWidth()/2 - getSpacing()*2,
														getSpacedIconHeight()/2 - 3));
												cell.setFillColor(new MTColor(
														235, 127, 0, 255)); // selected file color
											}
										}
										System.out.println("Taille liste : "+ listCells.size());

										if (listCells.size() == 0) {
											fc.displayDisabledBottomButtons();
										} else {
											fc.displayBottomButtons();
										}
									}
									return false;
								}
							});
					list.addListElement(cell);
				}
			}
			// Create ScrollBar
			if (nbItems > 4) {
				this.list.createScrollBar(this.applet, nbItems);
			}
		}
	}

	protected void deleteSelectedFiles() {
		for (File f : listCells) {
			f.delete();
		}
	}

	
	/**
	 * @param choice
	 * @return
	 * Create the TextArea to show the name of the file 
	 */
	protected MTComponent makeChoiceView(Object choice) {
		MTTextArea textArea = new MTTextArea(getRenderer(), getSpacing(), 0,getWidthXYGlobal() - getSpacingX2(), choiceViewHeight);
		String sfont = PropertyManager.getInstance().getProperty(PropertyManager.PICK_FONT);
		textArea.setFont(FontManager.getInstance().createFont(getRenderer(),sfont, 20, MTColor.BLACK, true));
		textArea.setText(choice.toString());
		textArea.setNoFill(true);
		textArea.setNoStroke(true);

		return textArea;
	}

	public MTImageButton getBackButton() {
		return backButton;
	}

	public IMenuModel getModel() {
		return menuModel;
	}

	public void setModel(IMenuModel menuModel) {
		this.menuModel = menuModel;
		this.menuModel.setCurrentMenu(menuModel.getStartMenu());
		updateList();
	}

	public void setStart() {
		menuModel.setCurrentMenu(menuModel.getStartMenu());
		updateList();
	}

	public void addChoiceListener(ChoiceListener listener) {
		this.listeners.add(listener);
	}

	public void removeChoiceListener(ChoiceListener listener) {
		this.listeners.remove(listener);
	}

	public boolean mustBeDestroy() {
		return mustBeDestroy;
	}

	/**
	 * @param mustBeDestroy
	 * @uml.property name="mustBeDestroy"
	 */
	public void setMustBeDestroy(boolean mustBeDestroy) {
		this.mustBeDestroy = mustBeDestroy;
	}

	public boolean isCloseVisible() {
		return this.closeButton.isVisible();
	}

	public void setCloseVisible(boolean visible) {
		this.closeButton.setVisible(visible);
		this.closeButton.setEnabled(visible);
	}
	
	//-----------------------------------------------------------
	protected String pathString = "\\server";
	protected  MTTextField pathField;
	//-----------------------------------------------------------
	
	protected void setPath(File choice) {
		//-------------------------------------------------------------------------
		// Max : update of the current path
		if ( ((File)choice).getPath().equals(PropertyManager.getInstance().getProperty(PropertyManager.FILE_PATH) )) {
			pathString = "\\server\\";
		} else {
			System.out.println(((File)choice).getAbsolutePath());
			pathString = "\\server" + ((File)choice).getAbsolutePath().substring(PropertyManager.getInstance().getProperty(PropertyManager.FILE_PATH).length()); // cut the beginning of the path
		}
		if ( pathString.length() > 30 ) {
			pathString = pathString.substring(0, 28) + "...";
		}
		if ( pathField != null ) pathField.setText( pathString );
		//-------------------------------------------------------------------------
	}
	
	public boolean processGestureEvent(MTGestureEvent ge) {
		super.processGestureEvent(ge);
		
		if (m_areCellsEnabled){
			if (ge instanceof TapEvent) {
				if (((TapEvent) ge).isTapped()) {
					MTListCell listCell = (MTListCell) ge.getTarget();
					Object choice = listCell.getUserData(CHOICE);
	
					if (this.menuModel.hasChoices(choice)) {
						//A folder had been tapped
						this.menuModel.setCurrentMenu(choice);
						updateList();
					} else {
						//A file had been tapped
						for (ChoiceListener listener : ListMenu.this.listeners)
							listener.choiceSelected(new ChoiceEvent(ListMenu.this, choice));
						
						//Destroy the list after opening a file
						if (ListMenu.this.mustBeDestroy()){
							this.destroy();
						}
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * @author claude
	 */
	public class ChoiceEvent {
		/**
		 * @uml.property name="listMenu"
		 * @uml.associationEnd
		 */
		private ListMenu listMenu;
		private Object choice;

		public ChoiceEvent(ListMenu menu, Object choice) {
			this.listMenu = menu;
			this.choice = choice;
		}

		/**
		 * @return
		 * @uml.property name="listMenu"
		 */
		public ListMenu getListMenu() {
			return this.listMenu;
		}

		/**
		 * @return
		 * @uml.property name="choice"
		 */
		public String getChoice() {
			if (choice == null)
				return "";
			return this.choice.toString();
		}

		public Object getChoosedObject() {
			Object ret = choice;
			if (choice instanceof DefaultMenuModel)
				ret = ((DefaultMenuModel) choice).getUserObject();
			return ret;
		}
	}
}
