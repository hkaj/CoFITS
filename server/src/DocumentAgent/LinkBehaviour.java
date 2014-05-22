package DocumentAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import java.sql.SQLException;
import java.sql.Statement;

import Requests.LinkRequest;

public final class LinkBehaviour extends OneShotBehaviour
{
	final private LinkRequest request;
	public LinkBehaviour(LinkRequest request)
	{
		this.request = request;
	}

	public LinkBehaviour(Agent a, LinkRequest request)
	{
		super(a);
		this.request = request;
	}

	@Override
	public void action()
	{
		final String sql = this.request.toSQL();
		try
		{
			final Statement s = ((DocumentAgent)this.myAgent).createConnection().createStatement();
//			System.out.println(sql);
			s.execute(sql);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
}
