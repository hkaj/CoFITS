package DocumentAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Constants.DataBaseConstants;

public class DocumentAgent extends Agent
{
	final static public String serviceName ="DocumentService";
	final static public String serviceType ="ProjectManagement";
	private Map<String, List<AID>> subscribedAgents;

	public DocumentAgent()
	{
		super();
		subscribedAgents = new HashMap<String, List<AID>>();
	}
	
	@Override
	protected void setup()
	{
		this.addBehaviour(new DispatchBehaviour());
		this.register();
		System.out.println(getLocalName() + " ---> deployed");
	}
	
	protected void register()
	{
		final DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		final ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
		sd.setName(serviceName);
		dfd.addServices(sd);
		try
		{
			DFService.register(this, dfd);
		}catch(FIPAException fe)
		{
			fe.printStackTrace();
		}
	}
	
	public Connection createConnection() throws SQLException {

		Connection conn = null;
		conn = DriverManager.getConnection(
				"jdbc:postgresql://" 
						+ DataBaseConstants.host + "/"
						+ DataBaseConstants.databaseName,
						DataBaseConstants.userName,DataBaseConstants.password);
		System.out.println("Connected to database");
	    return conn;
	}
}
