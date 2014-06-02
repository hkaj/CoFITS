package ModelObjects;

import java.sql.ResultSet;
import java.sql.SQLException;

import DatabaseScheme.ReferenceTable;
import DatabaseScheme.TableDocuments;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "type")
public class Document extends ModelObject
{
	final Integer id;
	final String name,type;
	final static private String[] validProperties = {"name","usedDuring","UsedIn","ownedBy", "lastModified"};

	@JsonCreator
	public Document(@JsonProperty("id")Integer id, @JsonProperty("name")String name, @JsonProperty("type")String type)
	{
		super();
		this.id=id;
		this.name = name;
		this.type = type;
	}
	
	public Document(ResultSet r) throws SQLException
	{
		super();
		this.id = r.getInt("id");
		this.name = r.getString("name");
		this.type = r.getString("type");
	}

	public String getName()
	{
		return name;
	}

	public String getType()
	{
		return type;
	}
	
	@Override @JsonIgnore
	public String[] getKeyConditions()
	{
		final String[] res = {"name="+this.id};
		return res ;
	}
	
	@Override @JsonIgnore
	public String[] getValue()
	{
		final String[] res = {this.id.toString(),"'"+this.type+"'","'"+this.name+"'"};
		return res;
	}

	@Override @JsonIgnore
	public ReferenceTable getReferenceTable()
	{
		return TableDocuments.getInstance();
	}

	@Override @JsonIgnore
	public String[] getKeyValues()
	{
		final String[] res = {this.id.toString()};
		return res ;
	}
}
