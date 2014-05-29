package utc.bsfile.main;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

import utc.bsfile.gui.widget.controlorb.ControlOrb;
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

	
	/**
	 * Initialize the manager singleton classes
	 */
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
	
	
	protected void addOrb(ControlOrb orb){
		m_orbs.add(orb);
	}
	
	
	/**
	 * @return The orb controlled by the admin
	 * By convention, the admin controlOrb is the first in the list
	 */
	protected ControlOrb getAdminOrb(){
		if (!m_orbs.isEmpty()){
			return m_orbs.get(0);
		} else {
			return null;
		}
	}
	
	
	/**
	 * Method to be called when the Scene instance will never be used again 
	 */
	protected void close(){
		destroy();
	}
	
	
	//Members
	protected List<ControlOrb> m_orbs = new ArrayList<ControlOrb>();
	
}
