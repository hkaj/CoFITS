package main;


import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Acknowledgement extends CyclicBehaviour {

	
	
	@Override
	public void action() {
		System.out.println( myAgent.getLocalName()+ "  in Behaviour : " + this.getBehaviourName());
		
		
		MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
		ACLMessage confirm = myAgent.receive(mt1);

		
		MessageTemplate mt2 = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
		ACLMessage refuse = myAgent.receive(mt2);
		
		if (confirm != null || refuse != null){
			if(confirm != null)
				try {
					System.out.println("confirm");
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}	
				else if(refuse != null)
					try {
						System.out.println("refuse");
						
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
		} else block();
		
	}

}
