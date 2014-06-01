package utc.bsfile.model.agent;

import java.beans.PropertyChangeSupport;

import utc.bsfile.main.MTBSFileScene;
import utc.bsfile.model.Constants;
import utc.bsfile.model.agent.action.SelectPickAction;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class BSFileGuiAgent extends GuiAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MTBSFileScene scene;
	PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	@Override
	protected void setup() {
		super.setup();
		scene = (MTBSFileScene) getArguments()[0];
		pcs.addPropertyChangeListener(scene);
		addBehaviour(new LaunchPickBehaviour());
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
			scene.getMTApplication().registerPreDrawAction(
					new SelectPickAction(pcs));
		}

	}
}