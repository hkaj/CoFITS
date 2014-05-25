package ModelObjects;

import java.sql.ResultSet;
import java.sql.SQLException;

import DatabaseScheme.ReferenceTable;
import DatabaseScheme.TableUsers;
import Requests.Predicate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User extends ModelObject
{
	final private String login;

	@JsonCreator
	public User(@JsonProperty("login") String l)
	{
		super();
		this.login=l;
	}
	
	public User(ResultSet r) throws SQLException
	{
		super();
		this.login = r.getString("login");
	}
	@JsonIgnore
	public String getLogin()
	{
		return this.login;
	}	
	
	@Override @JsonIgnore
	public String[] getKeyConditions()
	{
		final String[] res = {"login='"+this.login+"'"};
		return res ;
	}

	@Override @JsonIgnore
	public ReferenceTable getReferenceTable()
	{
		return TableUsers.getInstance();
	}

	@Override
	public String[] getValue()
	{
		final String[] res = {this.login};
		return res;
	}

	@Override
	public String[] getKeyValues()
	{
		final String[] res={this.login};
		return res;
	}
};
