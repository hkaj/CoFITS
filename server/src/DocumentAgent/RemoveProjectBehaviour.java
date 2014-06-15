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

public class RemoveProjectBehaviour extends OneShotBehaviour {
	final private HashMap<String, String> request;
	final private ACLMessage message;

	public RemoveProjectBehaviour(HashMap<String, String> request,
			ACLMessage message) {
		this.request = request;
		this.message = message;
	}

	@Override
	public void action() {
		ArrayList<Integer> sessions = null;
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> content = null;
		String login = request.get("login");
		String proj = request.get("project_id");
		String getSessionReq = "SELECT id FROM sessions WHERE project='" + proj
				+ "';";
		Connection conn = null;
		Statement s = null;
		ResultSet res = null;
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			res = s.executeQuery(getSessionReq);
			sessions = new ArrayList<Integer>();
			while (res.next()) {
				sessions.add(res.getInt("id"));
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		RemoveProjectSeqBehaviour removeProjectSeq = new RemoveProjectSeqBehaviour();
		for (int session : sessions) {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setSender(this.message.getSender());
			mapper = new ObjectMapper();
			content = new HashMap<String, String>();
			content.put("action", "REMOVE_SESSION");
			content.put("login", login);
			content.put("project_id", proj);
			content.put("session_id", String.valueOf(session));
			removeProjectSeq.addSubBehaviour(new RemoveSessionBehaviour(
					content, msg));
		}
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(this.message.getSender());
		mapper = new ObjectMapper();
		content = new HashMap<String, String>();
		content.put("action", "REMOVE_SESSION");
		content.put("login", login);
		content.put("project_id", proj);
		try {
			msg.setContent(mapper.writeValueAsString(content));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		removeProjectSeq.addSubBehaviour(new RemoveProjectSimpleBehaviour(
				content, msg));
		myAgent.addBehaviour(removeProjectSeq);
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
