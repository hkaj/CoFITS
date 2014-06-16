package DocumentAgent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Constants.RequestConstants;
oimport Constants.RequestConstants;

public class UploadBehaviour extends AbstractServerBehaviour {
	public UploadBehaviour(HashMap<String, String> request, ACLMessage message) {
		super(request, message);
	}

	@Override
	public void action() {
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
		// add the document in the DB
		this.addFileToDB(filename, session, request.get("login"));
		ACLMessage msg = message.createReply();
		msg.setPerformative(ACLMessage.REQUEST);
		List<AID> subscribers = docAgent.getSubscribers(project);
		for (AID dest : subscribers) {
			msg.addReplyTo(dest);
		}
		HashMap<String, String> req = new HashMap<String, String>();
		req.put("action", "LIST_PROJECT");
		req.put("project_id", project);
		req.put("login", "TATIN");
		docAgent.addBehaviour(new DownloadProjectOverviewBehaviour(req, msg));
	}

	private void addFileToDB(String file, String session, String owner) {
		Date date = new Date();
		Integer docId = 0;
		Timestamp now = new Timestamp(date.getTime());
		String reqInsertDoc = "INSERT INTO documents(name, owner, last_modified) VALUES ('"
				+ file + "', '" + owner + "', " + now + ");";
		String reqDocId = "SELECT id FROM documents WHERE name = '" + file
				+ "' AND owner = '" + owner + "';";
		Connection conn = null;
		Statement s = null;
		try {
			conn = this.createConnection();
			s = conn.createStatement();
			s.executeUpdate(reqInsertDoc);
			ResultSet res = s.executeQuery(reqDocId);
			res.next();
			docId = res.getInt("id");
			String reqInsertMobilized = "INSERT INTO mobilizedin(document, session) VALUES ('"
					+ docId + "', '" + session + "');";
			s.executeUpdate(reqInsertMobilized);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
