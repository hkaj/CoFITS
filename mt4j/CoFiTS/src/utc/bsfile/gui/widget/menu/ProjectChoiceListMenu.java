package utc.bsfile.gui.widget.menu;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

import utc.bsfile.gui.widget.menu.ChoiceListener;
import utc.bsfile.gui.widget.menu.ListMenu;
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.ImageManager;
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
		System.out.println("LA POLISSE : "+sfont);
		m_pathArea.setFont(FontManager.getInstance().createFont(getRenderer(),sfont, 16, MTColor.BLACK, true));
		m_pathArea.setText("Projects and sessions");
		m_pathArea.removeAllGestureEventListeners();
		m_pathArea.setNoFill(true);
		m_pathArea.setNoStroke(true);
		m_pathArea.setFontColor(new MTColor(255, 255, 255, 255));
		
		addChild(m_pathArea);

		//Add a connexion button
		m_launchAgentsButton = new MTImageButton(applet, ImageManager.getInstance().load("connect-icon.png"));
		
		m_launchAgentsButton.setPositionGlobal(new Vector3D(getPosition(TransformSpace.GLOBAL).x, getPosition(TransformSpace.GLOBAL).y));
		m_launchAgentsButton.setPickable(true);
		m_launchAgentsButton.setEnabled(true);
		
		addChild(m_confirmButton);
		addChild(m_launchAgentsButton);
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
	
	public final MTImageButton getLaunchAgentsButton(){
		return m_launchAgentsButton;
	}


	//Members
	protected TwoLinkedJsonNode m_selectedNode;
	protected MTSvgButton m_confirmButton;
	protected IMenuModel m_menuModel;
	protected MTTextArea m_pathArea;
	protected MTImageButton m_launchAgentsButton;
}
