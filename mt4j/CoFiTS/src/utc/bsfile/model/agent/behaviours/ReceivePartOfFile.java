package utc.bsfile.model.agent.behaviours;

import java.io.File;

import utc.bsfile.model.CofitsFile;
import utc.bsfile.model.agent.CofitsGuiAgent;
import utc.bsfile.util.FilePathManager;
import utc.bsfile.util.PropertyManager;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceivePartOfFile extends OneShotBehaviour {

	public ReceivePartOfFile(Agent a, ACLMessage message, int id) {
		super(a);
		m_message = message;
		m_id = id;
		m_partNumber = Integer.parseInt(m_message.getEnvelope().getComments());
		m_agent = (CofitsGuiAgent) a;
	}
	
	@Override
	public void action() {
		CofitsFile file = m_agent.getModel().getFile(m_id);
		File filepath = new File(PropertyManager.getInstance().getDirProperty(PropertyManager.FILE_PATH) + file.getFilepath());
		
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
	private int m_id;
	private int m_partNumber;
	private CofitsGuiAgent m_agent;
}
