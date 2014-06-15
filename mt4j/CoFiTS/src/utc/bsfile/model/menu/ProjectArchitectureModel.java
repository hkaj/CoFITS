package utc.bsfile.model.menu;

import java.io.File;
import java.util.Enumeration;

import utc.bsfile.util.JsonManager;

public class ProjectArchitectureModel implements IMenuModel {
	
	//Statics members
	//Those constants are defined to help user to choose a maximum level in tree visiting
	public static final int PROJECT_LEVEL = 0;
	public static final int SESSION_LEVEL = 1;
	public static final int FILE_LEVEL = 2;
	
	public static final String[] IMG_FILTER = new String[]{"bmp", "png", "jpg", "jpeg"};
	public static final String[] VIDEO_FILTER = new String[]{"mov", "mpeg", "mpg", "m4v", "wmv", "mp4","avi"};
	public static final String[] PDF_FILTER = new String[]{"pdf"};
	public static final String[] HTML_FILTER = new String[]{"html", "htm"};

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
	
	public ProjectArchitectureModel(TwoLinkedJsonNode start, TwoLinkedJsonNode current, int maxLevel, int currentLevel) {
		m_currentLevel = 0;
		m_filter = null;
		m_maxLevel = maxLevel;	
		m_start = start;
		m_current = current;
		System.out.println("OPENING : " + m_current.getName());
		System.out.println("ROOT : " + m_start.getName());
	}
	
	public ProjectArchitectureModel(File jsonFile, TwoLinkedJsonNode current, int currentLevel, int maxLevel, String[] filter) {
		m_currentLevel = currentLevel;
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
			
			return node.getNotLeafChildCount() > 0;
		}
		
		return false;
	}

	@Override
	public Object[] getChoices(Object choice) {
		Object[] choices = null;

		if (choice instanceof TwoLinkedJsonNode){
			if (((TwoLinkedJsonNode)choice).getNotLeafChildCount() > 0)
			{
				TwoLinkedJsonNode node = (TwoLinkedJsonNode) choice;
				choices = new Object[node.getNotLeafChildCount()];
	
				int index = 0;
				Enumeration<TwoLinkedJsonNode> enumeration = node.children();
				while (enumeration.hasMoreElements())
				{
					TwoLinkedJsonNode child = enumeration.nextElement(); 
					if (!child.isLeaf()){
						choices[index] = child;
						++index;
					}
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
	
	public String[] getFilter() {
		return m_filter;
	}
	
	public int getMaxLevel(){return m_maxLevel;}
	
	//Members
	protected TwoLinkedJsonNode m_current;
	protected TwoLinkedJsonNode m_start;
	protected String[] m_filter;
	protected int m_currentLevel;
	protected int m_maxLevel;
	

}
