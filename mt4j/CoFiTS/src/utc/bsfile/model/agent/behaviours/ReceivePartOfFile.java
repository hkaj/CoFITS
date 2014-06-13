package utc.bsfile.model.agent.behaviours;

import java.io.File;

import utc.bsfile.util.FilePathManager;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceivePartOfFile extends OneShotBehaviour {

	public ReceivePartOfFile(Agent a, ACLMessage message, String filename) {
		super(a);
		m_message = message;
		m_filename = filename;
		m_partNumber = Integer.parseInt(m_message.getEnvelope().getComments());
	}
	
	@Override
	public void action() {
		File filepath = new File(m_filename);
		
		if (filepath.exists()){
			FilePathManager.getInstance().appendBinaryFile(filepath, m_message.getByteSequenceContent());
		} else {
			FilePathManager.getInstance().createBinaryFile(filepath, m_message.getByteSequenceContent());
		}

	}
	
	
	//Getters & Setters
	public int getPartNumber(){ return m_partNumber;}
	
	//Members
	private ACLMessage m_message;
	private String m_filename;
	private int m_partNumber;
}
