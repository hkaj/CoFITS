package DocumentAgent;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import utils.projectNameEncoder;
import Constants.DataBaseConstants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateProjectBehaviour extends OneShotBehaviour {

	private HashMap<String, String> request;
	private ACLMessage message;

	public CreateProjectBehaviour(HashMap<String, String> req, ACLMessage msg) {
		this.message = msg;
		this.request = req;
	}

	@Override
	public void action() {
		String login = request.get("login");
		String proj_name = request.get("project_name");
		String description = request.get("description");
		String proj_id = new projectNameEncoder().encode(proj_name);
		String insertProjReq = "INSERT INTO projects(id, name, description, creator) VALUES ('"
				+ proj_id
				+ "', '"
				+ proj_name
				+ "', '"
				+ description
				+ "', '"
				+ login + "');";
		String involvedUsrReq = "INSERT INTO involvedIn(login, project, admin) VALUES ('"
				+ login + "', '" + proj_id + "', true);";
		Connection conn = null;
		Statement s = null;
		ObjectMapper mapper = new ObjectMapper();
		String fail = "none";
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			int res = 0;
			res = s.executeUpdate(insertProjReq);
			if (res > 0) {
				res = s.executeUpdate(involvedUsrReq);
				if (res <= 0) {
					System.out
							.println("[CREATE_PROJECT FAIL] The user has not been added to the project.");
					fail = "user";
				}
			} else {
				System.out
						.println("[CREATE_PROJECT FAIL] The project has not been created.");
				fail = "project";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// decide to send a confirm or a refuse depending on whether a request
		// failed or not.
		ACLMessage reply = this.message.createReply();
		Map<String, String> content = new HashMap<String, String>();
		content.put("project_id", proj_id);
		content.put("project_name", proj_name);
		if (fail == "none") {
			reply.setPerformative(ACLMessage.CONFIRM);
			content.put("state", "CREATED");
			content.put("reason", "");
		} else {
			reply.setPerformative(ACLMessage.REFUSE);
			content.put("state", "UNCHANGED");
			if (fail == "project") {
				content.put("reason", "The project has failed to be created.");
			} else if (fail == "user") {
				content.put(
						"reason",
						"The project has been created, but the user has not been added to it. Please add the user later.");
			} else {
				System.out
						.println("[CREATE_PROJECT ERROR] An interal error has occured.");
			}
		}
		try {
			reply.setContent(mapper.writeValueAsString(content));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		this.myAgent.send(reply);
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
