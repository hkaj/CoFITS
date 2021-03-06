package DocumentAgent;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import Constants.DataBaseConstants;

public abstract class AbstractLightBehaviour extends OneShotBehaviour {

	protected ACLMessage message;
	protected HashMap<String, String> request;

	public AbstractLightBehaviour(HashMap<String, String> req, ACLMessage msg) {
		super();
		this.message = msg;
		this.request = req;
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