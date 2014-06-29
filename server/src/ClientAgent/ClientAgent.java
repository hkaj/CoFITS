package ClientAgent;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;

import DocumentAgent.DocumentAgent;


public class ClientAgent extends Agent
{
	public static final String STATE_IDLE = "idle";
	public static final String STATE_PROJECT = "project";
	public static final String STATE_SESSION = "session";
	DataStore ds;
	public DemoBehaviour1 schedule;
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
//		schedule = new DemoBehaviour1();
		
//		SelectProjectsBehaviour spb = new SelectProjectsBehaviour(ds);
//		
//		SelectSessionsBehaviour ssb = new SelectSessionsBehaviour(ds,2);
		//schedule.registerFirstState(idle,STATE_IDLE);
		
//		schedule.registerFirstState(spb,STATE_PROJECT);
//		schedule.registerLastState(ssb,STATE_SESSION);
//		
//		schedule.registerTransition(STATE_PROJECT, STATE_SESSION,0);
				
		addBehaviour(new DemoBehaviour1());
		System.out.println(getLocalName() + " ---> deployed");
//		this.addBehaviour(new DemoBehaviour1());
//		this.addBehaviour(new ReceiveBehaviour());
//		this.addBehaviour(new SendRequestBehaviour(DocAgent[0].getName()));
	}
	
//	public void start() {
//		//schedule.start(STATE_PROJECT);
//		addBehaviour(this.schedule);
//	}
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
