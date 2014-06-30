package main;
import javax.swing.SwingUtilities;

import ui.MyFrame;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;

public class main {

	public static void main(String[] args) {
		 System.out.println("Hello, World");
	        
		 try{
		        Runtime rt = Runtime.instance();
				
				Profile p = new ProfileImpl("properties");
		        jade.wrapper.AgentContainer mc = rt.createAgentContainer(p);
		        		        
		        AgentController ac;
		        ac = mc.createNewAgent("TheOne", "agent.MainAgent", null);
		        ac.start();		

//			 MyFrame test = new MyFrame(null);
//			 test.setVisible(true);
		        
		 } catch (Exception e) {
				e.printStackTrace();
			}
		        
		        
			

	}

}
