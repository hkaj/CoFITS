package DocumentAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.sql.SQLException;
import java.sql.Statement;

import Requests.AddRequest;

public class AddBehaviour extends OneShotBehaviour
{

	final AddRequest req;
	final ACLMessage mes;
	public AddBehaviour(AddRequest r, ACLMessage m)
	{
		super();
		this.req=r;
		this.mes = m;
	}

	public AddBehaviour(Agent a, AddRequest r,ACLMessage m)
	{
		super(a);
		this.mes = m;
		this.req=r;
	}

	@Override
	public void action()
	{
		final String sql = this.req.toSQL();
//		System.out.println("AddBehaviour : "+sql);
		try
		{
			final Statement s = ((DocumentAgent)this.myAgent).createConnection().createStatement();
			s.execute(sql);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
