package utc.bsfile.model.agent;

import java.beans.PropertyChangeSupport;

import utc.bsfile.main.CofitsDesignScene;
import utc.bsfile.model.agent.action.SelectPickAction;
import utc.bsfile.model.agent.behaviours.ReceiveMessageBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class CofitsGuiAgent extends GuiAgent {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();
		m_scene = (CofitsDesignScene) getArguments()[0];
		m_pcs.addPropertyChangeListener(m_scene);
		
		addBehaviour(new LaunchPickBehaviour());
		addBehaviour(new ReceiveMessageBehaviour(this));
		
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {

	}

	private class LaunchPickBehaviour extends OneShotBehaviour {

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			m_scene.getMTApplication().registerPreDrawAction(
					new SelectPickAction(m_pcs));
		}

	}
	
	//Getters & Setters
	public final CofitsDesignScene getScene(){
		return m_scene;
	}
	
	public void setScene(CofitsDesignScene scene){
		m_scene = scene;
	}
	
	
	//Members
	private CofitsDesignScene m_scene;
	private PropertyChangeSupport m_pcs = new PropertyChangeSupport(this);
}
