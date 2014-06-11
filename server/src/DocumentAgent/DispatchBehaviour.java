package DocumentAgent;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.HashMap;

import Constants.CommunicationConstants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DispatchBehaviour extends CyclicBehaviour
{
	final MessageTemplate filter;
	DispatchBehaviour()
	{
		super();
		MessageTemplate f1 = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
		MessageTemplate f2 = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		this.filter = MessageTemplate.and(f1, f2);
	}
	
	@Override
	public void action()
	{
		final ACLMessage message = myAgent.receive(this.filter);
		if (message != null)
		{
			final String content =  message.getContent();
		    ObjectMapper mapper = new ObjectMapper();
			try
			{
				HashMap<String,String> req = mapper.readValue(content, new TypeReference<HashMap<String,String>>(){});
				if (message.getPerformative() == ACLMessage.SUBSCRIBE) {
					this.myAgent.addBehaviour(new AddSubscriberBehaviour(req, message));
				} else {
					switch((String)req.get("action"))
					{
//					case "create":
//						this.myAgent.addBehaviour(new AddBehaviour((AddRequest)req,message));
//						break;
					case "UPLOAD_FILE" :
						this.myAgent.addBehaviour(new UploadBehaviour(req, message));  // TODO
						break;
					case "DOWNLOAD_FILE" :
						this.myAgent.addBehaviour(new DownloadBehaviour(req, message));
						break;
					case "LIST" :
						this.myAgent.addBehaviour(new DownloadArchitectureBehaviour(req, message));
						break;
//					case "LIST_PROJECT" :
//						this.myAgent.addBehaviour(new DownloadProjectOverviewBehaviour(req, message));
//						break;
					case "ADD_USER" :
						this.myAgent.addBehaviour(new AddUserBehaviour(req, message));
						break;
//					case "CREATE_SESSION" :
//						this.myAgent.addBehaviour(new CreateSessionBehaviour(req, message));
//						break;
					case "quit":
						System.err.println("DispatchBehaviour.quit : Fonction non implémentée.");
						break;
					case "represent":
						System.err.println("DispatchBehaviour.represent : Fonction non implémentée.");
						break;
//					case "select":
//						this.myAgent.addBehaviour(new SelectBehaviour((SelectRequest) req,message));
//						break;
					default:
						break;
					}
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
