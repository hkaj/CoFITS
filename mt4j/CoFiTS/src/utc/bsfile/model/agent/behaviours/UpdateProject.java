package utc.bsfile.model.agent.behaviours;

import com.fasterxml.jackson.databind.JsonNode;

import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.JsonManager;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class UpdateProject extends UpdateStructureBehaviour {

	public UpdateProject(Agent a, ACLMessage message){
		super(a, message);
	}
	
	@Override
	public void action() {
		JsonNode messageNode = JsonManager.getInstance().createJsonNode(m_message.getContent());
		TwoLinkedJsonNode messageContentNode = new TwoLinkedJsonNode(messageNode, "root", true);
		TwoLinkedJsonNode architectureNode = m_agent.getModel().getProjectsArchitectureRootNode();
		
		for (TwoLinkedJsonNode projectNode : messageContentNode.getChildren()){
			TwoLinkedJsonNode localProjectNode = architectureNode.getChild(projectNode.getName());
			
			if (messageContentNode.getChild("name") != null){
				mergeProjects(localProjectNode, projectNode);
				m_agent.getModel().changeProject(projectNode);
			} else {
				deleteProjectFromDisk(localProjectNode);
				m_agent.getModel().removeProject(localProjectNode);
			}
			
		}
		
		
	}
	
}
