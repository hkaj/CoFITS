package DocumentAgent;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;

import ModelObjects.Project;

public class UploadBehaviour extends OneShotBehaviour
{
	final HashMap<String, String> request;
	final ACLMessage message;
	ArrayList<Project> projects;

	public UploadBehaviour (HashMap<String, String> request, ACLMessage message) {
		this.request = request;
		this.message = message;
	}

	@Override
	public void action() {
		// Ne pas oublier de propager les changements aux subscribers
	}
	
}
