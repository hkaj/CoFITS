package DocumentAgent;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
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

public class RemoveSessionSimpleBehaviour extends OneShotBehaviour {

	private HashMap<String, String> request;
	private ACLMessage message;
	private DocumentAgent docAgent;

	public RemoveSessionSimpleBehaviour(HashMap<String, String> req, ACLMessage msg) {
		this.request = req;
		this.message = msg;
		this.docAgent = (DocumentAgent) myAgent;
	}

	@Override
	public void action() {
		ArrayList<Integer> files = null;
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> content = null;
		String login = request.get("login");
		String proj = request.get("project_id");
		String session = request.get("session_id");
		String checkLoginReq = "SELECT admin FROM involvedIn WHERE login = '"
				+ login + "' and project = '" + proj + "';";
		String getFilesReq = "SELECT document FROM mobilizedin WHERE session='"
				+ session + "';";
		String removeMobilizedReq = "DELETE * FROM mobilizedIn WHERE session='"
				+ session + "';";
		String removeSessionReq = "DELETE * FROM sessions WHERE id='" + session
				+ "';";

		Connection conn = null;
		Statement s = null;
		int updated = 0;
		ResultSet res = null;
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			res = s.executeQuery(checkLoginReq);
			if (res.next()) {
				String admin = res.getString("admin");
				if (!admin.equals("t")) {
					content = new HashMap<String, String>();
					System.out.println("An unauthorized user (" + login
							+ ") tried to delete a session!");
					ACLMessage reply = this.message.createReply();
					reply.setPerformative(ACLMessage.REFUSE);
					content.put("session_id", session);
					content.put("state", "UNCHANGED");
					content.put("reason",
							"You have to be the project administrator to remove this session.");
					myAgent.send(reply);
					return;
				}
			}
			res = s.executeQuery(getFilesReq);
			updated = s.executeUpdate(removeMobilizedReq);
			updated = s.executeUpdate(removeSessionReq);
			files = new ArrayList<Integer>();
			while (res.next()) {
				files.add(res.getInt("document"));
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.notifySubscribers(proj);
		for (int file : files) {
			this.requestFileRemoval(login, proj, file);
		}
	}

	// Notify the project subscribers that the session has been deleted.
	private void notifySubscribers(String proj) {
		ObjectMapper mapper = new ObjectMapper();
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		for (AID aid : docAgent.getSubscribers(proj)) {
			msg.addReplyTo(aid);
		}
		msg.addReceiver(myAgent.getAID());
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("action", "LIST_PROJECT");
		content.put("project", proj);
		content.put("login", "TATIN");
		try {
			msg.setContent(mapper.writeValueAsString(content));
		} catch (IOException e) {
			e.printStackTrace();
		}
		myAgent.send(msg);
	}

	private void requestFileRemoval(String login, String proj, int file) {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("action", "REMOVE_FILE");
		content.put("login", login);
		content.put("project_id", proj);
		content.put("file_id", String.valueOf(file));
		try {
			msg.setContent(mapper.writeValueAsString(content));
		} catch (IOException e) {
			e.printStackTrace();
		}
		msg.addReceiver(myAgent.getAID());
		myAgent.send(msg);
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
