package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Constants.DataBaseConstants;


public class projectNameEncoder {
	
	public Connection createConnection() throws SQLException {

		Connection conn = null;
		conn = DriverManager.getConnection(
				"jdbc:postgresql://" + DataBaseConstants.host + "/"
					+ DataBaseConstants.databaseName,
				DataBaseConstants.userName,
				DataBaseConstants.password
		);
	    return conn;
	}
	
	public String encode(String projectName) {
		String projectId = projectName.replace(" ", "_");
		String requestStr = "SELECT count(*) as c FROM projects p where p.name = '" + projectName + "';"; 
		int count  = 0;
		try {
			Statement s = this.createConnection().createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			if (res.next()) {
				count = res.getInt("c");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		projectId = projectId.concat("_" + count);
		return projectId;
	}

	public void main(String[] args) {
		System.out.println("Trying to encode 'SR03 project'...");
		System.out.println("The encoded id is: " + encode("SR03 project"));
	}
}
