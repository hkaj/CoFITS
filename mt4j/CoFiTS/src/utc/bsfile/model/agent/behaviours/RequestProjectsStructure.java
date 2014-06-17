package utc.bsfile.model.agent.behaviours;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import utc.bsfile.model.agent.CofitsGuiAgent;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestProjectsStructure extends OneShotBehaviour {

	public RequestProjectsStructure(Agent a) {
		super(a);
		m_agent = (CofitsGuiAgent) a;		
	}

	@Override
	public void action() {
		ACLMessage messageToSend = new ACLMessage(ACLMessage.REQUEST);	//TODO Change the performative or add information

		//Send to the server
		messageToSend.addReceiver(m_agent.searchServer());
		messageToSend.setSender(myAgent.getAID());

		//Set json content
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> jsonMap = new HashMap<String, String>();
		StringWriter stringWriter = new StringWriter();

		jsonMap.put("action", "LIST");
		jsonMap.put("login", "TATIN");

		try {
			mapper.writeValue(stringWriter, jsonMap);
		} catch (IOException e) {
			e.printStackTrace();
		}

		messageToSend.setContent(stringWriter.toString());

		myAgent.send(messageToSend);

	}
	
	//Members
	private CofitsGuiAgent m_agent;

}
