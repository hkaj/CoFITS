package uc1;

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

public class RemoveAProjectBehaviour extends OneShotBehaviour{

	String pID;
	
	public RemoveAProjectBehaviour(String id){
		pID = id;
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

    			jsonMap.put("action", "REMOVE_PROJECT");
    			jsonMap.put("login", "aLogin");
    			jsonMap.put("project_id", pID);

    			try {
    				mapper.writeValue(stringWriter, jsonMap);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}

    			req.setContent(stringWriter.toString());
    			
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
