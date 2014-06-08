package ClientAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

import Constants.RequestConstants;
import Constants.RequestConstants.objectTypes;
import ModelObjects.Document;
import ModelObjects.ModelObject;
import ModelObjects.Project;
import ModelObjects.Session;
import Requests.AddRequest;
import Requests.Filter;
import Requests.LinkRequest;
import Requests.Predicate;
import Requests.Relation;
import Requests.SelectRequest;
import Requests.UploadRequest;
import SpecialisedRelations.MobilizedIn;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class DemoBehaviour1 extends Behaviour
{

	int step = 0;
//	MessageTemplate filter=null;
	final Session session = new Session("projet_ia04_0",new Timestamp(System.currentTimeMillis()).toString());
	Document doc = new Document(0,"nf11cours-2013",".pdf");
	final Project project = new Project("project_ia04_0", "", "", "");
	AID dest;
	public DemoBehaviour1()
	{
	}
	
	private void createSession()
	{
		this.dest = ((ClientAgent) this.myAgent).findDocumentAgent()[0].getName();
		final AddRequest request = new AddRequest(session);
		final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(request.toJSON());
		System.out.println("createSession message: " + message);
		this.myAgent.send(message);
	}
	
	private void insertDocument()
	{
		final AddRequest request = new AddRequest(doc);
		final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(request.toJSON());
		System.out.println("insertDocument message: " + message);
		this.myAgent.send(message);
	}

	private void selectDocument()
	{
		Predicate[] p = {new Filter("name","nf11cours-2013"),new Filter("type",".pdf")};
		final SelectRequest request = new SelectRequest(objectTypes.document, p);
		final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(request.toJSON());
		System.out.println("selectDocument message: " + message);
		this.myAgent.send(message);
		System.out.println("waiting for the response...");
		final ACLMessage answer = this.myAgent.blockingReceive();
		System.out.println("Response : " + answer.getContent());
		final ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		
		ModelObject[] res=null;
		try
		{
//			System.out.println("Documents reçus : "+answer.getContent());
			res = mapper.readValue(answer.getContent(), ModelObject[].class);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.doc = (Document)res[0];
	}
	
	private void linkDocument()
	{
		final Relation rel = new MobilizedIn(project);
		final LinkRequest req = new LinkRequest(doc,rel);
		final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(req.toJSON());
		this.myAgent.send(message);
	}
	
	private void uploadDocument()
	{
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
		final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(request.toJSON());
		this.myAgent.send(message);
	}
	
	public DemoBehaviour1(Agent a)
	{
		super(a);
	}

	@Override
	public void action()
	{
		System.out.println("Étape n°"+this.step);
		switch(this.step)
		{
		case 0 :
			this.createSession();
			break;
		case 1 :
			this.insertDocument();
			break;
		case 2 :
			this.selectDocument();
			break;
		case 3 :
			this.linkDocument();
			break;
		case 4 :
			this.uploadDocument();
			break;
		}
		try
		{
			System.out.println("Passer à l'étape suivante ?");
			System.in.read();
			this.step++;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		

	}

	@Override
	public boolean done()
	{
		return this.step == 5;
	}

}
