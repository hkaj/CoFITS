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

public class DownloadArchitectureBehaviour extends AbstractLightBehaviour {

	ArrayList<Project> projects;

	public DownloadArchitectureBehaviour(HashMap<String, String> request,
			ACLMessage message) {
		super(request, message);
	}

	@Override
	public void action() {
		if (!request.get("login").equals("TATIN")) {
			System.out
					.println("An unauthorized user tried to download the architecture!");
			return;
		}
		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> projectMap = new HashMap<String, Object>();
		projects = getProjects();
		for (Project p : projects) {
			Map<String, Object> proj = new HashMap<String, Object>();
			proj.put("name", p.getName());
			proj.put("creator", p.getCreator());
			proj.put("description", p.getDescription());
			proj.put("sessions", getSessions(p.getId()));
			projectMap.put(p.getId(), proj);

		}
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("action", "LIST");
		content.put("list", projectMap);
		try {
			reply.setContent(mapper.writeValueAsString(content));
		} catch (IOException e) {
			e.printStackTrace();
		}
		myAgent.send(reply);
	}

	private ArrayList<Project> getProjects() {
		ArrayList<Project> projects = new ArrayList<Project>();
		String requestStr = "SELECT * FROM projects;";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			while (res.next()) {
				projects.add(new Project(res));
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projects;
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
