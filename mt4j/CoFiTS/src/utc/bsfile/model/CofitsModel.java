package utc.bsfile.model;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.FilePathManager;
import utc.bsfile.util.JsonManager;
import utc.bsfile.util.PropertyManager;

public class CofitsModel {
	
	private static String SECOND_PROPERTIES_FILE = "rsc/config/second.properties";

	public CofitsModel() {
		JsonNode jsonNode = JsonManager.getInstance().createJsonNode(new File(PropertyManager.JSON_STRUCTURE_FILENAME));
		m_projectsArchitectureRootNode = new TwoLinkedJsonNode(jsonNode, "root", true);
	}
	
	
	//Agent related methods
	/**
	 * @param scene - The main Scene of the application
	 * @brief Launch the Agent in charge of communication with server
	 */
	public void launchAgentContainer() {
		Runtime rt = Runtime.instance();
	  	ProfileImpl p = null;
	  	ContainerController cc; 
	  	  try{

	  		p = new ProfileImpl(SECOND_PROPERTIES_FILE);
		    cc = rt.createAgentContainer(p);
		    
		    AgentController ac = cc.createNewAgent("tatin-cofits-agent", CofitsGuiAgent.class.getName() ,new Object[]{this});
		    ac.start();
			
	  	  } catch(Exception ex) {
	  		  System.out.println("Main container not started");
	  	  }
	}
	
	
	public void downloadFile(int fileId) {
		GuiEvent event = new GuiEvent(this, CofitsGuiAgent.DOWNLOAD_FILE);
		event.addParameter(fileId);
		if (m_agent != null){
			m_agent.postGuiEvent(event);
		} else {
			System.err.println("No Agent running");
		}
	}
	
	
	public void fileReceived(String filename) {
		firePropertyChange("File Received", null, filename);		
	}
	
	
	public void changeProjectArchitecture(TwoLinkedJsonNode newArchitectureNode, TwoLinkedJsonNode oldArchitectureNode) {		
		//Fire the changes to the views
		firePropertyChange("Architecture changed", oldArchitectureNode, newArchitectureNode);
		
		//The new node replace the old one
		m_projectsArchitectureRootNode = newArchitectureNode;
		generateFilesMap();
		
		//Save the new Json File
		saveJsonStructureFile();
		
		//Release the old tree
		oldArchitectureNode.releaseTree();
	}
	
	
	public void changeProject(TwoLinkedJsonNode newProjectNode) {
		//Get the old project node
		TwoLinkedJsonNode oldProjectNode = m_projectsArchitectureRootNode.getChild(newProjectNode.getName());
		
		//Set the new node in the architecture to replace the old one
		m_projectsArchitectureRootNode.removeChild(oldProjectNode.getName());
		m_projectsArchitectureRootNode.addChild(newProjectNode, false);
		
		generateFilesMap();
		
		//Fire the changes to the views		
		firePropertyChange("Project changed", null, m_projectsArchitectureRootNode);
		
		//Save the new Json File
		saveJsonStructureFile();
	}
	
	
	public void removeProject(TwoLinkedJsonNode oldProjectNode) {
		m_projectsArchitectureRootNode.removeChild(oldProjectNode.getName());
		oldProjectNode.releaseTree();
		firePropertyChange("Project changed", oldProjectNode, m_projectsArchitectureRootNode);
		generateFilesMap();
	}
	
	/**
	 * Generate a map to easily have the link between id and filename for a file
	 */
	private void generateFilesMap() {
		m_files.clear();
		
		for (TwoLinkedJsonNode projectNodes : m_projectsArchitectureRootNode.getChildren()){
			for (TwoLinkedJsonNode sessionNodes : projectNodes.getChildren()){
				for (TwoLinkedJsonNode fileNode : sessionNodes.getChildren()){	
					m_files.add(new CofitsFile(fileNode));
				}
			}
		}
	}
	
	
	/**
	 * 
	 */
	private void saveJsonStructureFile() {
		FilePathManager.getInstance().deletePath(PropertyManager.JSON_STRUCTURE_FILENAME);
		FilePathManager.getInstance().createTextFile(PropertyManager.JSON_STRUCTURE_FILENAME, m_projectsArchitectureRootNode.treeToJsonNodeString());
	}
	
	
	//Handle the users
	public void addUser(CofitsUser user) {
		m_users.add(user);		
	}
	
	
	public boolean hasUser(String text) {
		for (CofitsUser user : m_users){
			if (user.getLogin().equals(text)){
				return true;
			}
		}
		return false;
	}

	
	//Property Change methods
	public void addPropertyChangeListener(PropertyChangeListener listener){
		m_pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		m_pcs.removePropertyChangeListener(listener);
	}
	
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue){
		m_pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	
	//Getters & Setters
	public TwoLinkedJsonNode getProjectsArchitectureRootNode() {return m_projectsArchitectureRootNode;}
	
	public final List<CofitsFile> getFiles(){return m_files;}
	public final CofitsFile getFile(String filename){
		for (CofitsFile file : m_files){
			if (file.getFilename().equals(filename)){
				return file;
			}
		}
		return null;
	}
	
	public void setAgent(CofitsGuiAgent agent) {this.m_agent = agent;} //TODO find a way to only let the agent access this method
	
	//Members
	private PropertyChangeSupport m_pcs = new PropertyChangeSupport(this);
	private TwoLinkedJsonNode m_projectsArchitectureRootNode;
	private CofitsGuiAgent m_agent;
	private List<CofitsFile> m_files = new ArrayList<CofitsFile>();
	private List<CofitsUser> m_users = new ArrayList<CofitsUser>();

}
