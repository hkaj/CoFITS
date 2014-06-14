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
	public DispatchBehaviour()
	{
		super();
		MessageTemplate f1 = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
		MessageTemplate f2 = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		this.filter = MessageTemplate.or(f1, f2);
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
					case "CREATE_PROJECT":
						this.myAgent.addBehaviour(new CreateProjectBehaviour(req,message));
						break;
//					case "REMOVE_PROJECT":
//						this.myAgent.addBehaviour(new RemoveProjectBehaviour(req,message)); // TODO
//						break;
					case "UPLOAD_FILE": // gerer la mise a jour d'un fichier et l'envoi du CONFIRM/REFUSE
						this.myAgent.addBehaviour(new UploadBehaviour(req, message));
						break;
//					case "REMOVE_FILE":
//						this.myAgent.addBehaviour(new RemoveFileBehaviour(req, message)); //TODO
//						break;
					case "DOWNLOAD_FILE":
						this.myAgent.addBehaviour(new DownloadBehaviour(req, message));
						break;
					case "LIST":
						this.myAgent.addBehaviour(new DownloadArchitectureBehaviour(req, message));
						break;
					case "LIST_PROJECT":
						this.myAgent.addBehaviour(new DownloadProjectOverviewBehaviour(req, message));
						break;
					case "ADD_USER":
						this.myAgent.addBehaviour(new AddUserBehaviour(req, message));
						break;
//					case "REMOVE_USER" :
//						this.myAgent.addBehaviour(new RemoveUserBehaviour(req, message));
//						break;
					case "CREATE_SESSION":
						this.myAgent.addBehaviour(new CreateSessionBehaviour(req, message));
						break;
//					case "REMOVE_SESSION":
//						this.myAgent.addBehaviour(new RemoveSessionBehaviour(req, message)); //TODO
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
}
