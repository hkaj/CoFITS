package ModelObjects;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import DatabaseScheme.ReferenceTable;
import DatabaseScheme.TableProjects;
import Requests.Predicate;

public class Project extends ModelObject
{
	final String name,description;
	final static private String[] validProperties = {"name","description","session"};
	
	@JsonCreator
	public Project(@JsonProperty("name")String name, @JsonProperty("description")String description)
	{
		super();
		this.name = name;
		this.description = description;
	}
	
	public Project(ResultSet r) throws SQLException
	{
		super();
		this.name = r.getString("name");
		this.description = r.getString("description");
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}
	
	
	@Override @JsonIgnore
	public String[] getKeyConditions()
	{
		final String[] res = {"name="+this.name};
		return res ;
	}

	@Override @JsonIgnore
	public ReferenceTable getReferenceTable()
	{
		return TableProjects.getInstance();
	}

	@Override @JsonIgnore
	public String[] getValue()
	{
		final String[] res={this.name,this.description};
		return res;
	}

	@Override @JsonIgnore
	public String[] getKeyValues()
	{
		final String[] res={this.name};
		return res;
	}
}
