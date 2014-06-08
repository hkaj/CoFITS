package ModelObjects;

import java.sql.ResultSet;
import java.sql.SQLException;

import utils.projectNameEncoder;
import DatabaseScheme.ReferenceTable;
import DatabaseScheme.TableProjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Project extends ModelObject
{
	final String id,name,description,creator;
	final static private String[] validProperties = {"id","name","description","creator"};
	
	@JsonCreator
	public Project(@JsonProperty("id")String id, @JsonProperty("name")String name, @JsonProperty("description")String description, @JsonProperty("creator")String creator)
	{
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.creator=creator;
	}
	
	public Project(ResultSet r) throws SQLException
	{
		super();
		this.id = r.getString("id");
		this.name = r.getString("name");
		this.description = r.getString("description");
		this.creator = r.getString("creator");
	}
	
	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String getCreator()
	{
		return creator;
	}
	
	@Override @JsonIgnore
	public String[] getKeyConditions()
	{
		final String[] res = {"id="+this.id};
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
		final String[] res={this.id,this.name,this.description,this.creator};
		return res;
	}

	@Override @JsonIgnore
	public String[] getKeyValues()
	{
		final String[] res={this.id};
		return res;
	}
}
