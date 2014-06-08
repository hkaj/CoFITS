package ClientAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;

import Constants.RequestConstants;
import ModelObjects.Document;
import ModelObjects.Project;
import ModelObjects.Session;
import Requests.AddRequest;
import Requests.DownloadRequest;
import Requests.LinkRequest;
import Requests.Predicate;
import Requests.Relation;
import Requests.SelectRequest;
import Requests.UploadRequest;
import SpecialisedRelations.MobilizedIn;

public class SendRequestBehaviour extends OneShotBehaviour
{
	final AID dest;
	public SendRequestBehaviour(AID destination)
	{
		this.dest = destination;
	}

	public SendRequestBehaviour(Agent a,AID destination)
	{
		super(a);
		this.dest = destination;
	}

	private void t1()
	{
		ArrayList<Predicate> p = new ArrayList<Predicate>();
		SelectRequest sr = new SelectRequest(RequestConstants.objectTypes.document, p.toArray(new Predicate[0]));
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(sr.toJSON());
		this.myAgent.send(message);
	}
	
	private void t2()
	{
		final Document doc = new Document(0, "nf17", ".pdf");
		final AddRequest req = new AddRequest(doc);
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(req.toJSON());
		this.myAgent.send(message);
	}
	
	private void t3()
	{
		final Document doc = new Document(0, "nf17", ".pdf");
		final Path path = Paths.get(RequestConstants.clientAgentDirectory+doc.getName()+doc.getType());
		byte[] content = null;
		try
		{
			content= Files.readAllBytes(path);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		final UploadRequest request = new UploadRequest(doc, content);
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
//		System.out.println("Send request : "+request.toJSON());
		message.setContent(request.toJSON());
		this.myAgent.send(message);
	}
	
	private void t4()
	{
		final Document doc = new Document(0, "nf17", ".pdf");
		final DownloadRequest request = new DownloadRequest(doc);
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(request.toJSON());
		this.myAgent.send(message);
	}
	
	private void t5()
	{
		final Document doc = new Document(21, "le journal du lundi", "pdf");
		final Session session = new Session("projet sr04",new Timestamp(System.currentTimeMillis()).toString());
		final Project project = new Project("projet_sr04_0", "","","");
		System.out.println(session.getDate().toString());
		
		final AddRequest r1 = new AddRequest(doc);
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(r1.toJSON());
		this.myAgent.send(message);
		
		final AddRequest r2 = new AddRequest(session);
		message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(r2.toJSON());
		this.myAgent.send(message);
		
		final Relation rel = new MobilizedIn(project);
		final LinkRequest req = new LinkRequest(doc,rel);
		message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(req.toJSON());
		this.myAgent.send(message);
	}
	@Override
	public void action()
	{
		this.t5();
	}

}
