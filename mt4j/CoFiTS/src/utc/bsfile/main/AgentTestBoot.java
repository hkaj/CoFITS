package utc.bsfile.main;

import java.util.ArrayList;

import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.model.agent.behaviours.RequestDownloadFile;
import utc.bsfile.model.agent.behaviours.RequestProjectsArchitecture;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class AgentTestBoot {
	public static String SECONDARY_PROPERTIES_FILE = "rsc/config/second.properties";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		secondary_boot();
	}
	
	private static void downloadFileTest(CofitsGuiAgent agent) {
		int id = 6;	//File id
		agent.addBehaviour(new RequestDownloadFile(agent, id));
	}

	private static void downloadProjectsArchitectureTest(CofitsGuiAgent agent) {
		agent.addBehaviour(new RequestProjectsArchitecture(agent));		
	}

	public static void secondary_boot() {
		// open secondary container
		// properties: main=false; gui = false;
		Runtime rt = Runtime.instance();
		ProfileImpl p = null;
		ArrayList<CofitsGuiAgent> l = new ArrayList<CofitsGuiAgent>();
		try {
			p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
			ContainerController  cc = rt.createAgentContainer(p);
			AgentController cl = cc.createNewAgent("ClientAgent",
					CofitsGuiAgent.class.getName(),new Object[]{l});
			cl.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		downloadProjectsArchitectureTest(l.get(0));
		downloadFileTest(l.get(0));

	}
	
	
}