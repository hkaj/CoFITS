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
	final private String login, lastName, firstName;

	@JsonCreator
	public User(@JsonProperty("login") String l, @JsonProperty("lastName")String ln, @JsonProperty("firstName")String fn)
	{
		super();
		this.login=l;
		this.lastName = ln;
		this.firstName = fn;
	}
	
	public User(ResultSet r) throws SQLException
	{
		super();
		this.login = r.getString("login");
		this.firstName = r.getString("firstname");
		this.lastName = r.getString("lastname");
	}
	@JsonIgnore
	public String getLogin()
	{
		return this.login;
	}
	@JsonIgnore
	public String getLastName()
	{
		return this.lastName;
	}
	@JsonIgnore
	public String getFirstName()
	{
		return this.firstName;
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
		final String[] res = {this.login, this.lastName, this.firstName};
		return res;
	}

	@Override
	public String[] getKeyValues()
	{
		final String[] res={this.login};
		return res;
	}
};
