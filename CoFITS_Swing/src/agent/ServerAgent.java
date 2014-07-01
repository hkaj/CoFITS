package agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import uc1.ServerAnswerForProject;

public class ServerAgent extends Agent{
	
	protected void setup() {
		System.out.println(getLocalName()+ "--> Hello");
		
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Server");
		sd.setName("bam");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		ServerAnswerForProject b = new ServerAnswerForProject();
		addBehaviour(b);
		

		
	}
	
}
