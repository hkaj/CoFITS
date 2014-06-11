package utc.bsfile.gui.scenes;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.model.CofitsFile;
import utc.bsfile.model.CofitsModel;
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
		} else if (evt.getPropertyName().equals("File Received")){
			processFileDownloaded((String)evt.getNewValue());
		}
	}
	
	protected void processFileDownloaded(String filename){
		m_files.get(filename).setLocal(true);
	}

	
	/**
	 * Generate a map to easily have the link between id and filename for a file
	 */
	private void generateFilesMap() {
		//TODO Find another place to do that
		m_files.clear();
		
		for (TwoLinkedJsonNode projectNodes : m_projectsArchitectureRootNode.getChildren()){
			for (TwoLinkedJsonNode sessionNodes : projectNodes.getChildren()){
				for (TwoLinkedJsonNode fileNode : sessionNodes.getChildren()){	
					m_files.put(fileNode.getName(), new CofitsFile(fileNode));
				}
			}
		}
	}
	
	//Getters & Setters
	public TwoLinkedJsonNode getProjectsArchitectureRootNode() {return m_projectsArchitectureRootNode;}
	protected void setProjectsArchitectureRootNode(TwoLinkedJsonNode node) {m_projectsArchitectureRootNode = node; generateFilesMap();}

	public final CofitsModel getModel(){return m_model;}
	public final Map<String,CofitsFile> getFiles(){return m_files;}
	public final CofitsFile getFile(String filename){return m_files.get(filename);}
	
	//Members
	protected List<ControlOrb> m_orbs = new ArrayList<ControlOrb>();
	protected TwoLinkedJsonNode m_projectsArchitectureRootNode;
	protected Map<String, CofitsFile> m_files = new HashMap<String,CofitsFile>();
	protected CofitsModel m_model;
	
}
