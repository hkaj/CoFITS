package utc.bsfile.model.agent.behaviours;

import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.FilePathManager;
import utc.bsfile.util.JsonManager;
import utc.bsfile.util.PropertyManager;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class UpdateProjectsStructure extends OneShotBehaviour {

	public UpdateProjectsStructure(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
		m_guiAgent = (CofitsGuiAgent) a;
	}

	@Override
	public void action() {
		
		//Create JsonNode from content of message
		JsonNode jsonNode = JsonManager.getInstance().createJsonNode(m_message.getContent());
		
		if (jsonNode != null){
			
			//Create the new TwoLinkedTree
			TwoLinkedJsonNode newNodeTree = new TwoLinkedJsonNode(jsonNode, "", true);
			TwoLinkedJsonNode oldNodeTree = m_guiAgent.getModel().getProjectsArchitectureRootNode();
			
			if (oldNodeTree != null){
				//Merge the trees and process modification in memory
				mergeArchitectureTrees(oldNodeTree, newNodeTree);
			} else {				
				//Create the folders
				String separator = FileSystems.getDefault().getSeparator();
				String initialPath = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
				initialPath += initialPath.charAt(initialPath.length() - 1) == separator.charAt(0) ? separator : "";
				
				for (TwoLinkedJsonNode newProjectNode : newNodeTree.getChildren()){	
					for (TwoLinkedJsonNode newSessionNode : newProjectNode.getChildren()){
						String path = initialPath + newProjectNode.getName() + separator + newSessionNode.getName() + separator;
						FilePathManager.getInstance().createFolder(path);
					}
				}
			}
			
			//The new node replace the old one
			m_guiAgent.getModel().setProjectsArchitectureRootNode(newNodeTree);
			
			//Release the old tree
			oldNodeTree.releaseTree();
			
			//Save the new Json File
			FilePathManager.getInstance().deletePath(PropertyManager.JSON_STRUCTURE_FILENAME);
			FilePathManager.getInstance().createTextFile(PropertyManager.JSON_STRUCTURE_FILENAME, newNodeTree.treeToString());
		} 

	}
	
	
	private void mergeArchitectureTrees(TwoLinkedJsonNode oldArchitectureNode, TwoLinkedJsonNode newArchitectureNode){
		
		for (TwoLinkedJsonNode newProjectNode : newArchitectureNode.getChildren()){
			
			TwoLinkedJsonNode oldProjectNode = null;
			for (TwoLinkedJsonNode oldNode : oldArchitectureNode.getChildren()){
				if (oldNode.getName().equals(newProjectNode.getName())){
					oldProjectNode = oldNode;
					break;
				}
			}
			
			mergeProjects(oldProjectNode, newProjectNode);
			
		}
		
		//If the session is on the device but not on server, delete it
		for (TwoLinkedJsonNode oldProjectNode : oldArchitectureNode.getChildren()){
			boolean doDeleteProject = true;
			for (TwoLinkedJsonNode newNode : newArchitectureNode.getChildren()){
				if (newNode.getName().equals(oldProjectNode.getName())){
					doDeleteProject = false;
					break;
				}
			}
			
			if (doDeleteProject){
				String separator = FileSystems.getDefault().getSeparator();
				String path = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
				path += path.charAt(path.length() - 1) == separator.charAt(0) ? separator : "";
				path += oldProjectNode.getName();
				
				System.out.println("Project path to delete : " + path);
				
				FilePathManager.getInstance().deletePath(path);
			}
		}
		
	}
	
	
	private void mergeProjects(TwoLinkedJsonNode oldProjectNode, TwoLinkedJsonNode newProjectNode) {
		
		//Check if the projects exists in the old tree
		if (oldProjectNode != null){
			
			for (TwoLinkedJsonNode newSessionNode : newProjectNode.getChildren()){
				
				TwoLinkedJsonNode oldSessionNode = null;
				for (TwoLinkedJsonNode oldNode : oldProjectNode.getChildren()){
					if (oldNode.getName().equals(newSessionNode.getName())){
						oldSessionNode = oldNode;
						break;
					}
				}
				
				mergeSessions(oldSessionNode, newSessionNode);
				
			}
			
			//If the session is on the device but not on server, delete it
			for (TwoLinkedJsonNode oldSessionNode : oldProjectNode.getChildren()){
				boolean doDeleteSession = true;
				for (TwoLinkedJsonNode newNode : newProjectNode.getChildren()){
					if (newNode.getName().equals(oldSessionNode.getName())){
						doDeleteSession = false;
						break;
					}
				}
				
				if (doDeleteSession){
					String separator = FileSystems.getDefault().getSeparator();
					String path = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
					path += path.charAt(path.length() - 1) == separator.charAt(0) ? separator : "";
					path += oldProjectNode.getName() + separator + oldSessionNode.getName();
					
					System.out.println("Session path to delete : " + path);
					
					FilePathManager.getInstance().deletePath(path);
				}
			}
			
		} else {
			//Create the folders
			String separator = FileSystems.getDefault().getSeparator();
			String initialPath = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
			initialPath += initialPath.charAt(initialPath.length() - 1) == separator.charAt(0) ? separator : "";
			
			for (TwoLinkedJsonNode newSessionNode : newProjectNode.getChildren()){			
				String path = initialPath + newProjectNode.getName() + separator + newSessionNode.getName() + separator;
				FilePathManager.getInstance().createFolder(path);
			}
		}
		
	}


	private void mergeSessions(TwoLinkedJsonNode oldSessionNode, TwoLinkedJsonNode newSessionNode) {
		
		//Check if the sessions exists in the old tree
		if (oldSessionNode != null){
			
			//Merge each file
			for (TwoLinkedJsonNode newFileNode : newSessionNode.getChildren()){
				TwoLinkedJsonNode oldFileNode = null;
				for (TwoLinkedJsonNode oldNode : oldSessionNode.getChildren()){
					if (oldNode.getName().equals(newFileNode.getName())){
						oldFileNode = oldNode;
						break;
					}
				}
				
				mergeFiles(oldFileNode, newFileNode);
				
			}
			
			//If the file is on the device but not on server, delete it
			for (TwoLinkedJsonNode oldFileNode : oldSessionNode.getChildren()){
				if(oldFileNode.getCurrent().path("local").asBoolean()){	
					boolean doDeleteFile = true;
					for (TwoLinkedJsonNode newNode : newSessionNode.getChildren()){
						if (newNode.getName().equals(oldFileNode.getName())){
							doDeleteFile = false;
							break;
						}
					}
					
					if (doDeleteFile){
						TwoLinkedJsonNode oldProjectNode = (TwoLinkedJsonNode) oldSessionNode.getParent();
						
						String separator = FileSystems.getDefault().getSeparator();
						String path = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
						path += path.charAt(path.length() - 1) == separator.charAt(0) ? separator : "";
						path += oldProjectNode.getName() + separator + oldSessionNode.getName() + separator + oldFileNode.getName();
						
						FilePathManager.getInstance().deletePath(path);
					}
				}
			}
			
		} else {
			//Create the folder
			TwoLinkedJsonNode oldProjectNode = (TwoLinkedJsonNode) newSessionNode.getParent();
			String separator = FileSystems.getDefault().getSeparator();
			String path = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
			path += path.charAt(path.length() - 1) == separator.charAt(0) ? separator : "";
			path += oldProjectNode.getName() + separator + newSessionNode.getName() + separator;
			
			FilePathManager.getInstance().createFolder(path);
		}
		
	}


	private void mergeFiles(TwoLinkedJsonNode oldFileNode, TwoLinkedJsonNode newFileNode) {
		
		if (oldFileNode == null){
			//Add the local field to new node
			ObjectNode newJsonNode = (ObjectNode) newFileNode.getCurrent();
			newJsonNode.put("local", false);
			newFileNode.addChild(new TwoLinkedJsonNode(newJsonNode.path("local"), "local"), true);
			
		} else {
			if (oldFileNode.getCurrent().path("local").asBoolean()){
				//Check whether the file on the table is up to date or not
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");	
			 
				try {
					Date oldDate = formatter.parse(oldFileNode.getCurrent().path("last-modified").asText());
					Date newDate = formatter.parse(newFileNode.getCurrent().path("last-modified").asText());
					
					if (oldDate.before(newDate)){
						//TODO Behaviour to download the file
						System.out.println("Let's download the files !");
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			
			//Add the local field to newNode from oldNode
			ObjectNode newJsonNode = (ObjectNode) newFileNode.getCurrent();
			newJsonNode.put("local", oldFileNode.getCurrent().path("local").asBoolean());
			newFileNode.addChild(new TwoLinkedJsonNode(oldFileNode.getCurrent().path("local"), "local"), false);
		}
		
	}
	

	//Members
	private ACLMessage m_message;
	private CofitsGuiAgent m_guiAgent;
}
