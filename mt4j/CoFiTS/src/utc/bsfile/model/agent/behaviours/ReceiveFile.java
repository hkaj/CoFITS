package utc.bsfile.model.agent.behaviours;

import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveFile extends SequentialBehaviour {

	public ReceiveFile(Agent a, ACLMessage message) {
		super(a);
		m_initialMessage = message;
		//TODO set nb part expected
		m_partsReceivedBehaviours = new Vector<ReceivePartOfFile>();
		
		initSequentialBehaviour();
	}
	
	
	private void initSequentialBehaviour() {
		while (m_nbPartReceived < m_nbPartExpected){
			//TODO wait for agent to add new messages in new Messages
			
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


	//Members
	private ACLMessage m_initialMessage;
	private int m_nbPartReceived = 0;
	private int m_nbPartExpected;
	private Vector<ReceivePartOfFile> m_partsReceivedBehaviours;
	private LinkedBlockingQueue<ACLMessage> m_newMessages = new LinkedBlockingQueue<ACLMessage>();
}
