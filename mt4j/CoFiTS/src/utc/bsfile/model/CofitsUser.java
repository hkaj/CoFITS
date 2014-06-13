package utc.bsfile.model;

public class CofitsUser {

	public CofitsUser(String login) {
		this(login, false);
	}

	public CofitsUser(String login, boolean isAdmin){
		this.m_login = login;
		this.m_admin = isAdmin;
	}
	
	//Getters & Setters
	public String getLogin(){return m_login;}
	public boolean isAdmin(){return m_admin;}
	
	public void setLogin(String log){m_login = log;}
	public void setAdmin(boolean isAdmin){m_admin = isAdmin;}
	
	
	//Members
	private String m_login;
	private boolean m_admin;
}
