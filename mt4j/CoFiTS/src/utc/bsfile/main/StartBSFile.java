package utc.bsfile.main;

import org.mt4j.MTApplication;

import utc.bsfile.model.agent.BSFileGuiAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class StartBSFile extends MTApplication {
	private static final long serialVersionUID = 1L;
	public static String SECOND_PROPERTIES_FILE = "rsc/config/second.properties";
	
	public static void main(String[] args) {
		initialize();
	}
	
	
	@Override
	public void startUp() {
		//Launching the Scene
		MTBSFileScene scene = new MTBSFileScene(this, "Multi-touch File Brainstorming");
		
		//Launching the Agent
		//launchAgentContainer(scene);
		
		//Add the Scene to the Application
		addScene(scene);
	}
	
	
	/**
	 * @param scene - The main Scene of the application
	 * @brief Launch the Agent in charge of communication with server
	 */
	private void launchAgentContainer(MTBSFileScene scene) {
		Runtime rt = Runtime.instance();
	  	ProfileImpl p = null;
	  	ContainerController cc; 
	  	  try{
	  		  
	  		/**
	  		 * host : null value means use the default (i.e. localhost)
	         * port - is the port number. A negative value should be used for using the default port number.
	         * platformID - is the symbolic name of the platform, 
	         * isMain
	  		 */
	  		
	  		p = new ProfileImpl(SECOND_PROPERTIES_FILE);
	  		
		    cc = rt.createAgentContainer(p);
		    
		    AgentController ac = cc.createNewAgent("bsgui-agent", "utc.bsfile.model.agent.BSFileGuiAgent",new Object[]{scene});
			ac.start();
			
	  	  } catch(Exception ex) {
	  		  System.out.println("Main container not started");
	  	  }
	}
	
	
	//Members
	BSFileGuiAgent guiagent = null;
}
