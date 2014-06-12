package utc.bsfile.main;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

import utc.bsfile.gui.Theme;
import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.util.FileExtensionIconManager;
import utc.bsfile.util.ImageManager;

/**
 * Abstract class for defining the design of every Scene in CoFiTS application
 *
 */
public abstract class CofitsDesignScene extends AbstractScene implements PropertyChangeListener{

	public CofitsDesignScene(AbstractMTApplication mtApplication, String name){
		this(mtApplication, name, new ArrayList<ControlOrb>(), false);
	}
	
	public CofitsDesignScene(AbstractMTApplication mtApplication, String name, List<ControlOrb> orbs, boolean doClearOrbGestures) {
		super(mtApplication, name);
		
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
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("projectsArchitectureRootNode changed")){
			setProjectsArchitectureRootNode((TwoLinkedJsonNode)evt.getNewValue());
		}
	}
	
	//Getters & Setters
	public TwoLinkedJsonNode getProjectsArchitectureRootNode() {return m_projectsArchitectureRootNode;}
	protected void setProjectsArchitectureRootNode(TwoLinkedJsonNode node) {m_projectsArchitectureRootNode = node;}
	
	
	//Members
	protected List<ControlOrb> m_orbs = new ArrayList<ControlOrb>();
	protected TwoLinkedJsonNode m_projectsArchitectureRootNode;
	
	
}
