package utc.bsfile.gui.widget.menu;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.bsfile.main.StartCofitsEntities;
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.ImageManager;

public class ProjectChoiceListMenu extends ListMenu implements ChoiceListener {

	public ProjectChoiceListMenu(PApplet applet, int x, int y, float width,
			int nbItem, IMenuModel model) {
		super(applet, x, y, width, nbItem, model);
		
		addChoiceListener(this);
		
		//Add a confirmation button
		m_confirmButton = new MTImageButton(applet, ImageManager.getInstance().load("confirm-button.png"));
		
		m_confirmButton.setPositionGlobal(new Vector3D(getPosition(TransformSpace.GLOBAL).x + getWidthXY(TransformSpace.LOCAL) / 2, getPosition(TransformSpace.GLOBAL).y + getHeightXY(TransformSpace.LOCAL) - 25));
		m_confirmButton.setPickable(true);
		m_confirmButton.setEnabled(false);
		
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
		m_confirmButton.setEnabled(true);		
	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
		m_selectedNode = null;
		m_confirmButton.setEnabled(false);		
	}
	
	//Getters
	
	public final TwoLinkedJsonNode getSelectedNode(){
		return m_selectedNode;
	}
	
	
	public final MTImageButton getConfirmButton(){
		return m_confirmButton;
	}
	
	public final MTImageButton getLaunchAgentsButton(){
		return m_launchAgentsButton;
	}

	protected TwoLinkedJsonNode m_selectedNode;
	protected MTImageButton m_confirmButton;
	protected MTImageButton m_launchAgentsButton;
}
