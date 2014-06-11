package ClientAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.FileOutputStream;
import java.io.IOException;

import Constants.RequestConstants;
import Constants.RequestConstants.objectTypes;
import ModelObjects.BinaryContent;
import ModelObjects.Document;
import ModelObjects.ModelObject;
import ModelObjects.Project;
import ModelObjects.Session;
import Requests.DownloadRequest;
import Requests.Predicate;
import Requests.SelectRequest;
import SpecialisedRelations.MobilizedIn;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class DemoBehaviour2 extends Behaviour
{

	int step = 0;
	AID dest;
	Session session;
	Project project;
	Document doc;
//	MessageTemplate filter=null;
	public DemoBehaviour2()
	{
	}
	
	private void selectSession()
	{
		this.dest = ((ClientAgent) this.myAgent).findDocumentAgent()[0].getName();
		final SelectRequest request = new SelectRequest(objectTypes.session, new Predicate[0]);
		final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(request.toJSON());
		this.myAgent.send(message);
		final ACLMessage answer = this.myAgent.blockingReceive();
		final ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		
		ModelObject[] res=null;
		try
		{
//			System.out.println("Sessions reçues :"+answer.getContent());
			res = mapper.readValue(answer.getContent(), ModelObject[].class);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.session = (Session)res[res.length-1];
	}
	
	private void selectDocument()
	{
		Predicate p[] ={new MobilizedIn(project)};
		final SelectRequest request = new SelectRequest(objectTypes.document,p );
		final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(this.dest);
		message.setContent(request.toJSON());
		this.myAgent.send(message);
		final ACLMessage answer = this.myAgent.blockingReceive();
		final ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		
		ModelObject[] res=null;
		try
		{
//			System.out.println("Documents reçus :"+answer.getContent());
			res = mapper.readValue(answer.getContent(), ModelObject[].class);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.doc = (Document)res[0];
	}

//	private void downloadDocument()
//	{
//		
//		final DownloadRequest request = new DownloadRequest(doc);
//		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
//		message.addReceiver(this.dest);
//		message.setContent(request.toJSON());
//		this.myAgent.send(message);
//		
//		
//		final ACLMessage answer = this.myAgent.blockingReceive();
//		
//		final ObjectMapper mapper = new ObjectMapper();
//		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		
//		BinaryContent res=null;
//		try
//		{
////			System.out.println("Sessions reçues :"+answer.getContent());
//			res = mapper.readValue(answer.getContent(), BinaryContent.class);
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		
//		final String path = RequestConstants.clientAgentDirectory+this.doc.getName()+this.doc.getType();
//		FileOutputStream fos;
//		try
//		{
//			fos = new FileOutputStream(path);
//			fos.write(res.getContent());
//			fos.close();
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	public DemoBehaviour2(Agent a)
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
			this.selectSession();
			break;
		case 1 :
			this.selectDocument();
			break;
//		case 2 :
//			this.downloadDocument();
//			break;
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
		return this.step == 3;
	}

}
