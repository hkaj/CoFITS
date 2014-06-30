package uc3;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

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
            sd.setType( "ProjectManagement" );
            dfd.addServices(sd);
            
            DFAgentDescription[] result = DFService.search(myAgent, dfd);
            
            System.out.println(result.length + " results" );
            if (result.length>0){
            	AID name =  result[0].getName();
    			req.addReceiver(name);
    			
    			ObjectMapper mapper = new ObjectMapper();
    			HashMap<String, String> jsonMap = new HashMap<String, String>();
    			StringWriter stringWriter = new StringWriter();

    			jsonMap.put("action", "CREATE_PROJECT");
    			jsonMap.put("login", "aLogin");
    			jsonMap.put("login_to_add", pLogin);
    			jsonMap.put("login_to_add", IsAdmin);
    			jsonMap.put("project_id", pID);
    			

    			try {
    				mapper.writeValue(stringWriter, jsonMap);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}

    			req.setContent(stringWriter.toString());
    			
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
