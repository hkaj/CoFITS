package DocumentAgent;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import Constants.DataBaseConstants;
import Constants.RequestConstants;
import ModelObjects.Project;

public class UploadBehaviour extends OneShotBehaviour {
	final private HashMap<String, String> request;
	final private ACLMessage message;
	ArrayList<Project> projects;

	public UploadBehaviour(HashMap<String, String> request, ACLMessage message) {
		this.request = request;
		this.message = message;
	}

	@Override
	public void action() {
		// Ne pas oublier de propager les changements aux subscribers
		String project = this.request.get("project_id");
		String session = this.request.get("session_id");
		String filename = this.request.get("file_name");

		Path path = Paths.get(RequestConstants.documentAgentDirectory, project,
				session, filename);
		FileOutputStream fos = null;
		try {
			// write the file in the file system
			fos = new FileOutputStream(path.toString());
			Files.createDirectories(path.getParent());
			Files.createFile(path);
			fos.write(this.request.get("file").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
		}
		this.addFileToDB(filename, session, request.get("user_login"));
	}

	private void addFileToDB(String file, String session, String owner) {
		Date date = new Date();
		Timestamp now = new Timestamp(date.getTime());
		String req = "INSERT INTO documents(name, owner, last_modified) VALUES ('"
				+ file + "', '" + owner + "', " + now + ");";
		Connection conn = null;
		Statement s = null;
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			s.executeUpdate(req);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try { s.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
			if (conn != null) {
				try { conn.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
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
