package DocumentAgent;

import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ModelObjects.Project;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DownloadProjectOverviewBehaviour extends AbstractLightBehaviour {

	public DownloadProjectOverviewBehaviour(HashMap<String, String> request,
			ACLMessage message) {
		super(request, message);
	}

	@Override
	public void action() {
		Project proj = null;
		Map<String, Object> projStruct = null;
		ACLMessage reply = message.createReply();
		String project_id = this.request.get("project_id");
		String login = this.request.get("login");
		boolean authorized = (login == "TATIN");
		if (!authorized) {
			String authReq = "SELECT * FROM involvedIn WHERE project='"
					+ project_id + "' AND user='" + login + "';";
			Connection conn = null;
			Statement s = null;
			try {
				conn = this.createConnection();
				s = conn.createStatement();
				ResultSet res = s.executeQuery(authReq);
				if (res.next()) {
					authorized = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (s != null) {
						s.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (!authorized) {
				System.out
						.println("[LIST_PROJECT FAIL] An unauthorized user tried to download a project structure.");
				return;
			}
			ObjectMapper mapper = new ObjectMapper();
			proj = this.getProject(project_id);
			HashMap<String, Object> project = new HashMap<String, Object>();
			project.put(project_id, "");
			if (proj != null) {
				projStruct = new HashMap<String, Object>();
				projStruct.put("name", proj.getName());
				projStruct.put("creator", proj.getCreator());
				projStruct.put("description", proj.getDescription());
				projStruct.put("sessions", getSessions(proj.getId()));
				project.put(project_id, projStruct);
			}
			Map<String, Object> content = new HashMap<String, Object>();
			content.put("action", "LIST_PROJECT");
			content.put("list", project);
			try {
				reply.setContent(mapper.writeValueAsString(content));
			} catch (IOException e) {
				e.printStackTrace();
			}
			reply.setPerformative(ACLMessage.INFORM);
			myAgent.send(reply);
		}
	}

	private Project getProject(String id) {
		Project project = null;
		String requestStr = "SELECT * FROM projects WHERE id='" + id + "';";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			if (res.next()) {
				project = new Project(res);
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return project;
	}

	private ArrayList<Object> getSessions(String projectId) {
		ArrayList<Object> sessions = new ArrayList<Object>();
		String requestStr = "SELECT * FROM sessions where project = '"
				+ projectId + "';";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			while (res.next()) {
				HashMap<String, Object> session = new HashMap<String, Object>();
				Integer id = res.getInt("id");
				session.put("id", id);
				session.put("date", res.getTimestamp("date").toString());
				session.put("files", getFiles(id));
				sessions.add(session);
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sessions;
	}

	private ArrayList<Object> getFiles(Integer sessionId) {
		ArrayList<Object> files = new ArrayList<Object>();
		String requestStr = "SELECT * FROM mobilizedIn mobIn INNER JOIN documents doc ON mobIn.document = doc.id WHERE session = '"
				+ sessionId + "';";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			while (res.next()) {
				HashMap<String, Object> file = new HashMap<String, Object>();
				file.put("id", res.getInt("id"));
				file.put("name", res.getString("name"));
				file.put("owner", res.getString("owner"));
				file.put("last_modified", res.getTimestamp("last_modified")
						.toString());
				files.add(file);
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return files;
	}
}
