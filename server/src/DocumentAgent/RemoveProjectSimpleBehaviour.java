package DocumentAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RemoveProjectSimpleBehaviour extends AbstractServerBehaviour {
	protected DocumentAgent docAgent;

	public RemoveProjectSimpleBehaviour(Agent a, HashMap<String, String> request,
			ACLMessage message) {
		super(request, message);
		this.docAgent = (DocumentAgent)a;
		System.out.println("docAgent: " + docAgent);
	}

	@Override
	public void action() {
		HashMap<String, String> content = null;
		String login = request.get("login");
		String proj = request.get("project_id");
		String checkLoginReq = "SELECT admin FROM involvedIn WHERE login = '"
				+ login + "' and project = '" + proj + "';";
		String removeProjReq = "DELETE FROM projects WHERE id='" + proj
				+ "';";
		String removeInvolvedReq = "DELETE FROM involvedIn WHERE project='"
				+ proj + "';";
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
							+ ") tried to delete a project!");
					ACLMessage reply = this.message.createReply();
					reply.setPerformative(ACLMessage.REFUSE);
					content.put("project_id", proj);
					content.put("state", "UNCHANGED");
					content.put("reason",
							"You have to be a project administrator to remove this project.");
					myAgent.send(reply);
					return;
				}
			}
			s.executeUpdate(removeInvolvedReq);
			s.executeUpdate(removeProjReq);
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		notifySubscribers(proj);
	}

	// Notify the project subscribers that the project has been deleted.
	private void notifySubscribers(String proj) {
		ObjectMapper mapper = new ObjectMapper();
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		List<AID> subscrbrs = new ArrayList<AID>();
		subscrbrs = docAgent.getSubscribers(proj);
		for (AID dest : subscrbrs) {
			msg.addReplyTo(dest);
		}
		HashMap<String, String> projStruct = new HashMap<String, String>();
		projStruct.put(proj, "");
		HashMap<String, Object> req = new HashMap<String, Object>();
		req.put("action", "LIST_PROJECT");
		req.put("list", projStruct);
		try {
			msg.setContent(mapper.writeValueAsString(req));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		myAgent.send(msg);
	}
}
