package ClientAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveBehaviour extends CyclicBehaviour
{
	final MessageTemplate filter;
	public ReceiveBehaviour()
	{
		super();
		this.filter = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	}

	public ReceiveBehaviour(Agent a)
	{
		super(a);
		this.filter = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	}

	@Override
	public void action()
	{
		final ACLMessage message = myAgent.receive(this.filter);
		if (message != null)
		{
			System.out.println("Message reçu");
			final String content =  message.getContent();
			System.out.println("ReceiveBehaviour : "+ content);			
		}
		else {block();}
	}

}
