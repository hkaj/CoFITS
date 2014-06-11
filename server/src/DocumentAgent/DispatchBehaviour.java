package DocumentAgent;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.HashMap;

import Constants.CommunicationConstants;
import Requests.AddRequest;
import Requests.DownloadRequest;
import Requests.LinkRequest;
import Requests.SelectRequest;
import Requests.UploadRequest;

import com.fasterxml.jackson.core.type.TypeReference;
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
//			System.out.println("DispatchBehaviour : "+ content);
		    ObjectMapper mapper = new ObjectMapper();
			try
			{
				HashMap<String,String> req = mapper.readValue(content, new TypeReference<HashMap<String,String>>(){});
				switch((String)req.get("action"))
				{
//				case "create":
//					this.myAgent.addBehaviour(new AddBehaviour((AddRequest)req,message));
//					break;
				case "UPLOAD_FILE" :
					this.myAgent.addBehaviour(new UploadBehaviour(req, message));
					break;
				case "DOWNLOAD_FILE" :
					this.myAgent.addBehaviour(new DownloadBehaviour(req, message));
					break;
				case "LIST" :
					this.myAgent.addBehaviour(new DownloadArchitectureBehaviour(req, message));
					break;
				case "LIST_PROJECT" :
					this.myAgent.addBehaviour(new DownloadProjectOverviewBehaviour(req, message));
					break;
				case "ADD_USER" :
					this.myAgent.addBehaviour(new AddUserBehaviour(req, message));
					break;
				case "CREATE_SESSION" :
					this.myAgent.addBehaviour(new CreateSessionBehaviour(req, message));
					break;
				case "quit":
					System.err.println("DispatchBehaviour.quit : Fonction non implémentée.");
					break;
				case "represent":
					System.err.println("DispatchBehaviour.represent : Fonction non implémentée.");
					break;
//				case "select":
//					this.myAgent.addBehaviour(new SelectBehaviour((SelectRequest) req,message));
//					break;
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
