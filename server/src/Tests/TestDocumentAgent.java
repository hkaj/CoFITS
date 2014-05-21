package Tests;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class TestDocumentAgent
{

	public TestDocumentAgent()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	
	public static void main(String[] args)
	{
		System.out.println("Début Main");
		System.out.println("Création du runtime");
		Runtime rt = Runtime.instance();
		Profile p = null;
		try
		{
			
			System.out.println("Création d'un profile");
			p = new ProfileImpl("./properties");
			System.out.println(p.getParameter("local-host", null));
			System.out.println("Création du conteneur principal");
			AgentContainer mc = rt.createMainContainer(p);
			
			System.out.println("Création des agents");
			
			AgentController am1 = mc.createNewAgent("DocumentAgent",
					"DocumentAgent.DocumentAgent", new Object[]{50});
			am1.start();
			
			try{
				  //do what you want to do before sleeping
				  Thread.currentThread().sleep(1000);//sleep for 1000 ms
				  //do what you want to do after sleeptig
				}
				catch(InterruptedException ie){
				//If this thread was intrrupted by nother thread
				}
			
			AgentController cl = mc.createNewAgent("ClientAgent",
					"ClientAgent.ClientAgent", new Object[]{50});
			cl.start();
			
		}
		catch(Exception ex)
		{
			System.out.println("Erreur : " + ex.getMessage());
		};
		System.out.println("Fin Main");

	}

}
