package utc.bsfile.gui.widget.menu;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

import utc.bsfile.gui.widget.menu.ChoiceListener;
import utc.bsfile.gui.widget.menu.ListMenu;
import utc.bsfile.gui.Theme;

import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.PropertyManager;

public class ProjectChoiceListMenu extends ListMenu implements ChoiceListener {

	public ProjectChoiceListMenu(PApplet applet, int x, int y, float width,
			int nbItem, IMenuModel model) {
		super(applet, x, y, width, nbItem, model);
		
		addChoiceListener(this);
		
		m_menuModel = model;

		//Add a confirmation button
		m_confirmButton = new MTSvgButton(applet, MT4jSettings.getInstance().getDefaultSVGPath() + "KeybValidate-green.svg");
		int buttonSize = 40;
		int spacing = 10;
		m_confirmButton.setSizeXYGlobal(buttonSize, buttonSize);
		m_confirmButton.setPositionGlobal(new Vector3D(x+getWidthXY(TransformSpace.LOCAL)/2, y+getHeightXY(TransformSpace.LOCAL) - buttonSize/2 - spacing));
		m_confirmButton.setPickable(true);
		m_confirmButton.setVisible(false);
		

		m_pathArea = new MTTextArea(applet);
		Vector3D pathAreaPosition = new Vector3D(x + getSpacing() + 3, y + getSpacingX2() + getSpacedIconHeight() );
		m_pathArea.setPositionGlobal(pathAreaPosition);
		String sfont = PropertyManager.getInstance().getProperty(PropertyManager.PICK_FONT);
		m_pathArea.setFont(FontManager.getInstance().createFont(getRenderer(),sfont, 16, MTColor.BLACK, true));
		m_pathArea.setText("Projects and sessions");
		m_pathArea.removeAllGestureEventListeners();
		m_pathArea.setNoFill(true);
		m_pathArea.setNoStroke(true);
		m_pathArea.setFontColor(new MTColor(255, 255, 255, 255));
		
		addChild(m_pathArea);

		//Add a connexion button
		m_launchAgentsButton = new MTSvgButton(applet, MT4jSettings.getInstance().getDefaultSVGPath() + "connect.svg");
		m_launchAgentsButton.setPositionGlobal(new Vector3D(x + getSpacing() + 40 + getSpacing()+5 + iconWidth/2, y + getSpacing() + iconHeight/2));
		m_launchAgentsButton.setSizeXYGlobal(iconWidth,  iconHeight);
		
		addChild(m_confirmButton);
		addChild(m_launchAgentsButton);
	}
	
	@Override
	public void downLoadFile(MTGestureEvent ge) { // ne t�l�charge pas le fichier... (ne pas toucher au nom anyway)
		// change the color of the selected cell
		for (MTListCell cell : list.getListCellContainer().getCells()) {
			cell.setFillColor(Theme.ITEM_LIGHT_COLOR);
			if (cell.equals(ge.getTarget())) ((MTListCell)ge.getTarget()).setFillColor(Theme.ACTIVE_COLOR);
		}
	}

	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		m_selectedNode = (TwoLinkedJsonNode) choiceEvent.getChoosedObject();
		m_confirmButton.setVisible(true);
	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
		m_selectedNode = null;
		m_confirmButton.setVisible(false);		
	}
	
	//Getters
	public final TwoLinkedJsonNode getSelectedNode(){
		return m_selectedNode;
	}
	
	
	public final MTSvgButton getConfirmButton(){
		return m_confirmButton;
	}
	
	public final IMenuModel getMenuModel(){
		return m_menuModel;
	}
	
	public final MTTextArea getPathArea(){
		return m_pathArea;
	}
	
	public final MTSvgButton getLaunchAgentsButton(){
		return m_launchAgentsButton;
	}
	
	public void changeModel(IMenuModel model) {
		setModel(model);
		m_selectedNode = null;
	}


	//Members
	protected TwoLinkedJsonNode m_selectedNode;
	protected MTSvgButton m_confirmButton;
	protected IMenuModel m_menuModel;
	protected MTTextArea m_pathArea;
	protected MTSvgButton m_launchAgentsButton;
}
