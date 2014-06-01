package utc.bsfile.main;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

import utc.bsfile.util.FileExtensionIconManager;
import utc.bsfile.util.ImageManager;

/**
 * Abstract class for defining the design of every Scene in CoFiTS application
 *
 */
public abstract class CofitsDesignScene extends AbstractScene {

	public CofitsDesignScene(AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		
		//Input Listener drawing a circle when touching with one finger
		this.registerGlobalInputProcessor(new CursorTracer(getMTApplication(), this));
		this.setClearColor(new MTColor(126, 130, 168, 255));
	
		initManager();
	}

	
	private void initManager() {
		ImageManager.getInstance().init(getMTApplication());
		FileExtensionIconManager.getInstance().init(getMTApplication());
	}
	
	
	/**
	 * @param comp - the Component to be cleared
	 * Clears all the gestures linked to a Component
	 */
	protected void clearAllGestures(MTComponent comp) {
		comp.unregisterAllInputProcessors();
		comp.removeAllGestureEventListeners();
	}
	
}
