package utc.bsfile.gui.widget.menu;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
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
		
		m_confirmButton.setPositionRelativeToOther(this, new Vector3D(getWidthXY(TransformSpace.LOCAL) / 2, getHeightXY(TransformSpace.LOCAL) - 25));
		m_confirmButton.setPickable(true);
		m_confirmButton.setEnabled(false);
		
		addChild(m_confirmButton);
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
	
	
	public final TwoLinkedJsonNode getSelectedNode(){
		return m_selectedNode;
	}
	
	
	public final MTImageButton getConfirmButton(){
		return m_confirmButton;
	}
	

	protected TwoLinkedJsonNode m_selectedNode;
	protected MTImageButton m_confirmButton;
}
