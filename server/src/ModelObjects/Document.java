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
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "type")
public class Document extends ModelObject
{
	final Integer id;
	final String name,type, owner;
	final static private String[] validProperties = {"name","usedDuring","UsedIn","ownedBy", "lastModified"};

	@JsonCreator
	public Document(@JsonProperty("id")Integer id, @JsonProperty("name")String name, @JsonProperty("type")String type, @JsonProperty("owner")String owner)
	{
		super();
		this.id=id;
		this.name = name;
		this.type = type;
		this.owner = owner;
	}
	
	@JsonCreator
	public Document(@JsonProperty("id")Integer id, @JsonProperty("owner")String owner)
	{
		super();
		this.id=id;
		this.name = this.retrieveProp("name", id);
		this.type = this.retrieveProp("type", id);
		this.owner = owner;
	}
	
	public Document(ResultSet r) throws SQLException
	{
		super();
		this.id = r.getInt("id");
		this.name = r.getString("name");
		this.type = r.getString("type");
		this.owner = r.getString("owner");
	}

	public String getName()
	{
		return name;
	}

	public String getType()
	{
		return type;
	}
	
	public String retrieveProp(String prop, Integer id)
	{
		String value = "";
		String requestStr = "SELECT " + prop + " FROM documents d where d.id = " + id + ";"; 
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
}
