package utc.bsfile.model.agent.behaviours;

import java.nio.file.FileSystems;

import com.fasterxml.jackson.databind.JsonNode;

import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.FilePathManager;
import utc.bsfile.util.JsonManager;
import utc.bsfile.util.PropertyManager;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class UpdateProjectsStructure extends UpdateStructureBehaviour {

	public UpdateProjectsStructure(Agent a, ACLMessage message) {
		super(a, message);
	}

	@Override
	public void action() {		
		//Create JsonNode from content of message
		JsonNode jsonNode = JsonManager.getInstance().createJsonNode(m_message.getContent());
		
		if (jsonNode != null){
			
			//Create the new TwoLinkedTree
			TwoLinkedJsonNode newNodeTree = new TwoLinkedJsonNode(jsonNode, "root", true);
			TwoLinkedJsonNode oldNodeTree = m_agent.getModel().getProjectsArchitectureRootNode();
			
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
		
			
			m_agent.getModel().changeProjectArchitecture(newNodeTree, oldNodeTree);
		} 

	}
}
