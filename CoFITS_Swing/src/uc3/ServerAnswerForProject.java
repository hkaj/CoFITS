package uc3;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ServerAnswerForProject extends CyclicBehaviour {

	@Override
	public void action() {
		System.out.println( myAgent.getLocalName()+ "  in Behaviour : " + this.getBehaviourName());
		
		ACLMessage message = myAgent.receive();
		if (message != null){
			try {
				String ans = message.getContent();
				System.out.println(ans);
				
				ACLMessage req = new ACLMessage(ACLMessage.CONFIRM);
				req.addReceiver(message.getSender());
				String content = "";
				content = "Done";
				req.setContent(content);
				myAgent.send(req);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else 
			 block();

	}
	

}
