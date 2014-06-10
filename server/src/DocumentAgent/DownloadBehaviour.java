package DocumentAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import Constants.RequestConstants;
import ModelObjects.BinaryContent;
import ModelObjects.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class DownloadBehaviour extends OneShotBehaviour {
	final HashMap<String, String> request;
	final ACLMessage message;

	public DownloadBehaviour(HashMap<String, String> request, ACLMessage message) {
		this.request = request;
		this.message = message;
	}

	public DownloadBehaviour(Agent a, HashMap<String, String> request,
			ACLMessage message) {
		super(a);
		this.request = request;
		this.message = message;
	}

	@Override
	public void action() {
		Document document = new Document(Integer.parseInt(this.request
				.get("file_id")), this.request.get("login"));
		Path path = Paths.get(RequestConstants.documentAgentDirectory
				+ document.getName() + (document.getType().equals("") ? "" : "." + document.getType()));
		
		// Send a confirmation message
		ACLMessage reply = this.message.createReply();
		reply.setPerformative(ACLMessage.AGREE);

		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> jsonMap = new HashMap<String, String>();
		StringWriter stringWriter = new StringWriter();

		jsonMap.put("action", "DOWNLOAD_FILE");
		jsonMap.put("file_id", this.request.get("file_id"));
		jsonMap.put("number_of_message", "1");
		Envelope envelop = new Envelope();
		envelop.setComments("1");

		try {
			mapper.writeValue(stringWriter, jsonMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		reply.setContent(stringWriter.toString());
		this.myAgent.send(reply);

		// Actually send the file
		reply = this.message.createReply();
		BinaryContent res = null;
		try {
			res = new BinaryContent(Files.readAllBytes(path));
			reply.setByteSequenceContent(res.getContent());
			reply.setEnvelope(envelop);
		} catch (IOException e) {
			e.printStackTrace();
		}
		reply.setPerformative(ACLMessage.INFORM);
		this.myAgent.send(reply);
	}
}
