package utc.bsfile.model.agent.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveMessageBehaviour extends CyclicBehaviour {

	public ReceiveMessageBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage messageReceived = myAgent.receive();
		
		if (messageReceived != null){
			
			if (messageReceived.getPerformative() == ACLMessage.INFORM){
				myAgent.addBehaviour(new UpdateProjectsStructure(myAgent, messageReceived));
			}
			
		} else {
			block();
		}

	}

}
