package utc.bsfile.model.agent.behaviours;

import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.databind.JsonNode;

import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.util.JsonManager;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ManageReceiveFile extends OneShotBehaviour {

	public ManageReceiveFile(Agent a, ACLMessage message, LinkedBlockingQueue<ACLMessage> queue) {
		m_agent = (CofitsGuiAgent) a;
		m_initialMessage = message;
		
		JsonNode jsonNode = JsonManager.getInstance().createJsonNode(m_initialMessage.getContent());
		
		m_nbPartExpected = jsonNode.path("number_of_messages").asInt();
		m_filename = "foo";	//TODO set the right filename from file id
		
		m_partsReceivedBehaviours = new Vector<ReceivePartOfFile>(m_nbPartExpected);
		m_newMessages = queue;
		m_agent = (CofitsGuiAgent) a;
	}
	
	
	public synchronized void initSequentialBehaviour() {
				
	}

	
	private void startReceiveFileBehaviour(){
		//Create the ReceiveFileBehaviour
		m_receiveFileBehaviour = new ReceiveFile(myAgent, getConversationId(), m_filename);
		
		//Add the sub behaviours
		for(ReceivePartOfFile behaviour : m_partsReceivedBehaviours){
			m_receiveFileBehaviour.addSubBehaviour(behaviour);
		}
		
		//Start the sequential behaviour
		myAgent.addBehaviour(m_receiveFileBehaviour);
	}
	
	@Override
	public void action() {
		m_agent.addMessageQueue(getConversationId(), m_newMessages);
		m_agent.addManageDownloadBehaviour(getConversationId(), this);
		
		while (m_nbPartReceived < m_nbPartExpected){
			//Wait for agent to add new messages in new messages queue
			waitSelf();
			
			//The thread had been activated, a new message is available
			ACLMessage message = m_newMessages.poll();
			int index = Integer.parseInt(message.getEnvelope().getComments()) - 1;
			m_partsReceivedBehaviours.add(index, new ReceivePartOfFile(myAgent, message, m_filename));
			++m_nbPartReceived;
		}
		
		startReceiveFileBehaviour();
	}


	/**
	 * 
	 */
	private synchronized void waitSelf() {
		try {
			wait();
		} catch (InterruptedException e) {
			System.err.println("Unabled to wait in the Thread managing conversation with id : " + m_initialMessage.getConversationId());
			e.printStackTrace();
		}
	}
	
	public synchronized void notifySelf(){
		notify();
	}
	
	//Getters & Setters
	public final String getConversationId(){return m_initialMessage.getConversationId();}
	public final String getFilename(){return m_filename;}	//TODO Set the right filename
	
	//Members
	private ACLMessage m_initialMessage;
	private int m_nbPartReceived = 0;
	private int m_nbPartExpected;
	private String m_filename;
	private Vector<ReceivePartOfFile> m_partsReceivedBehaviours;
	private LinkedBlockingQueue<ACLMessage> m_newMessages = new LinkedBlockingQueue<ACLMessage>();
	private CofitsGuiAgent m_agent;
	private ReceiveFile m_receiveFileBehaviour;

}
