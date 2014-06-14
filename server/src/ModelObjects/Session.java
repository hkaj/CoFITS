package ModelObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import Constants.DataBaseConstants;
import DatabaseScheme.ReferenceTable;
import DatabaseScheme.TableSessions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Session extends ModelObject
{
	final Integer id;
	final private String project;
	final private String date;
	final static private String[] validProperties = {"project","date"};
	
	public Session(@JsonProperty("id")Integer id) {
		super();
		this.id = id;
		this.project = retrieveProp("project", id);
		this.date = retrieveProp("date", id);
	}

	public Session(Integer id, String project, Timestamp date)
	{
		super();
		this.id = id;
		this.project = project;
		this.date = date.toString();
	}
	
	@JsonCreator
	public Session(@JsonProperty("project")String project, @JsonProperty("date")String date)
	{
		super();
		this.id = retrieveId(project, date);
		this.project = project;
		this.date = date;
	}
	
	public Session(ResultSet r) throws SQLException
	{
		super();
		this.id = r.getInt("id");
		this.project = r.getString("project");
		this.date = r.getTimestamp("date").toString();
	}
	
	private String retrieveProp(String prop, Integer id)
	{
		String value = "";
		String requestStr = "SELECT " + prop + " FROM sessions s where s.id = " + id + ";"; 
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			if (prop == "date") {
				value = res.getTimestamp(prop).toString();
			} else {
				value = res.getString(prop);
			}
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	private Integer retrieveId(String project, String date) {
		Integer value = 0;
		String requestStr = "SELECT id FROM sessions s where s.project = " + project + " AND date = '" + date + "';"; 
		try {
			Connection conn = this.createConnection();
			Statement s = conn.createStatement();
			final ResultSet res = s.executeQuery(requestStr);
			value = res.getInt("id");
			s.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public String getProject()
	{
		return project;
	}
	
	@JsonIgnore
	public Timestamp getDate()
	{
		return Timestamp.valueOf(date);
	}
	
	@Override @JsonIgnore
	public String[] getKeyConditions()
	{
		final String[] res = {"project='"+this.project+"'","date='"+this.date.toString()+"'"};
		return res ;
	}

	@Override @JsonIgnore
	public ReferenceTable getReferenceTable()
	{
		return TableSessions.getInstance();
	}

	@Override @JsonIgnore
	public String[] getValue()
	{
		final String[] res = {"'"+this.project+"'", "'"+this.date+"'"};
		return res;
	}

	@Override @JsonIgnore
	public String[] getKeyValues()
	{
		return this.getValue();
	}
	
	private Connection createConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(
				"jdbc:postgresql://" + DataBaseConstants.host + "/"
					+ DataBaseConstants.databaseName,
				DataBaseConstants.userName,
				DataBaseConstants.password
		);
	    return conn;
	}
	
}
