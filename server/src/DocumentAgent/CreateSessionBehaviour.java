package DocumentAgent;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import Constants.DataBaseConstants;

public class CreateSessionBehaviour extends OneShotBehaviour {

	private ACLMessage message;
	private HashMap<String, String> request;
	
	public CreateSessionBehaviour(HashMap<String, String> req, ACLMessage msg) {
		message = msg;
		request = req;
	}
	
	@Override
	public void action() {
		String login = request.get("login");
		String proj_id = request.get("project_id");
		String date = request.get("date");

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
