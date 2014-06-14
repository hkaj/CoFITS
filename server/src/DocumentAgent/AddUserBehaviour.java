package DocumentAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import Constants.DataBaseConstants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddUserBehaviour extends OneShotBehaviour {
	private ACLMessage message;
	private HashMap<String, String> request;

	public AddUserBehaviour(HashMap<String, String> request, ACLMessage message) {
		this.request = request;
		this.message = message;
	}

	public AddUserBehaviour(Agent a, HashMap<String, String> request,
			ACLMessage message) {
		super(a);
		this.request = request;
		this.message = message;
	}

	@Override
	public void action() {
		ACLMessage reply = message.createReply();
		ObjectMapper mapper = new ObjectMapper();
		String adminLogin = request.get("my_login");
		String projectId = request.get("project_id");
		String loginToAdd = request.get("login_to_add");
		String admin = request.get("admin");

		HashMap<String, String> responseContent = new HashMap<String, String>();
		responseContent.put("action", "ADD_USER");
		responseContent.put("login", loginToAdd);
		responseContent.put("project_id", projectId);

		String requestStr = "SELECT * FROM involvedIn WHERE login = '"
				+ adminLogin + "' AND project = '" + projectId
				+ "' AND admin = true;";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			if (!res.next()) {
				reply.setPerformative(ACLMessage.REFUSE);
				responseContent.put("reason", "L'utilisateur '" + adminLogin
						+ "' n'est pas administrateur du projet '" + projectId
						+ "'.");
			} else {
				addUserToProject(loginToAdd, projectId, admin);
				reply.setPerformative(ACLMessage.CONFIRM);
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			reply.setContent(mapper.writeValueAsString(responseContent));
			myAgent.send(reply);
		} catch (IOException e){
			e.printStackTrace();
		} 
	}

	private void addUserToProject(String login, String project, String admin) {
		String requestStr = "INSERT INTO involvedIn VALUES ('" + login + "', '"
				+ project + "', " + admin + ");";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			s.executeUpdate(requestStr);
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
