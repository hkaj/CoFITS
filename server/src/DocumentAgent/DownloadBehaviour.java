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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import Constants.DataBaseConstants;
import Constants.RequestConstants;
import ModelObjects.BinaryContent;
import ModelObjects.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class DownloadBehaviour extends OneShotBehaviour {
	private final HashMap<String, String> request;
	private final ACLMessage message;

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
		int fileId = Integer.parseInt(this.request.get("file_id"));
		Document document = new Document(fileId);
		String project_id = document.getProject();
		int session_id = document.getSession();
		
		Path path = Paths.get(RequestConstants.documentAgentDirectory,
				project_id, String.valueOf(session_id), document.getName());

		// Send a confirmation message
		ACLMessage reply = this.message.createReply();
		reply.setConversationId(this.message.getConversationId());
		reply.setPerformative(ACLMessage.AGREE);

		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> jsonMap = new HashMap<String, String>();
		StringWriter stringWriter = new StringWriter();

		jsonMap.put("action", "DOWNLOAD_FILE");
		jsonMap.put("file_id", this.request.get("file_id"));
		jsonMap.put("number_of_messages", "1");
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
			reply.setPerformative(ACLMessage.INFORM);
			this.myAgent.send(reply);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Connection createConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(
				"jdbc:postgresql://" + DataBaseConstants.host + "/"
						+ DataBaseConstants.databaseName,
				DataBaseConstants.userName, DataBaseConstants.password);
		return conn;
	}
}
