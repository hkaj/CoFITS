package utc.bsfile.model.agent.behaviours;

import utc.bsfile.model.agent.CofitsGuiAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveMessageBehaviour extends CyclicBehaviour {

	public ReceiveMessageBehaviour(Agent a) {
		super(a);
		m_agent = (CofitsGuiAgent) a;
	}

	@Override
	public void action() {
		ACLMessage messageReceived = myAgent.receive();
		
		if (messageReceived != null){
			
			if (messageReceived.getPerformative() == ACLMessage.INFORM){
				myAgent.addBehaviour(new UpdateProjectsStructure(myAgent, messageReceived));
			} else if (messageReceived.getPerformative() == ACLMessage.AGREE){
				m_agent.createNewReceiveFileBehaviour(messageReceived);
			} else if (messageReceived.getPerformative() == ACLMessage.INFORM){	//TODO change performative or add information test
				m_agent.propagatePartFileMessage(messageReceived);
			}
			
		} else {
			block();
		}

	}

	//Members
	private CofitsGuiAgent m_agent;
}
