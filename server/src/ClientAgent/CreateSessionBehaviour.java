package ClientAgent;

import ModelObjects.Session;
import Requests.AddRequest;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class CreateSessionBehaviour extends OneShotBehaviour {
  private Session session;
  
	public CreateSessionBehaviour(Session session) {
	super();
	this.session = session;
}

	@Override
	public void action() {
		AID dest = ((ClientAgent) this.myAgent).findDocumentAgent()[0].getName();
		final AddRequest request = new AddRequest(session);
		final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(dest);
		message.setContent(request.toJSON());
		this.myAgent.send(message);
	}

}
