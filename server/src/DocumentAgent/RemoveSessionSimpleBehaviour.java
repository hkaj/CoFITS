package DocumentAgent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RemoveSessionSimpleBehaviour extends AbstractServerBehaviour {

	public RemoveSessionSimpleBehaviour(HashMap<String, String> req, ACLMessage msg) {
		super(req, msg);
	}

	@Override
	public void action() {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> content = null;
		String login = request.get("login");
		String proj = request.get("project_id");
		String session = request.get("session_id");
		String checkLoginReq = "SELECT admin FROM involvedIn WHERE login = '"
				+ login + "' and project = '" + proj + "';";
		String removeSessionReq = "DELETE FROM sessions WHERE id='" + session
				+ "';";

		Connection conn = null;
		Statement s = null;
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
					reply.setContent(mapper.writeValueAsString(content));
					myAgent.send(reply);
					return;
				}
			}
			s.executeUpdate(removeSessionReq);
			s.close();
			conn.close();
		} catch (JsonProcessingException | SQLException e) {
			e.printStackTrace();
		}
		this.notifySubscribers(proj);
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
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		myAgent.send(msg);
	}
}
