package utc.bsfile.model;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.model.menu.TwoLinkedJsonNode;

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
		    
		    AgentController ac = cc.createNewAgent("tatin-cofits-agent", CofitsGuiAgent.class.getName() ,new Object[]{});
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
	public void setProjectsArchitectureRootNode(TwoLinkedJsonNode node) {
		TwoLinkedJsonNode oldValue = m_projectsArchitectureRootNode;
		m_projectsArchitectureRootNode = node;
		firePropertyChange("projectsArchitectureRootNode changed", oldValue, m_projectsArchitectureRootNode);
	}
	
	
	//Members
	private PropertyChangeSupport m_pcs = new PropertyChangeSupport(this);
	private TwoLinkedJsonNode m_projectsArchitectureRootNode;
	private CofitsGuiAgent m_agent;	//TODO Initialize it
}
