package uc3;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class AddAUserBehaviour extends OneShotBehaviour{
	
	String pLogin;
	String IsAdmin;
	String pID;
	
	public AddAUserBehaviour(String login, String admin, String ID){
		pLogin = login;
		IsAdmin = admin;
		pID = ID;
	}

	@Override
	public void action() {
		System.out.println( myAgent.getLocalName()+ "  in Behaviour : " + this.getBehaviourName());

		
		ACLMessage req = new ACLMessage(ACLMessage.REQUEST);

		try {
			
			DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd  = new ServiceDescription();
            sd.setType( "Server" );
            dfd.addServices(sd);
            
            DFAgentDescription[] result = DFService.search(myAgent, dfd);
            
            System.out.println(result.length + " results" );
            if (result.length>0){
            	AID name =  result[0].getName();
    			req.addReceiver(name);
    			String content = "";
    			content = "{ \"action\": \"CREATE_PROJECT\", \"my_login\" : \"" + "myLogin" + " \", \"login_to_add\" : \""+"theUser" + "\", \"admin\" : \""+ IsAdmin+ "\", \"project_id\" : \""+ pID+"\"}";
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
