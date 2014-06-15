package utc.bsfile.model.agent.behaviours;

import com.fasterxml.jackson.databind.JsonNode;

import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.util.JsonManager;
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
				manageInformMessage(messageReceived);
			} else if (messageReceived.getPerformative() == ACLMessage.AGREE){
				m_agent.createNewReceiveFileBehaviour(messageReceived);
			} else {
				System.err.println("Unexpected message !");
			}
		} else {
			block();
		}

	}

	private void manageInformMessage(ACLMessage message) {
		if (message.getConversationId() != null && m_agent.hasReceiveFileBehaviour(message.getConversationId())){
			//The message is about a part of file to receive
			m_agent.propagatePartFileMessage(message);
		} else {
			JsonNode messageContentNode = JsonManager.getInstance().createJsonNode(message.getContent());
			System.out.println(messageContentNode);
			if (messageContentNode.path("action").asText().equals("LIST")) {
				System.out.println("received");
				myAgent.addBehaviour(new UpdateProjectsStructure(myAgent, message));
			} else if (messageContentNode.path("action").asText().equals("LIST_PROJECT")) {
				m_agent.addBehaviour(new UpdateProject(myAgent, message));
			}			
		}
		
	}

	//Members
	private CofitsGuiAgent m_agent;
}
