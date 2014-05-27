package ClientAgent;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

import Constants.RequestConstants;
import Constants.RequestConstants.objectTypes;
import ModelObjects.ModelObject;
import ModelObjects.Project;
import Requests.SelectRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SelectProjectsBehaviour extends Behaviour {
	int step = 0;
	MessageTemplate filter = MessageTemplate
			.MatchPerformative(ACLMessage.INFORM);

	public SelectProjectsBehaviour(DataStore ds) {
		setDataStore(ds);
	}

	@Override
	public void action() {
		switch (this.step) {
		case 0:
			AID dest = ((ClientAgent) this.myAgent).findDocumentAgent()[0]
					.getName();
			final SelectRequest request = new SelectRequest(objectTypes.project);
			final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.addReceiver(dest);
			message.setContent(request.toJSON());
			message.setConversationId(RequestConstants.SELECT_PROJECT_CID);
			this.myAgent.send(message);
			filter = MessageTemplate
					.MatchConversationId(RequestConstants.SELECT_PROJECT_CID);
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
				Project p = (Project) m;
				System.out.println(p.getId());
				System.out.println(p.getName());
				System.out.println(p.getDescription());
				System.out.println(p.getCreator());
				System.out.println("---------------");
			}
			getDataStore().put("results", res);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int onEnd() {
		System.out.println("onEnd called --- " + step);
		return done() ? 0 : 1;
	}

}
