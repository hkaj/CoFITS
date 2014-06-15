package DocumentAgent;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import Constants.DataBaseConstants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RemoveSessionBehaviour extends OneShotBehaviour {
	private HashMap<String, String> request;
	private ACLMessage message;

	public RemoveSessionBehaviour(HashMap<String, String> req, ACLMessage msg) {
		this.request = req;
		this.message = msg;
	}
	
	@Override
	public void action() {
		ArrayList<Integer> files = null;
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> content = null;
		String login = request.get("login");
		String proj = request.get("project_id");
		String session = request.get("session_id");
		String getFilesReq = "SELECT document FROM mobilizedin WHERE session='"
				+ session + "';";
		Connection conn = null;
		Statement s = null;
		ResultSet res = null;
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			res = s.executeQuery(getFilesReq);
			files = new ArrayList<Integer>();
			while (res.next()) {
				files.add(res.getInt("document"));
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		RemoveSessionSeqBehaviour removeSessionSeq = new RemoveSessionSeqBehaviour();
		for (int file : files) {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			mapper = new ObjectMapper();
			content = new HashMap<String, String>();
			content.put("action", "REMOVE_FILE");
			content.put("login", login);
			content.put("project_id", proj);
			content.put("session_id", session);
			content.put("file_id", String.valueOf(file));
			try {
				msg.setContent(mapper.writeValueAsString(content));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			msg.setSender(this.message.getSender());
			removeSessionSeq.addSubBehaviour(new RemoveFileBehaviour(content,
					msg));
		}
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(this.message.getSender());
		mapper = new ObjectMapper();
		content = new HashMap<String, String>();
		content.put("action", "REMOVE_SESSION");
		content.put("login", login);
		content.put("project_id", proj);
		content.put("session_id", session);
		try {
			msg.setContent(mapper.writeValueAsString(content));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		RemoveSessionSimpleBehaviour removeSessionSimple = new RemoveSessionSimpleBehaviour(
				content, msg);
		removeSessionSeq.addSubBehaviour(removeSessionSimple);
		myAgent.addBehaviour(removeSessionSeq);
	}

	private Connection createConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(
				"jdbc:postgresql://" + DataBaseConstants.host + "/"
						+ DataBaseConstants.databaseName,
				DataBaseConstants.userName, DataBaseConstants.password);
		return conn;
	}
}
