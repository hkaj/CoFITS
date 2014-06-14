package utc.bsfile.model.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import utc.bsfile.model.agent.behaviours.ManageReceiveFile;
import utc.bsfile.model.CofitsModel;
import utc.bsfile.model.agent.behaviours.ReceiveFile;
import utc.bsfile.model.agent.behaviours.ReceiveMessageBehaviour;
import utc.bsfile.model.agent.behaviours.RequestProjectsArchitecture;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

import utc.bsfile.model.agent.behaviours.RequestDownloadFile;
import jade.core.NotFoundException;
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
		
		registerToDF();

		m_model = (CofitsModel) getArguments()[0];	
		m_model.setAgent(this);
		
		addBehaviour(new ReceiveMessageBehaviour(this));
		addBehaviour(new RequestProjectsArchitecture(this));
	}

	@Override
	protected void onGuiEvent(GuiEvent evt) {
		if (evt.getType() == DOWNLOAD_FILE){
			int fileId = (Integer) evt.getParameter(0);
			addBehaviour(new RequestDownloadFile(this, fileId));
		}
	}


	/**
	 * @param message - Agreement message for downloading file
	 * Creates a new Threaded behaviour in order to receive a file
	 */
	public void createNewReceiveFileBehaviour(ACLMessage message) {
		String conversationId = message.getConversationId();
		if (m_manageReceiveFileBehaviours.get(conversationId) == null){
			LinkedBlockingQueue<ACLMessage> newQueue = new LinkedBlockingQueue<ACLMessage>();
			ManageReceiveFile newManageReceiveFileBehav = new ManageReceiveFile(this, message, newQueue);
			
			Behaviour b = m_threadedBehaviourFactory.wrap(newManageReceiveFileBehav); 
			addBehaviour(b);
		} else {
			System.err.println("Behaviour with the conversation id : " + conversationId + " already exists");
		}
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
			m_manageReceiveFileBehaviours.get(conversationId).notifySelf();
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
		m_manageReceiveFileBehaviours.remove(conversationId);
		m_receiveFileMessagesQueues.remove(conversationId);
		
		m_model.fileReceived(receiveFile.getFilename());
		
		//Interrupt the thread
		//TODO Check whether this step is needed or not
//		try {
//			m_threadedBehaviourFactory.interrupt(receiveFile);
//		} catch (NotFoundException e) {
//			System.err.println("Unabled to interrupt receive file thread with conversation id : " + conversationId);
//			e.printStackTrace();
//		}
	}
	
	
	public void addMessageQueue(String conversationId,
			LinkedBlockingQueue<ACLMessage> queue) {
		m_receiveFileMessagesQueues.put(conversationId, queue);
		
	}
	
	
	public void addManageDownloadBehaviour(String conversationId,
			ManageReceiveFile manageReceiveFile) {
		m_manageReceiveFileBehaviours.put(conversationId, manageReceiveFile);		
	}
	
	
	/**
	 * @return The server AID
	 * Search for the server AID in DFAgent
	 */
	public AID searchServer() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		DFAgentDescription[] result = null;
		
		sd.setType("ProjectManagement");
		sd.setName("DocumentService");
		template.addServices(sd);
		
		try {
			result = DFService.search(this, template);
		}catch (FIPAException e) {
			e.printStackTrace();
		}
		
		return result[0].getName();
	}
	
	
	//Getters & Setters
	public CofitsModel getModel() {
		return m_model;
	}
	
	public Map<String, ManageReceiveFile> getManageReceiveFileBehaviours(){
		return m_manageReceiveFileBehaviours;
	}
	
	
	//Members
	private Map<String, ManageReceiveFile> m_manageReceiveFileBehaviours = new HashMap<String, ManageReceiveFile>();
	private Map<String, LinkedBlockingQueue<ACLMessage> > m_receiveFileMessagesQueues = new HashMap<String, LinkedBlockingQueue<ACLMessage> >();
	private ThreadedBehaviourFactory m_threadedBehaviourFactory = new ThreadedBehaviourFactory();
	private CofitsModel m_model;

}
