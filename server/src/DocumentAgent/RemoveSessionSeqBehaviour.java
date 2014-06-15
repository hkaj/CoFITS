package DocumentAgent;

import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

public class RemoveSessionSeqBehaviour extends SequentialBehaviour {
	private ACLMessage message;
	private HashMap<String, String> request;
	private DocumentAgent docAgent;
	
	public RemoveSessionSeqBehaviour(HashMap<String, String> req, ACLMessage msg) {
		this.request = req;
		this.message = msg;
		this.docAgent = (DocumentAgent) myAgent;
//		this.addSubBehaviour(new Remove);
	}	
	
}
