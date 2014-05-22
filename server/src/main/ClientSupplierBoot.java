package main;

import java.util.ArrayList;
import java.util.Scanner;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class ClientSupplierBoot {
	public static String SECONDARY_PROPERTIES_FILE = "properties/second.properties";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		secondary_boot();
	}
	public static void secondary_boot() {
		// open secondary container
		// properties: main=false; gui = false;
		ArrayList<ClientAgent.ClientAgent> l = new ArrayList<ClientAgent.ClientAgent>();
		Runtime rt = Runtime.instance();
		ProfileImpl p = null;
		try {
			p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
			ContainerController  cc = rt.createAgentContainer(p);
			AgentController cl = cc.createNewAgent("ClientAgent",
					"ClientAgent.ClientAgent",new Object[]{l});
			cl.start();
			Scanner sc = new Scanner(System.in);
			System.out.println("Start?");
			sc.nextLine();
			l.get(0).start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
