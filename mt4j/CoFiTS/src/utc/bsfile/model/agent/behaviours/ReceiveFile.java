package utc.bsfile.model.agent.behaviours;

import utc.bsfile.model.agent.CofitsGuiAgent;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

public class ReceiveFile extends SequentialBehaviour {

	public ReceiveFile(Agent a, String conversationId, int id) {
		super(a);
		m_agent = (CofitsGuiAgent) a;
		m_conversationId = conversationId;
		m_id = id;
	}

	@Override
	public int onEnd() {
		m_agent.receiveFileBehaviourEnd(this);
		return super.onEnd();
	}
	
	//Getters
	public String getConversationId() {return m_conversationId;}
	public int getId() {return m_id;}
		
	//Members
	private CofitsGuiAgent m_agent;
	private String m_conversationId;
	private int m_id;
}
