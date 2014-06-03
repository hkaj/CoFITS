package utc.bsfile.model.agent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import utc.bsfile.main.CofitsDesignScene;
import utc.bsfile.model.agent.action.SelectPickAction;
import utc.bsfile.model.agent.behaviours.ReceiveMessageBehaviour;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class CofitsGuiAgent extends GuiAgent {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();
		CofitsDesignScene scene = (CofitsDesignScene) getArguments()[0];
		addPropertyChangeListener(scene);
		
		addBehaviour(new LaunchPickBehaviour(scene));
		addBehaviour(new ReceiveMessageBehaviour(this));
		
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {

	}

	private class LaunchPickBehaviour extends OneShotBehaviour {

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		public LaunchPickBehaviour(CofitsDesignScene scene) {
			m_scene = scene;
		}

		@Override
		public void action() {
			m_scene.getMTApplication().registerPreDrawAction(
					new SelectPickAction(m_pcs));
		}
		
		CofitsDesignScene m_scene;

	}
	
	
	//Property Change Support methods
	public void addPropertyChangeListener(PropertyChangeListener listener){
		m_pcs.addPropertyChangeListener(listener);
	}
	
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		m_pcs.removePropertyChangeListener(listener);
	}
	
	
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue){
		m_pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	
	//Getters & Setters
	public TwoLinkedJsonNode getProjectsArchitectureRootNode() {return m_projectsArchitectureRootNode;}
	public void setProjectsArchitectureRootNode(TwoLinkedJsonNode node) {
		TwoLinkedJsonNode oldValue = m_projectsArchitectureRootNode;
		m_projectsArchitectureRootNode = node;
		firePropertyChange("projectsArchitectureRootNode changed", oldValue, m_projectsArchitectureRootNode);		
	}
	
	
	//Members
	private PropertyChangeSupport m_pcs = new PropertyChangeSupport(this);
	private TwoLinkedJsonNode m_projectsArchitectureRootNode;
}
