package DocumentAgent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class AddSubscriberBehaviour extends AbstractServerBehaviour {
	AddSubscriberBehaviour(HashMap<String, String> req, ACLMessage msg) {
		super(req, msg);
	}

	@Override
	public void action() {
		String proj = request.get("project_id");
		AID subscriber = message.getSender();
		String reqStr = "SELECT id FROM projects WHERE id = '" + proj + "';";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			final ResultSet res = s.executeQuery(reqStr);
			if (res.next()) {
				docAgent.addSubscriber(proj, subscriber);
			} else {
				System.out.println("[SUBSCRIPTION FAIL] The project " + proj
						+ " does not exist.");
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
