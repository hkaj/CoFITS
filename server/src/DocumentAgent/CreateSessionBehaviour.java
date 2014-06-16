package DocumentAgent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateSessionBehaviour extends AbstractServerBehaviour {

	public CreateSessionBehaviour(HashMap<String, String> req, ACLMessage msg) {
		super(req, msg);
	}

	@Override
	public void action() {
		String login = request.get("login");
		String proj = request.get("project_id");
		String date = request.get("date");
		String checkUsrReq = "SELECT * FROM involvedIn WHERE login='" + login
				+ "' AND project='" + "';";
		String insertSessionReq = "INSERT INTO sessions (project, date) VALUES ('"
				+ proj + "', '" + date + "');";
		String sessionIdReq = "SELECT id FROM sessions WHERE project = '"
				+ proj + "' AND date = '" + "';";
		ACLMessage reply = this.message.createReply();

		Connection conn = null;
		Statement s = null;
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			ResultSet res = null;
			res = s.executeQuery(checkUsrReq);
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, String> content = new HashMap<String, String>();
			content.put("project_id", proj);
			content.put("date", date);
			if (res.next()) {
				// verified user
				int ins = 0;
				ins = s.executeUpdate(insertSessionReq);
				if (ins > 0) {
					// new session added
					res = s.executeQuery(sessionIdReq);
					res.next();
					int sessionId = res.getInt("id");
					reply.setPerformative(ACLMessage.CONFIRM);
					content.put("session_id", Integer.toString(sessionId));
					content.put("reason", "");
					content.put("state", "CREATED");
					try {
						reply.setContent(mapper.writeValueAsString(content));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out
							.println("[CREATE_SESSION FAIL] The session has failed to be created.");
					reply.setPerformative(ACLMessage.REFUSE);
					content.put("state", "UNCHANGED");
					content.put(
							"reason",
							"The session has not been created, maybe a meeting is already planned at this date.");
				}
			} else {
				System.out
						.println("[CREATE_SESSION FAIL] Unauthorized user tried to create a session.");
				reply.setPerformative(ACLMessage.REFUSE);
				content.put("state", "UNCHANGED");
				content.put(
						"reason",
						"You are not part of this project, please ask the administrator of the project to add you.");
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
		myAgent.send(reply);
		ACLMessage msg = message.createReply();
		msg.setPerformative(ACLMessage.REQUEST);
		List<AID> subscribers = docAgent.getSubscribers(proj);
		for (AID dest : subscribers) {
			msg.addReplyTo(dest);
		}
		HashMap<String, String> req = new HashMap<String, String>();
		req.put("action", "LIST_PROJECT");
		req.put("project_id", proj);
		req.put("login", "TATIN");
		docAgent.addBehaviour(new DownloadProjectOverviewBehaviour(req, msg));
	}
}
