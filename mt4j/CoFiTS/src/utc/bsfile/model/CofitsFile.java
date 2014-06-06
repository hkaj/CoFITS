package utc.bsfile.model;

import java.util.Date;

public class CofitsFile {

	public CofitsFile(int id, String filename, Date lastModified, boolean isLocal) {
		m_id = id;
		m_lastModified = lastModified;
		m_filename = filename;
		m_local = isLocal;
	}
	
	//Getters & Setters

	public final Date getLastModified() {
		return m_lastModified;
	}
	public final void setLastModified(Date lastModified) {
		this.m_lastModified = lastModified;
	}
	public final String getFilename() {
		return m_filename;
	}
	public final void setFilename(String filename) {
		this.m_filename = filename;
	}
	public final int getId() {
		return m_id;
	}
	public final void setId(int id) {
		this.m_id = id;
	}
	
	public final boolean isLocal() {
		return m_local;
	}

	public void setLocal(boolean m_local) {
		this.m_local = m_local;
	}

	//Members
	private Date m_lastModified;
	private String m_filename;
	private int m_id;
	private boolean m_local;

}
