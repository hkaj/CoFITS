package utc.bsfile.model.agent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import utc.bsfile.main.CofitsDesignScene;
import utc.bsfile.model.agent.action.SelectPickAction;
import utc.bsfile.model.agent.behaviours.ReceiveMessageBehaviour;
import utc.bsfile.model.agent.behaviours.RequestProjectsStructure;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class CofitsGuiAgent extends GuiAgent {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();
		CofitsDesignScene scene = (CofitsDesignScene) getArguments()[0];
		addPropertyChangeListener(scene);
		
		registerToDF();
		
		addBehaviour(new LaunchPickBehaviour(scene));
		addBehaviour(new ReceiveMessageBehaviour(this));
		addBehaviour(new RequestProjectsStructure(this));
		
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
	
	
	/**
	 * 	The agent registers to the AMS
	 */
	private void registerToDF() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("table");
		sd.setName("TATIN");
		
		dfd.addServices(sd);
		
		try{
			DFService.register(this, dfd);
		} catch (FIPAException e){
			e.printStackTrace();
		}
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
	
	
	/**
	 +	 * @return The server AID
	 +	 * Search for the server AID in DFAgent
	 +	 */
	 public AID searchServer() {
	 	DFAgentDescription template = new DFAgentDescription();
	 	ServiceDescription sd = new ServiceDescription();
	 	DFAgentDescription[] result = null;
	 	
	 	sd.setType("ReceiverAgent");
	 	sd.setName("receiver");
	 	template.addServices(sd);
	 	
	 	try {
	 		result = DFService.search(this, template);
	 	}catch (FIPAException e) {
	 		e.printStackTrace();
	 	}
	 	
	 	return result[0].getName();
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
