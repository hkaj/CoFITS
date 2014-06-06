package utc.bsfile.model.agent.behaviours;

import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import utc.bsfile.model.agent.CofitsGuiAgent;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveFile extends SequentialBehaviour {

	public ReceiveFile(Agent a, ACLMessage message, LinkedBlockingQueue<ACLMessage> queue) {
		super(a);
		m_initialMessage = message;
		//TODO set nb part expected
		m_partsReceivedBehaviours = new Vector<ReceivePartOfFile>();
		m_newMessages = queue;
		m_agent = (CofitsGuiAgent) a;
	}
	
	
	public synchronized void initSequentialBehaviour() {
		while (m_nbPartReceived < m_nbPartExpected){
			//Wait for agent to add new messages in new messages queue
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("Unabled to wait in the Thread managing conversation with id : " + m_initialMessage.getConversationId());
				e.printStackTrace();
			}
			
			//The thread had been activated, a new message is available
			ACLMessage message = m_newMessages.poll();
			
			//TODO replace the index with the part number and filename
			int index = 0;
			String filename = "foo/bar";
			if (m_partsReceivedBehaviours.get(index) == null){
				m_partsReceivedBehaviours.set(index, new ReceivePartOfFile(myAgent, message, filename));
				++m_nbPartReceived;
			}
		}
		
		startBehaviour();		
	}

	
	private void startBehaviour(){
		//Add the sub behaviours
		for(ReceivePartOfFile behaviour : m_partsReceivedBehaviours){
			addSubBehaviour(behaviour);
		}
		
		//Start the sequential behaviour
		myAgent.addBehaviour(this);
	}

	
	public synchronized void notifySelf(){
		notify();
	}

	@Override
	public int onEnd() {
		m_agent.receiveFileBehaviourEnd(this);
		return super.onEnd();
	}
	
	
	//Getters & Setters
	public final String getConversationId(){return m_initialMessage.getConversationId();}
	public final String getFilename(){return "";}	//TODO Set the right filename
	
	//Members
	private ACLMessage m_initialMessage;
	private int m_nbPartReceived = 0;
	private int m_nbPartExpected;
	private Vector<ReceivePartOfFile> m_partsReceivedBehaviours;
	private LinkedBlockingQueue<ACLMessage> m_newMessages = new LinkedBlockingQueue<ACLMessage>();
	private CofitsGuiAgent m_agent;
}
