package utc.bsfile.model.agent.behaviours;

import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.node.ObjectNode;

import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.FilePathManager;
import utc.bsfile.util.PropertyManager;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public abstract class UpdateStructureBehaviour extends OneShotBehaviour {

	public UpdateStructureBehaviour(Agent a, ACLMessage message) {
		m_agent = (CofitsGuiAgent) a;
		m_message = message;
	}
	
	
protected void mergeArchitectureTrees(TwoLinkedJsonNode oldArchitectureNode, TwoLinkedJsonNode newArchitectureNode){
	
//		newArchitectureNode.displayConsole(0);
//		System.out.println();System.out.println();
		
		
		for (TwoLinkedJsonNode newProjectNode : newArchitectureNode.getChildren()){
			TwoLinkedJsonNode oldProjectNode = null;
			
			//Find the old node
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
				deleteProjectFromDisk(oldProjectNode);
			}
		}
		
	}



	
	
	protected void mergeProjects(TwoLinkedJsonNode oldProjectNode, TwoLinkedJsonNode newProjectNode) {
		
//		System.out.println("PROJECT");
//		newProjectNode.displayConsole(0);
//		System.out.println();System.out.println();
		
		//Check if the projects exists in the old tree
		if (oldProjectNode != null){
			
			for (TwoLinkedJsonNode newSessionNode : newProjectNode.getChildren()){
				if (newSessionNode.getChild("id") != null){
					int newId = newSessionNode.getCurrent().path("id").asInt();
//					System.out.println("    New Session : "+ newSessionNode.getName());
					TwoLinkedJsonNode oldSessionNode = null;
					
					for (TwoLinkedJsonNode oldNode : oldProjectNode.getChildren()){
						if (oldNode.getChild("id") != null){
//							System.out.println("Old session : " + oldNode.getName());
							if (oldNode.getCurrent().path("id").asInt() == newId){
								oldSessionNode = oldNode;
//								System.out.println();
								break;						
							}
						}
					}
					
					mergeSessions(oldSessionNode, newSessionNode);
				}
				
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
					deleteSessionFromDisk(oldProjectNode, oldSessionNode);
				}
			}
			
		} else {
			//Create the folders
			String separator = FileSystems.getDefault().getSeparator();
			String initialPath = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
			initialPath += initialPath.charAt(initialPath.length() - 1) == separator.charAt(0) ? separator : "";
			
			for (TwoLinkedJsonNode newSessionNode : newProjectNode.getChildren()){		
				if (newSessionNode.getCurrent() instanceof ObjectNode){
					String path = initialPath + newProjectNode.getName() + separator + Integer.toString(newSessionNode.getCurrent().path("id").asInt()) + separator;
					FilePathManager.getInstance().createFolder(path);
				}
			}
			
			//Set local parameter
			for (TwoLinkedJsonNode newSessionNode : newProjectNode.getChildren()){	
				for (TwoLinkedJsonNode newFileNode : newSessionNode.getChildren()){
					if (newFileNode.getChild("id") != null){
						ObjectNode newJsonNode = (ObjectNode) newFileNode.getCurrent();
						newJsonNode.put("local", false);
						newFileNode.addChild(new TwoLinkedJsonNode(newJsonNode.path("local"), "local"), true);
					}
				}
			}
		}
		
	}


	protected void mergeSessions(TwoLinkedJsonNode oldSessionNode, TwoLinkedJsonNode newSessionNode) {
		
//		System.out.println("SESSION");
//		newSessionNode.displayConsole(0);
//		System.out.println(oldSessionNode);
//		System.out.println();System.out.println();
		
		//Check if the sessions exists in the old tree
		if (oldSessionNode != null){
			
			//Merge each file
			for (TwoLinkedJsonNode newFileNode : newSessionNode.getChildren()){
				if (newFileNode.getChild("id") != null){
					int newId = newFileNode.getCurrent().path("id").asInt();
					TwoLinkedJsonNode oldFileNode = null;
					
	//				System.out.println("    new node : " + newFileNode.getName());
					for (TwoLinkedJsonNode oldNode : oldSessionNode.getChildren()){
						if (oldNode.getChild("id") != null){
	//					System.out.println("old node : "+oldNode.getName());
							if (oldNode.getCurrent().path("id").asInt() == newId){
								oldFileNode = oldNode;
								break;
							}
						}
					}
				
					mergeFiles(oldFileNode, newFileNode);
				}
				
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
						deleteFileFromDisk(oldSessionNode, oldFileNode);
					}
				}
			}
			
		} else {
			//Create the folder
			TwoLinkedJsonNode oldProjectNode = (TwoLinkedJsonNode) newSessionNode.getParent();
			String separator = FileSystems.getDefault().getSeparator();
			String path = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
			path += path.charAt(path.length() - 1) == separator.charAt(0) ? separator : "";
			path += oldProjectNode.getName() + separator + newSessionNode.getCurrent().path("id").asInt() + separator;
			
			FilePathManager.getInstance().createFolder(path);
			
			//Set local parameter
			for (TwoLinkedJsonNode newFileNode : newSessionNode.getChildren()){
				if (newFileNode.getChild("id") != null){
					ObjectNode newJsonNode = (ObjectNode) newFileNode.getCurrent();
					newJsonNode.put("local", false);
					newFileNode.addChild(new TwoLinkedJsonNode(newJsonNode.path("local"), "local"), true);
				}
			}
		}
		
	}


	protected void mergeFiles(TwoLinkedJsonNode oldFileNode, TwoLinkedJsonNode newFileNode) {
		
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
						m_agent.addBehaviour(new RequestDownloadFile(m_agent,oldFileNode.getCurrent().path("id").asInt()));
						System.out.println("Let's download the files !");
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			
			//Add the local field to newNode from oldNode
			if (newFileNode.getChild("id") != null){
				ObjectNode newJsonNode = (ObjectNode) newFileNode.getCurrent();
				newJsonNode.put("local", oldFileNode.getCurrent().path("local").asBoolean());
				newFileNode.addChild(new TwoLinkedJsonNode(oldFileNode.getCurrent().path("local"), "local"), false);
			}
		}
		
	}
	
	
	/**
	 * @param oldProjectNode
	 */
	protected void deleteProjectFromDisk(TwoLinkedJsonNode oldProjectNode) {
		String separator = FileSystems.getDefault().getSeparator();
		String path = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
		path += path.charAt(path.length() - 1) == separator.charAt(0) ? separator : "";
		path += oldProjectNode.getName();
		
		System.out.println("Project path to delete : " + path);
		
		FilePathManager.getInstance().deletePath(path);
	}
	
	
	/**
	 * @param oldProjectNode
	 * @param oldSessionNode
	 */
	protected void deleteSessionFromDisk(TwoLinkedJsonNode oldProjectNode,
			TwoLinkedJsonNode oldSessionNode) {
		String separator = FileSystems.getDefault().getSeparator();
		String path = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
		path += path.charAt(path.length() - 1) == separator.charAt(0) ? separator : "";
		path += oldProjectNode.getName() + separator + oldSessionNode.getCurrent().path("id").asInt();
		
		System.out.println("Session path to delete : " + path);
		
		FilePathManager.getInstance().deletePath(path);
	}
	
	
	/**
	 * @param oldSessionNode
	 * @param oldFileNode
	 */
	protected void deleteFileFromDisk(TwoLinkedJsonNode oldSessionNode,
			TwoLinkedJsonNode oldFileNode) {
		TwoLinkedJsonNode oldProjectNode = (TwoLinkedJsonNode) oldSessionNode.getParent();
		
		String separator = FileSystems.getDefault().getSeparator();
		String path = PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH);
		path += path.charAt(path.length() - 1) == separator.charAt(0) ? separator : "";
		path += oldProjectNode.getName() + separator + oldSessionNode.getCurrent().path("id").asInt() + separator + oldFileNode.getName();
		
		FilePathManager.getInstance().deletePath(path);
	}
	
	
	//Members
	protected CofitsGuiAgent m_agent;
	protected ACLMessage m_message;
}
