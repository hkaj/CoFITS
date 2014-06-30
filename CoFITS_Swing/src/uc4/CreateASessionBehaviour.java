package uc4;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class CreateASessionBehaviour extends OneShotBehaviour{

	String pID;
	
	public CreateASessionBehaviour(String ID){
		pID = ID;
	}
	
	@Override
	public void action() {
		System.out.println( myAgent.getLocalName()+ "  in Behaviour : " + this.getBehaviourName());

		
		ACLMessage req = new ACLMessage(ACLMessage.REQUEST);

		try {
			
			DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd  = new ServiceDescription();
            sd.setType( "ProjectManagement" );
            dfd.addServices(sd);
            
            DFAgentDescription[] result = DFService.search(myAgent, dfd);
            
            System.out.println(result.length + " results" );
            if (result.length>0){
            	AID name =  result[0].getName();
    			req.addReceiver(name);
    			String content = "";
    			content = "{ \"action\": \"CREATE_SESSION\", \"login\" : \"" + "aLogin" + " \", \"project_id\" : \""+ pID +"\", \"date\" :"+ String.valueOf(System.currentTimeMillis()) + "\"}";
    			req.setContent(content);
    			
    			myAgent.send(req);
                
            }
            else {
            	System.out.println("no result");
            }
			
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
	}

	
	
}
