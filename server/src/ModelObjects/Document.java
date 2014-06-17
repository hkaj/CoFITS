package ModelObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Constants.DataBaseConstants;
import DatabaseScheme.ReferenceTable;
import DatabaseScheme.TableDocuments;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class Document extends ModelObject {
	final Integer id;
	final String name, type, owner;

	@JsonCreator
	public Document(@JsonProperty("id") Integer id,
			@JsonProperty("name") String name,
			@JsonProperty("type") String type,
			@JsonProperty("owner") String owner) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.owner = owner;
	}

	@JsonCreator
	public Document(@JsonProperty("id") Integer id) {
		super();
		this.id = id;
		this.name = this.retrieveProp("name", id);
		this.type = this.retrieveProp("type", id);
		this.owner = this.retrieveProp("owner", id);
	}

	public Document(ResultSet r) throws SQLException {
		super();
		this.id = r.getInt("id");
		this.name = r.getString("name");
		this.type = r.getString("type");
		this.owner = r.getString("owner");
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String retrieveProp(String prop, Integer id) {
		String value = "";
		String requestStr = "SELECT " + prop
				+ " FROM documents d where d.id = " + id + ";";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			res.next();
			value = res.getString(prop);
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	@JsonIgnore
	public String[] getKeyConditions() {
		final String[] res = { "name=" + this.id };
		return res;
	}

	@Override
	@JsonIgnore
	public String[] getValue() {
		final String[] res = { this.id.toString(), "'" + this.type + "'",
				"'" + this.name + "'" };
		return res;
	}

	@Override
	@JsonIgnore
	public ReferenceTable getReferenceTable() {
		return TableDocuments.getInstance();
	}

	@Override
	@JsonIgnore
	public String[] getKeyValues() {
		final String[] res = { this.id.toString() };
		return res;
	}

	public int getSession() {
		int sessionId = 0;
		String getSessionReq = "SELECT session FROM mobilizedIn WHERE document='"
				+ this.id + "';";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			ResultSet res = s.executeQuery(getSessionReq);
			res.next();
			sessionId = res.getInt("session");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sessionId;
	}

	public String getProject() {
		int sessionId = this.getSession();
		String proj = null;
		String getProjReq = "SELECT project FROM sessions WHERE id='"
				+ sessionId + "';";
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			ResultSet res = s.executeQuery(getProjReq);
			res.next();
			proj = res.getString("project");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return proj;
	}

	public Connection createConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(
				"jdbc:postgresql://" + DataBaseConstants.host + "/"
						+ DataBaseConstants.databaseName,
				DataBaseConstants.userName, DataBaseConstants.password);
		return conn;
	}
}
