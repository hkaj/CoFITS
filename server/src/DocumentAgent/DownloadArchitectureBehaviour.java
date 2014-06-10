package DocumentAgent;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import Constants.DataBaseConstants;
import ModelObjects.Project;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DownloadArchitectureBehaviour extends OneShotBehaviour {

	final HashMap<String, String> request;
	final ACLMessage message;
	ArrayList<Project> projects;

	public DownloadArchitectureBehaviour(HashMap<String, String> request,
			ACLMessage message) {
		this.request = request;
		this.message = message;
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
			projectMap.put(p.getId(), getSessions(p.getId()));
		}
		try {
			reply.setContent(mapper.writeValueAsString(projectMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
		myAgent.send(reply);
	}

	private ArrayList<Project> getProjects() {
		ArrayList<Project> projects = new ArrayList<Project>();
		String requestStr = "SELECT * FROM projects;";
		try {
			Statement s = this.createConnection().createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			while (res.next()) {
				projects.add(new Project(res));
			}
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
			Statement s = this.createConnection().createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			while (res.next()) {
				HashMap<String, Object> session = new HashMap<String, Object>();
				Integer id = res.getInt("id");
				session.put("id", id);
				session.put("date", res.getTimestamp("date").toString());
				session.put("files", getFiles(id));
				sessions.add(session);
			}
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
			Statement s = this.createConnection().createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			while (res.next()) {
				HashMap<String, Object> file = new HashMap<String, Object>();
				file.put("id", res.getInt("id"));
				file.put("name", res.getString("name"));
				file.put("type", res.getString("type"));
				file.put("owner", res.getString("owner"));
				file.put("last_modified", res.getTimestamp("last_modified")
						.toString());
				files.add(file);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return files;
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
