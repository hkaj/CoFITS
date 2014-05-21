package DocumentAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import Constants.RequestConstants;
import ModelObjects.BinaryContent;
import ModelObjects.Document;
import ModelObjects.ModelObject;
import Requests.DownloadRequest;

public final class DownloadBehaviour extends OneShotBehaviour
{
	final DownloadRequest request;
	final ACLMessage message;
	public DownloadBehaviour(DownloadRequest request, ACLMessage message)
	{
		this.request = request;
		this.message = message;
	}

	public DownloadBehaviour(Agent a, DownloadRequest request, ACLMessage message)
	{
		super(a);
		this.request = request;
		this.message = message;
	}

	@Override
	public void action()
	{
			final Document target = this.request.getSubject();
			Path path = Paths.get(RequestConstants.documentAgentDirectory+target.getName()+target.getType());
			BinaryContent res = null;
			final ACLMessage reply =this.message.createReply();
			final ObjectMapper mapper = new ObjectMapper();
			mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
			try
			{
				res = new BinaryContent(Files.readAllBytes(path));
				reply.setContent(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
			reply.setPerformative(ACLMessage.INFORM);
			this.myAgent.send(reply);
		}
}