package ClientAgent;

import java.util.ArrayList;

import Constants.RequestConstants;
import DocumentAgent.DocumentAgent;
import ModelObjects.ModelObject;
import ModelObjects.User;
import Requests.Predicate;
import Requests.SelectRequest;
import SpecialisedRelations.FollowedBy;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class ClientAgent extends Agent
{
	public static final String STATE_IDLE = "idle";
	public static final String STATE_PROJECT = "project";
	public static final String STATE_SESSION = "session";
	DataStore ds;
	DemoBehaviour schedule;
	public ClientAgent()
	{
		super();
	}
	
	@Override
	protected void setup()
	{
		
		ArrayList<ClientAgent> l = (ArrayList<ClientAgent>) getArguments()[0];
		l.add(this);
		final DFAgentDescription[] DocAgent = this.findDocumentAgent();
		ds = new DataStore();
		schedule = new DemoBehaviour();
		
		SelectProjectsBehaviour spb = new SelectProjectsBehaviour(ds);
		
		SelectSessionsBehaviour ssb = new SelectSessionsBehaviour(ds,2);
		//schedule.registerFirstState(idle,STATE_IDLE);
		schedule.registerFirstState(spb,STATE_PROJECT);
		schedule.registerLastState(ssb,STATE_SESSION);
		
		schedule.registerTransition(STATE_PROJECT, STATE_SESSION,0);
				
		//addBehaviour(schedule);
		System.out.println(getLocalName() + " ---> deployed");
//		this.addBehaviour(new DemoBehaviour1());
//		this.addBehaviour(new ReceiveBehaviour());
//		this.addBehaviour(new SendRequestBehaviour(DocAgent[0].getName()));
	}
	
	public void start() {
		//schedule.start(STATE_PROJECT);
		addBehaviour(schedule);
	}
	public DFAgentDescription[] findDocumentAgent()
	{
		final DFAgentDescription template = new DFAgentDescription();
		final ServiceDescription sd = new ServiceDescription();
		sd.setType(DocumentAgent.serviceType);
		sd.setName(DocumentAgent.serviceName);
		template.addServices(sd);
		try
		{
			DFAgentDescription[] result = DFService.search(this, template);
			if(result != null && result.length > 0)
			{
				return result;			
			}else
			{
				System.err.println("Aucun destinataire trouvé");
			}
		}catch(FIPAException fe)
		{
			fe.printStackTrace();
		}
		return null;
	}
}
