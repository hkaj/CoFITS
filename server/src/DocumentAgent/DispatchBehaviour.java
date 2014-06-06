package DocumentAgent;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

import Constants.CommunicationConstants;
import Requests.AddRequest;
import Requests.BaseRequest;
import Requests.DownloadRequest;
import Requests.LinkRequest;
import Requests.SelectRequest;
import Requests.UploadRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DispatchBehaviour extends CyclicBehaviour
{
	final MessageTemplate filter;
	DispatchBehaviour()
	{
		super();
		this.filter = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		
	}
	
	@Override
	public void action()
	{
		final ACLMessage message = myAgent.receive(this.filter);
		if (message != null)
		{
			final String content =  message.getContent();
			System.out.println("DispatchBehaviour : "+ content);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
			BaseRequest req;
			try
			{
				req = mapper.readValue(content, BaseRequest.class);
				switch(req.getRequestType())
				{
				case create:
					this.myAgent.addBehaviour(new AddBehaviour((AddRequest)req,message));
					break;
				case upload :
					this.myAgent.addBehaviour(new UploadBehaviour((UploadRequest)req));
					break;
				case download :
					this.myAgent.addBehaviour(new DownloadBehaviour((DownloadRequest)req,message));
					break;
				case link:
					this.myAgent.addBehaviour(new LinkBehaviour((LinkRequest)req));
					break;
				case quit:
					System.err.println("DispatchBehaviour.quit : Fonction non implémentée.");
					break;
				case represent:
					System.err.println("DispatchBehaviour.represent : Fonction non implémentée.");
					break;
				case select:
					this.myAgent.addBehaviour(new SelectBehaviour((SelectRequest) req,message));
					break;
				default:
					break;
				}
			} catch (IOException e)
			{
				final ACLMessage rep = message.createReply();
				rep.setPerformative(ACLMessage.REFUSE);
				rep.setContent(CommunicationConstants.InvalidRequest);
				this.myAgent.send(rep);
				e.printStackTrace();
			}				
		}
		else {block();}
		
	}
	
	private boolean isAValidRequest(String r)
	{
		return true;
	}

}
