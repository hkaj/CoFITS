package DocumentAgent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import Constants.DataBaseConstants;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RemoveSessionBehaviour extends OneShotBehaviour {
	private HashMap<String, String> request;
	private ACLMessage message;
	private DocumentAgent docAgent;
	
	public RemoveSessionBehaviour(HashMap<String, String> req, ACLMessage msg) {
		this.request = req;
		this.message = msg;
		this.docAgent = (DocumentAgent) myAgent;
	}


	@Override
	public void action() {
		
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
