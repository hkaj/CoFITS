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

public class CreateAProjectBehaviour extends OneShotBehaviour{

	String pName;
	String pDesc;
	
	public CreateAProjectBehaviour(String name, String desc){
		pName = name;
		pDesc = desc;
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
    			jsonMap.put("project_name", pName);
    			jsonMap.put("description", pDesc);

    			try {
    				mapper.writeValue(stringWriter, jsonMap);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}

    			req.setContent(stringWriter.toString());
    			System.out.println("message : "+ req);
    			System.out.println("contenu avec jackson: " + stringWriter.toString());
    			    			
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
