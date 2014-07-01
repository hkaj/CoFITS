package agent;

import javax.swing.SwingUtilities;

import ui.MyFrame;
import main.Acknowledgement;
import jade.core.Agent;

public class MainAgent extends Agent{
	
	protected void setup() {
		
		Acknowledgement b = new Acknowledgement();
		addBehaviour(b);
		
		MyFrame fenetre = new MyFrame(this);
		fenetre.setVisible(true);
		
		
	} 

}
