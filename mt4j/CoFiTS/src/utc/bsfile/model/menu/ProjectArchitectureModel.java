package utc.bsfile.model.menu;

import java.io.File;
import java.io.FileFilter;
import java.util.Enumeration;

import utc.bsfile.util.FileExtensionFilter;
import utc.bsfile.util.JsonManager;

public class ProjectArchitectureModel implements IMenuModel {
	
	//Statics members
	//Those constants are defined to help user to choose a maximum level in tree visiting
	public static final int PROJECT_LEVEL = 0;
	public static final int SESSION_LEVEL = 1;
	public static final int FILE_LEVEL = 2;
	
	public static final String[] IMG_FILTER = new String[]{"bmp", "png", "jpg", "jpeg"};
	public static final String[] VIDEO_FILTER = new String[]{"mov", "mpeg", "mpg", "m4v", "wmv", "mp4","avi"};
	public static final String PDF_FILTER = "pdf";
	public static final String[] HTML_FILTER = new String[]{"html", "htm"};
	
	public enum ModelFilter {
		PDF,
		IMG,
		VID,
		WEB,
		NONE
	}

	//Constructors
	public ProjectArchitectureModel(File jsonFile){
		this(jsonFile, FILE_LEVEL);
	}
	
	public ProjectArchitectureModel(String json){
		this(json, FILE_LEVEL);
	}
	
	public ProjectArchitectureModel(File jsonFile, int maxLevel) {
		m_currentLevel = 0;
		m_maxLevel = maxLevel;
		
		//Parse the Json file and transform it to a TwoLinkedJsonNode tree		
		m_start = new TwoLinkedJsonNode(JsonManager.getInstance().createJsonNode(jsonFile), "", true);
		m_current = m_start;
	}
	
	public ProjectArchitectureModel(File jsonFile, TwoLinkedJsonNode node, int maxLevel) {
		m_currentLevel = 0;
		m_maxLevel = maxLevel;
		
		//Parse the Json file and transform it to a TwoLinkedJsonNode tree		
		m_start = new TwoLinkedJsonNode(JsonManager.getInstance().createJsonNode(jsonFile), "", true);
		m_current = node;
	}
	
	public ProjectArchitectureModel(File jsonFile, TwoLinkedJsonNode current, int maxLevel, ModelFilter filter) {
		m_currentLevel = 0;
		m_maxLevel = maxLevel;
		m_filter = filter;
		
		//Parse the Json file and transform it to a TwoLinkedJsonNode tree		
		m_start = new TwoLinkedJsonNode(JsonManager.getInstance().createJsonNode(jsonFile), "", true);
		m_current = current;
	}
	
	public ProjectArchitectureModel(String json, int maxLevel){
		m_currentLevel = 0;
		m_maxLevel = maxLevel;
		
		//Parse the Json content and transform it to a TwoLinkedJsonNode tree
		m_start = new TwoLinkedJsonNode(JsonManager.getInstance().createJsonNode(json), "", true);
		m_current = m_start;
	}
	
	//Implements IMenuModel interface
	@Override
	public Object getStartMenu() {
		return m_start;
	}

	@Override
	public Object getCurrentMenu() {
		return m_current;
	}

	@Override
	public void setCurrentMenu(Object current) {
		if (current instanceof TwoLinkedJsonNode){
			
			//Need to check level in order to update it
			if (current == m_current.getParent()){
				--m_currentLevel;
			} else if (m_current.getChildren().contains(current)) {
				++m_currentLevel;
			} else {
				//TODO Process error
				System.out.println("Menu not parent or child");
				return;
			}
			
			m_current = (TwoLinkedJsonNode) current;
		}
	}

	@Override
	public boolean hasChoices(Object choice) {
		
		if (choice != null && choice instanceof TwoLinkedJsonNode)
		{
			TwoLinkedJsonNode node = (TwoLinkedJsonNode) choice;
			
			//if the max level is reached, we stop the visit
			if (m_currentLevel == m_maxLevel)
				return false;
			
			return node.getChildCount() > 0;
		}
		
		return false;
	}

	@Override
	public Object[] getChoices(Object choice) {
		Object[] choices = null;

		if (choice instanceof TwoLinkedJsonNode){
			if (((TwoLinkedJsonNode)choice).getChildCount() > 0)
			{
				TwoLinkedJsonNode node = (TwoLinkedJsonNode) choice;
				choices = new Object[node.getChildCount()];
	
				int index = 0;
				Enumeration<TwoLinkedJsonNode> enumeration = node.children();
				while (enumeration.hasMoreElements())
				{
					choices[index] = enumeration.nextElement();
					++index;
				}
			}
		}

		return choices;
	}

	@Override
	public Object getParentMenu(Object object) {
		return m_current.getParent();
	}

	@Override
	public boolean onlyStartMenuCanCancel() {
		return true;
	}
	
	public int getCurrentLevel() {
		return m_currentLevel;
	}
	
	
	//Members
	protected TwoLinkedJsonNode m_current;
	protected TwoLinkedJsonNode m_start;
	protected ModelFilter m_filter;
	protected int m_currentLevel;
	protected int m_maxLevel;
	

}
