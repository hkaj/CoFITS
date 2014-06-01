package utc.bsfile.model.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.fasterxml.jackson.databind.JsonNode;

public class TwoLinkedJsonNode implements TreeNode {

	//Static methods
	
	
	/**
	 * @param jsonNode - The root jsonNode encapsulated
	 * @param name - The name of the root node
	 * @return The root of a new double linked tree with JsonNode encapsulated
	 * Creates a new double linked tree from a JsonNode.
	 */
	public static TwoLinkedJsonNode getTwoLinkedTreeFromJsonNode(JsonNode jsonNode, String name){
		return getTwoLinkedTreeFromJsonNode(jsonNode, null, name);
	}
	
	
	/**
	 * @param jsonNode - A jsonNode
	 * @param parent - The jsonNode parent in the tree
	 * @param name - The name of the node
	 * @return A node of a double linked tree with JsonNode encapsulated
	 * Creates a new node for a double linked tree encapsulating JsonNode. Attach children of jsonNode recursively
	 */
	protected static TwoLinkedJsonNode getTwoLinkedTreeFromJsonNode(JsonNode jsonNode, TwoLinkedJsonNode parent, String name){
		TwoLinkedJsonNode node = new TwoLinkedJsonNode(jsonNode, parent, name);
		
		if (jsonNode.isObject()) {
			Iterator<String> it = jsonNode.fieldNames();
			while (it.hasNext()){
				String field = it.next();
				if (field != null){
					TwoLinkedJsonNode child = getTwoLinkedTreeFromJsonNode(jsonNode.get(field), node,  field);
					node.addChild(child);
				}
			}
		}
		
		return node;
	}
	
	//Constructors
	public TwoLinkedJsonNode(JsonNode jsonNode, String name){
		this(jsonNode, null, name);
	}
	
	public TwoLinkedJsonNode(JsonNode jsonNode, TwoLinkedJsonNode parent, String name) {
		
		m_parent = parent;
		m_current = jsonNode;
		m_name = name;
		
	}
	
	
	//Methods
	public void addChild(TwoLinkedJsonNode node){
		m_children.add(node);
	}
	
	public void addChildren(Collection<TwoLinkedJsonNode> nodes){
		m_children.addAll(nodes);
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
	public TreeNode getParent() {
		return m_parent;
	}
	
	public JsonNode getParentJsonNode() {
		return m_parent.getCurrent();
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

	//Members
	private String m_name;
	private JsonNode m_current;
	private TwoLinkedJsonNode m_parent;
	private List<TwoLinkedJsonNode> m_children = new ArrayList<TwoLinkedJsonNode>();

}
