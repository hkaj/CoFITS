package utc.bsfile.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.node.ObjectNode;

import utc.bsfile.model.menu.TwoLinkedJsonNode;

public class CofitsFile {

	public CofitsFile(TwoLinkedJsonNode fileNode) {
		m_node = fileNode;
	}
	
	//Getters & Setters

	public final Date getLastModified() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			return formatter.parse(m_node.getCurrent().path("id").asText());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public final void setLastModified(Date lastModified) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");		
		ObjectNode node = (ObjectNode) m_node.getCurrent();
		node.remove("last-modified");
		node.put("last-modified", formatter.format(lastModified));
	}
	
	public final String getFilename() {
		return m_node.getName();
	}
	
	public final void setFilename(String filename) {
		m_node.setName(filename);
	}
	
	public final int getId() {
		return m_node.getCurrent().path("id").asInt();
	}
	
	public final void setId(int id) {
		ObjectNode node = (ObjectNode) m_node.getCurrent();
		node.remove("id");
		node.put("id", id);
	}
	
	public final boolean isLocal() {
		return m_node.getCurrent().path("local").asBoolean();
	}

	public void setLocal(boolean local) {
		ObjectNode node = (ObjectNode) m_node.getCurrent();
		node.remove("local");
		node.put("local", local);
	}

	//Members
	private TwoLinkedJsonNode m_node;

}
