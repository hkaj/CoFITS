package DocumentAgent;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import Constants.DataBaseConstants;

public abstract class AbstractServerBehaviour extends OneShotBehaviour {

	protected DocumentAgent docAgent;
	protected HashMap<String, String> request;
	protected ACLMessage message;

	public AbstractServerBehaviour(HashMap<String, String> req, ACLMessage msg) {
		super();
		this.message = msg;
		this.request = req;
		this.docAgent = (DocumentAgent) myAgent;
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