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
import utc.bsfile.model.agent.behaviours.RequestDownloadFile;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import jade.core.AID;
import jade.core.NotFoundException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class CofitsGuiAgent extends GuiAgent {
	
	private static final long serialVersionUID = 1L;
	public static final int DOWNLOAD_FILE = 1;

	@Override
	protected void setup() {
		super.setup();
		CofitsDesignScene scene = (CofitsDesignScene) getArguments()[0];
		addPropertyChangeListener(scene);
		
		firePropertyChange("Agent Created", null, this);
		
		addBehaviour(new LaunchPickBehaviour(scene));
		addBehaviour(new ReceiveMessageBehaviour(this));
		
	}

	@Override
	protected void onGuiEvent(GuiEvent evt) {
		if (evt.getType() == DOWNLOAD_FILE){
			int fileId = (Integer) evt.getParameter(0);
			addBehaviour(new RequestDownloadFile(this, fileId));
		}
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
			
			newReceiveFileBehav = (ReceiveFile) m_threadedBehaviourFactory.wrap(newReceiveFileBehav); 
			m_receiveFileBehaviours.put(conversationId, newReceiveFileBehav);
			m_receiveFileMessagesQueues.put(conversationId, newQueue);
			newReceiveFileBehav.initSequentialBehaviour();
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

			//Wake up the behaviour
			m_receiveFileBehaviours.get(conversationId).notifySelf();
		} else {
			System.err.println("Agreement message might have not been received yet for conversation id : " + conversationId);
			putBack(message);
		}
		
	}
	
	
	/**
	 * @param receiveFile - ReceiveFile behaviour which ends
	 * Remove every reference of the behaviour and interrupt its thread.
	 */
	public void receiveFileBehaviourEnd(ReceiveFile receiveFile) {
		String conversationId = receiveFile.getConversationId();
		m_receiveFileBehaviours.remove(conversationId);
		m_receiveFileMessagesQueues.remove(conversationId);
		
		firePropertyChange("File Received", null, receiveFile.getFilename());
		
		//Interrupt the thread
		//TODO Check whether this step is needed or not
		try {
			m_threadedBehaviourFactory.interrupt(receiveFile);
		} catch (NotFoundException e) {
			System.err.println("Unabled to interrupt receive file thread with conversation id : " + conversationId);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return The server AID
	 * Search for the server AID in DFAgent
	 */
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
	private Map<String, ReceiveFile> m_receiveFileBehaviours = new HashMap<String, ReceiveFile>();
	private Map<String, LinkedBlockingQueue<ACLMessage> > m_receiveFileMessagesQueues = new HashMap<String, LinkedBlockingQueue<ACLMessage> >();
	private ThreadedBehaviourFactory m_threadedBehaviourFactory = new ThreadedBehaviourFactory();

}
