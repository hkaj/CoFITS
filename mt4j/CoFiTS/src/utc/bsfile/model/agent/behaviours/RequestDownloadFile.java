package utc.bsfile.model.agent.behaviours;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import utc.bsfile.model.agent.CofitsGuiAgent;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestDownloadFile extends OneShotBehaviour {

	public RequestDownloadFile(Agent a, String projectId, int sessionId, int fileId) {
		super(a);
		m_fileId = fileId;
		m_sessionId = sessionId;
		m_projectId = projectId;
		m_agent = (CofitsGuiAgent) a;
	}

	@Override
	public void action() {
		ACLMessage messageToSend = new ACLMessage(ACLMessage.REQUEST);	//TODO Change the performative or add information
		
		//Send to the server
		messageToSend.addReceiver(m_agent.searchServer());
		messageToSend.setSender(myAgent.getAID());
		
		//Set the conversation id
		String conversationId = myAgent.getName() + "_" + m_fileId;
		messageToSend.setConversationId(conversationId);

		//Set json content
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> jsonMap = new HashMap<String, String>();
		StringWriter stringWriter = new StringWriter();
		
		jsonMap.put("action", "DOWNLOAD_FILE");
		jsonMap.put("file_id", Integer.toString(m_fileId));
		jsonMap.put("session_id", Integer.toString(m_sessionId));
		jsonMap.put("project_id", m_projectId);
		jsonMap.put("login", "TATIN");
		
		System.out.println("Project id : " + m_projectId);
		System.out.println("Session id : " + m_sessionId);
		System.out.println("File id : " + m_fileId);
		
		try {
			mapper.writeValue(stringWriter, jsonMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		messageToSend.setContent(stringWriter.toString());
		System.out.println("THERE !!!!!!!!!!! " + messageToSend.getConversationId());
		
		myAgent.send(messageToSend);
	}

	
	//Members
	private int m_fileId;
	private int m_sessionId;
	private String m_projectId;
	private CofitsGuiAgent m_agent;
}
