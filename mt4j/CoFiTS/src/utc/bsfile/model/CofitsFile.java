package utc.bsfile.model;

import java.nio.file.FileSystems;
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
			return formatter.parse(m_node.getCurrent().path("last-modified").asText());
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
	
	public final String getFilepath(){
		TwoLinkedJsonNode session = m_node.getParentTwoLinkedJsonNode();
		TwoLinkedJsonNode project = session.getParentTwoLinkedJsonNode();
		String separator = FileSystems.getDefault().getSeparator();
		
		return project.getName() + separator + session.getName() + separator + getFilename();
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
	
	public final boolean isFile() {
		if ( m_node.getCurrent().path("local").asText().equals("") ){
			return false;
		}
		return true;
	}

	public void setLocal(boolean local) {
		ObjectNode node = (ObjectNode) m_node.getCurrent();
		node.remove("local");
		node.put("local", local);
	}
	
	
	public String getProjectId(){
		return m_node.getParentTwoLinkedJsonNode().getParentTwoLinkedJsonNode().getName();
	}
	
	
	public int getSessionId(){
		return m_node.getParentTwoLinkedJsonNode().getCurrent().path("id").asInt();
	}

	//Members
	private TwoLinkedJsonNode m_node;

}
