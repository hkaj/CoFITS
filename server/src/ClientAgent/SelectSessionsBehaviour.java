package ClientAgent;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import Constants.RequestConstants;
import Constants.RequestConstants.objectTypes;
import ModelObjects.ModelObject;
import ModelObjects.Project;
import ModelObjects.Session;
import Requests.Filter;
import Requests.Predicate;
import Requests.SelectRequest;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SelectSessionsBehaviour extends Behaviour {
	int p;
	int step = 0;
	MessageTemplate filter = MessageTemplate
			.MatchPerformative(ACLMessage.INFORM);

	public SelectSessionsBehaviour(DataStore ds, int n) {
		super();
		this.p = n;
		setDataStore(ds);
	}

	@Override
	public void action() {
		switch (this.step) {
		case 0:
			AID dest = ((ClientAgent) this.myAgent).findDocumentAgent()[0]
					.getName();
			ModelObject[] m = (ModelObject[]) getDataStore().get("results");
			//Project prj = (Project) m[p];
			if (m != null)
				System.out.println("Results length = " + m.length);
			else
				System.out.println("null --> Results length = 0");
			Project prj = (Project) m[p-1];
			Predicate[] pds = {new Filter("name",prj.getName())};
			final SelectRequest request = new SelectRequest(objectTypes.session,pds);
			final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.addReceiver(dest);
			message.setContent(request.toJSON());
			message.setConversationId(RequestConstants.SELECT_SESSION_CID);
			this.myAgent.send(message);
			filter = MessageTemplate
					.MatchConversationId(RequestConstants.SELECT_SESSION_CID);
			step++;
		case 1:
			final ACLMessage answer = myAgent.receive(this.filter);
			if (answer != null) {
				System.out.println("Message reçu");
				print(answer);
				step++;
			} else {
				block();
			}
		}

	}

	@Override
	public boolean done() {
		return step >= 2;
	}

	private void print(ACLMessage answer) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

		ModelObject[] res = null;
		// System.out.println(answer.getContent());
		try {
			res = mapper.readValue(answer.getContent(), ModelObject[].class);
			for (ModelObject m : res) {
				Session p = (Session) m;
				System.out.println(p.getProject());
				System.out.println(p.getDate());
				System.out.println("---------------");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
