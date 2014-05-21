package main;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class MainBoot {
	public static String MAIN_PROPERTIES_FILE = "properties/main.properties";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boot_gui();
	}

	public static void boot_gui() {
		// open main console gui
		// properties: main=true; gui = true;
		Runtime rt = Runtime.instance();
		ProfileImpl p = null;
		try {
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mc = rt.createMainContainer(p);
			AgentController am1 = mc.createNewAgent("DocumentAgent",
					"DocumentAgent.DocumentAgent", null);
			am1.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
