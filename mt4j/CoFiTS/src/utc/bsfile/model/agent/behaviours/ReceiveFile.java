package utc.bsfile.model.agent.behaviours;

import utc.bsfile.model.agent.CofitsGuiAgent;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

public class ReceiveFile extends SequentialBehaviour {

	public ReceiveFile(Agent a, String conversationId, String filename) {
		super(a);
		m_agent = (CofitsGuiAgent) a;
		m_conversationId = conversationId;
		m_filename = filename;
	}

	@Override
	public int onEnd() {
		m_agent.receiveFileBehaviourEnd(this);
		return super.onEnd();
	}
	
	//Getters
	public String getConversationId() {return m_conversationId;}
	public String getFilename() {return m_filename;}
		
	//Members
	private CofitsGuiAgent m_agent;
	private String m_conversationId;
	private String m_filename;
}
