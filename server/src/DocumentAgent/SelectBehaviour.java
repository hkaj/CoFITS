package DocumentAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import ModelObjects.Document;
import ModelObjects.ModelObject;
import ModelObjects.Project;
import ModelObjects.Session;
import Requests.SelectRequest;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class SelectBehaviour extends OneShotBehaviour
{

	final SelectRequest req;
	final ACLMessage mes;
	public SelectBehaviour(SelectRequest r, ACLMessage m)
	{
		super();
		this.req=r;
		this.mes = m;
	}

	public SelectBehaviour(Agent a, SelectRequest r,ACLMessage m)
	{
		super(a);
		this.mes = m;
		this.req=r;
	}

	@Override
	public void action()
	{
		final String sql = req.toSQL();
//		System.out.println("SelectBehaviour : objet recu : "+req.toJSON());
//		System.out.println("SelectBehaviour : requête reçue :"+sql);
		final Statement s ;
		final List<ModelObject> listRes = new LinkedList<ModelObject>();
		try
		{
			s = ((DocumentAgent)this.myAgent).createConnection().createStatement();
			final ResultSet res = s.executeQuery(sql);
			while (res.next())
			{
				final ModelObject element;
				//Création de l'objet
				switch(req.getTargetObject())
				{
				case document:
					element = new Document(res);
					break;
				case project:
					element = new Project(res);
					break;
				case session:
					element = new Session(res);
					break;
//				case user:
//					element = new User(res);
//					break;
				default:
					element = null;
					break;
				}
				listRes.add(element);
			}
			s.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		final ACLMessage answer = this.mes.createReply();
		
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibilityChecker(
			     mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY)
			  );
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL );
		try
		{
			answer.setContent(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listRes.toArray(new ModelObject[0])));
//			System.out.println("Resultat :"+mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listRes));
		} catch (JsonProcessingException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.myAgent.send(answer);
	}

}
