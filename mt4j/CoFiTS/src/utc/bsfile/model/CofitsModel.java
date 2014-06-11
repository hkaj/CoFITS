package utc.bsfile.model;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.FilePathManager;
import utc.bsfile.util.PropertyManager;

public class CofitsModel {
	
	private static String SECOND_PROPERTIES_FILE = "rsc/config/second.properties";

	public CofitsModel() {
		
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
		firePropertyChange("projectsArchitectureRootNode changed", oldArchitectureNode, newArchitectureNode);
		
		//The new node replace the old one
		m_projectsArchitectureRootNode = newArchitectureNode;
		generateFilesMap();
		
		//Save the new Json File
		FilePathManager.getInstance().deletePath(PropertyManager.JSON_STRUCTURE_FILENAME);
		FilePathManager.getInstance().createTextFile(PropertyManager.JSON_STRUCTURE_FILENAME, newArchitectureNode.treeToString());
		
		//Release the old tree
		oldArchitectureNode.releaseTree();
	}
	
	/**
	 * Generate a map to easily have the link between id and filename for a file
	 */
	private void generateFilesMap() {
		m_files.clear();
		
		for (TwoLinkedJsonNode projectNodes : m_projectsArchitectureRootNode.getChildren()){
			for (TwoLinkedJsonNode sessionNodes : projectNodes.getChildren()){
				for (TwoLinkedJsonNode fileNode : sessionNodes.getChildren()){	
					m_files.put(fileNode.getName(), new CofitsFile(fileNode));
				}
			}
		}
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
	
	public final Map<String,CofitsFile> getFiles(){return m_files;}
	public final CofitsFile getFile(String filename){return m_files.get(filename);}
	
	public void setAgent(CofitsGuiAgent agent) {this.m_agent = agent;} //TODO find a way to only let the agent access this method
	
	//Members
	private PropertyChangeSupport m_pcs = new PropertyChangeSupport(this);
	private TwoLinkedJsonNode m_projectsArchitectureRootNode;
	private CofitsGuiAgent m_agent;	//TODO Initialize it
	private Map<String, CofitsFile> m_files = new HashMap<String,CofitsFile>();

}
