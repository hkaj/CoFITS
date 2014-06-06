package utc.bsfile.model.agent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import utc.bsfile.main.CofitsDesignScene;
import utc.bsfile.model.agent.action.SelectPickAction;
import utc.bsfile.model.agent.behaviours.ReceiveFile;
import utc.bsfile.model.agent.behaviours.ReceiveMessageBehaviour;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

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

	
	/**
	 * @param message - Agreement message for downloading file
	 * Creates a new Threaded behaviour in order to receive a file
	 */
	public void createNewReceiveFileBehaviour(ACLMessage message) {
		String conversationId = message.getConversationId();
		if (m_receiveFileBehaviours.get(conversationId) == null){
			LinkedBlockingQueue<ACLMessage> newQueue = new LinkedBlockingQueue<ACLMessage>();
			ReceiveFile newReceiveFileBehav = new ReceiveFile(this, message, newQueue);
			m_receiveFileBehaviours.put(conversationId, newReceiveFileBehav);
			m_receiveFileMessagesQueues.put(conversationId, newQueue);
		} else {
			System.err.println("Behaviour with the conversation id : " + conversationId + " already exists");
		}
	}
	
	
	/**
	 * @param message - Message with the part of a file
	 * Send a message with a file part to the right threaded behaviour
	 */
	public synchronized void propagatePartFileMessage(ACLMessage message) {
		String conversationId = message.getConversationId();
		LinkedBlockingQueue<ACLMessage> queue = m_receiveFileMessagesQueues.get(conversationId); 
		if(queue != null){
			try {
				queue.put(message);
			} catch (InterruptedException e) {
				System.err.println("Unabled to add new message in queue of conversation id receive file behaviour : " + conversationId);
				e.printStackTrace();
			}
			//TODO wake the thread up
		} else {
			System.err.println("Agreement message might have not been received yet for conversation id : " + conversationId);
			putBack(message);
		}
		
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
	private Map<String, ReceiveFile> m_receiveFileBehaviours = new HashMap<String, ReceiveFile>();
	private Map<String, LinkedBlockingQueue<ACLMessage> > m_receiveFileMessagesQueues = new HashMap<String, LinkedBlockingQueue<ACLMessage> >();  

}
