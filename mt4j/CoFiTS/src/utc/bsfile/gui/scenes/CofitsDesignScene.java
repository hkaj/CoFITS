package utc.bsfile.gui.scenes;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.model.CofitsModel;
import utc.bsfile.util.FileExtensionIconManager;
import utc.bsfile.util.ImageManager;

/**
 * Abstract class for defining the design of every Scene in CoFiTS application
 *
 */
public abstract class CofitsDesignScene extends AbstractScene implements PropertyChangeListener{

	public CofitsDesignScene(AbstractMTApplication mtApplication, String name, CofitsModel model){
		this(mtApplication, name, model, new ArrayList<ControlOrb>(), false);
	}
	
	public CofitsDesignScene(AbstractMTApplication mtApplication, String name,CofitsModel model, List<ControlOrb> orbs, boolean doClearOrbGestures) {
		super(mtApplication, name);
		
		m_model = model;
		model.addPropertyChangeListener(this);
		
		//Add the orbs to the list of Orbs
		for (ControlOrb orb : orbs){
			if (doClearOrbGestures){
				clearAllGestures(orb);
				orb.setDefaultGestureActions();
			}
			
			orb.setEnabled(true);
			orb.setVisible(true);
			
			addOrb(orb);
			getCanvas().addChild(orb);
		}
		
		
		//Input Listener drawing a circle when touching with one finger
		this.registerGlobalInputProcessor(new CursorTracer(getMTApplication(), this));
		//this.setClearColor(new MTColor(126, 130, 168, 255));
		this.setClearColor(new MTColor(50, 50, 50, 255));
		
		//TODO construct the node somewhere
		
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
		for (ControlOrb orb : m_orbs){
			if (orb.isAdmin()){
				return orb;
			}
		}
		return null;
	}
	
	
	/**
	 * Method to be called when the Scene instance will never be used again 
	 */
	protected void close(){
		destroy();
	}	
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("projectsArchitectureRootNode changed")){
			//TODO set the new model for pickFileChooserOpened
		} else if (evt.getPropertyName().equals("File Received")){
			processFileDownloaded((int)evt.getNewValue());
		}
	}
	
	protected void processFileDownloaded(int id){
		System.out.println(id);
		m_model.getFile(id).setLocal(true);
	}

	
	//Getters & Setters
	public final CofitsModel getModel(){return m_model;}
	public ControlOrb getOrb(String login){
		for (ControlOrb orb : m_orbs){
			if (orb.getLogin().equals(login))
				return orb;
		}
		return null;
	}
	
	//Members
	protected List<ControlOrb> m_orbs = new ArrayList<ControlOrb>();
	protected CofitsModel m_model;
	
}
