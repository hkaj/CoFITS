package utc.bsfile.main;

import org.mt4j.MTApplication;

import utc.bsfile.gui.scenes.LoginScene;
import utc.bsfile.model.CofitsModel;

public class StartCofitsEntities extends MTApplication {
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		initialize();
	}
	
	
	@Override
	public void startUp() {
		//Launching the model
		CofitsModel model = new CofitsModel();
		
		//Launching the Scene
		LoginScene logScene = new LoginScene(this, "Logging scene");
		model.addPropertyChangeListener(logScene);
		
		//Add the Scene to the Application
		addScene(logScene);		
	}
}
