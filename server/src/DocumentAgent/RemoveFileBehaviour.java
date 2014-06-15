package DocumentAgent;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import Constants.DataBaseConstants;
import Constants.RequestConstants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RemoveFileBehaviour extends OneShotBehaviour {

	private HashMap<String, String> request;
	private ACLMessage message;
	private DocumentAgent docAgent;

	public RemoveFileBehaviour(HashMap<String, String> request,
			ACLMessage message) {
		this.request = request;
		this.message = message;
		this.docAgent = (DocumentAgent) myAgent;
	}

	@Override
	public void action() {
		HashMap<String, String> content = null;
		String login = request.get("login");
		String proj = request.get("project_id");
		int session = Integer.parseInt(request.get("session_id"));
		int fileId = Integer.parseInt(request.get("file_id"));
		String removeFileReq = "DELETE * FROM documents WHERE id='" + fileId
				+ "';";
		String removeMobilized = "DELETE * FROM mobilizedIn WHERE document='"
				+ fileId + "';";
		boolean isAdmin = this.checkLogin(proj, login);
		// Refuse to remove the file due to lack of privilege.
		if (!isAdmin) {
			content = new HashMap<String, String>();
			System.out.println("An unauthorized user (" + login
					+ ") tried to delete a file!");
			ACLMessage reply = this.message.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			content.put("file_id", String.valueOf(fileId));
			content.put("state", "UNCHANGED");
			content.put("reason",
					"You have to be the project administrator to remove this file (for now).");
			myAgent.send(reply);
			return;
		}
		removeFileFromSystem(fileId, session, proj);
		Connection conn = null;
		Statement s = null;
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			s.executeUpdate(removeMobilized);
			s.executeUpdate(removeFileReq);
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		notifySubscribers(proj);
	}

	private void removeFileFromSystem(int fileId, int session, String project) {
		Connection conn = null;
		Statement s = null;
		String fileName = ""; 
		String getFileName = "SELECT name FROM documents WHERE id='" + fileId
				+ "';";
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			ResultSet res = s.executeQuery(getFileName);
			res.next();
			fileName = res.getString("name"); 			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Path path = Paths.get(RequestConstants.documentAgentDirectory, project,
				String.valueOf(session), fileName);
		try {
			Files.delete(path);
		} catch (NoSuchFileException e) {
			e.printStackTrace();
			System.out.println("This file doesn't seem to exist, please check the path.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("There seems to be permission issues with this file, please contact the administrator.");
		}
	}

	// Notify the project subscribers that the file has been deleted.
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

	private boolean checkLogin(String proj, String login) {
		boolean isAdmin = false;
		String checkLoginReq = "SELECT admin FROM involvedIn WHERE login = '"
				+ login + "' and project = '" + proj + "';";
		Connection conn = null;
		Statement s = null;
		ResultSet res = null;
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			res = s.executeQuery(checkLoginReq);
			if (res.next()) {
				String admin = res.getString("admin");
				if (admin.equals("t")) {
					isAdmin = true;
				}
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isAdmin;
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
