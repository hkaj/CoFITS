package ModelObjects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import DatabaseScheme.ReferenceTable;
import DatabaseScheme.TableSessions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Session extends ModelObject
{
	final private String project;
	final private String date;
	final static private String[] validProperties = {"project","date"};
	

	public Session(String project, Timestamp date)
	{
		super();
		this.project = project;
		this.date = date.toString();
	}
	
	@JsonCreator
	public Session(@JsonProperty("project")String project, @JsonProperty("date")String date)
	{
		super();
		this.project = project;
		this.date = date;
	}
	
	public Session(ResultSet r) throws SQLException
	{
		super();
		this.project = r.getString("project");
		this.date = r.getTimestamp("date").toString();
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
	
}
