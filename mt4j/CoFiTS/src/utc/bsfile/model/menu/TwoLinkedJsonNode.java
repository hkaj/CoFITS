package utc.bsfile.model.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TwoLinkedJsonNode implements TreeNode {
	
	/**
	 * Construct the tree from JsonNode encapsulated using its children to construct the TwoLinked children nodes
	 */
	public void constructTree(){
		releaseTree();
		
		if (m_current.isObject()) {
			Iterator<String> it = m_current.fieldNames();
			while (it.hasNext()){
				String field = it.next();
				if (field != null){
					TwoLinkedJsonNode child = new TwoLinkedJsonNode(m_current.get(field), this, field, true);
					addChild(child, false);
				}
			}
		}
	}
	
	
	//Constructors
	public TwoLinkedJsonNode(JsonNode jsonNode, String name){
		this(jsonNode, name, false);
	}
	
	public TwoLinkedJsonNode(JsonNode jsonNode, String name, boolean doConstructTree){
		this(jsonNode, null, name, doConstructTree);
	}
	
	public TwoLinkedJsonNode(JsonNode jsonNode, TwoLinkedJsonNode parent, String name, boolean doConstructTree) {
		m_parent = parent;
		m_current = jsonNode;
		m_name = name;
		
		if (doConstructTree)
			constructTree();
	}
	
	
	//Methods	
	public void addChild(TwoLinkedJsonNode node, boolean doEraseIfJsonNodeChildExists){
		m_children.add(node);
		if (m_current.get(node.getName()) == null || doEraseIfJsonNodeChildExists) {
			ObjectNode objectNode = (ObjectNode) m_current;
			objectNode.put(node.getName(), node.getCurrent());
		}
		node.setParent(this);
	}


	public void addChildren(Collection<TwoLinkedJsonNode> nodes, boolean doEraseIfJsonNodeChildExists){
		for (TwoLinkedJsonNode node : nodes){
			addChild(node, doEraseIfJsonNodeChildExists);
		}
	}

	//Implements TreeNode interface
	@Override
	public TreeNode getChildAt(int childIndex) {
		if (childIndex < m_children.size() && childIndex >= 0)
			return m_children.get(childIndex);
		return null;
	}

	@Override
	public int getChildCount() {
		return m_children.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return m_children.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLeaf() {
		return m_children.isEmpty();
	}
	
	public boolean isRoot(){
		return m_parent == null;
	}

	@Override
	public Enumeration<TwoLinkedJsonNode> children() {
		return Collections.enumeration(m_children);
	}
	
	
	public List<TwoLinkedJsonNode> getChildren(){
		return m_children;
	}
	
	
	public JsonNode getCurrent() {
		return m_current;
	}

	@Override
	public String toString() {
		return m_name;
	}	
	
	/**
	 * @return A Json string representing the JsonNode Tree
	 * WARNING : You should not have used a addChild method with doErase to false unless you managed JsonNode child before
	 * Otherwise, the json file might not be what you're expecting 
	 */
	public String treeToString(){
		return m_current.toString();
	}
	
	
	/**
	 * While TwoLinkedJsonNode is double linked, we need to free the references
	 * in order to allow the Garbage Collector to destroy the JsonNodes
	 */
	public void releaseTree() {
		
		for (TwoLinkedJsonNode child : m_children){
			child.releaseTree();
		}
		
		m_children.clear();		
	}
	
	
	//Getters & Setters
	public String getName(){
		return m_name;
	}
	
	@Override
	public TreeNode getParent() {
		return m_parent;
	}
	
	public void setParent(TwoLinkedJsonNode twoLinkedJsonNode) {
		m_parent = twoLinkedJsonNode;		
	}
	
	public JsonNode getParentJsonNode() {
		return m_parent.getCurrent();
	}
	
	public void setName(String filename) {
		m_name = filename;		
	}

	//Members
	private String m_name;
	private JsonNode m_current;
	private TwoLinkedJsonNode m_parent;
	private List<TwoLinkedJsonNode> m_children = new ArrayList<TwoLinkedJsonNode>();

}
