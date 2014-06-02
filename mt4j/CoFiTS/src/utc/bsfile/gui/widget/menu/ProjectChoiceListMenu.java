package utc.bsfile.gui.widget.menu;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.model.menu.TwoLinkedJsonNode;

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
				
		addChild(m_confirmButton);
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
	
	public final TwoLinkedJsonNode getSelectedNode(){
		return m_selectedNode;
	}
	
	
	public final MTSvgButton getConfirmButton(){
		return m_confirmButton;
	}
	
	public final IMenuModel getMenuModel(){
		return m_menuModel;
	}
	

	protected TwoLinkedJsonNode m_selectedNode;
	//protected MTImageButton m_confirmButton;
	protected MTSvgButton m_confirmButton;
	protected IMenuModel m_menuModel;
}
