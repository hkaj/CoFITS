/**
 * 
 */
package utc.bsfile.model.menu;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Aymeric PELLE
 *
 */
@SuppressWarnings("serial")
public class DefaultMenuModel extends DefaultMutableTreeNode implements IMenuModel
{
	/**
	 * @uml.property  name="current"
	 */
	private Object current;
	
	public DefaultMenuModel()
	{
		super();
		current = getStartMenu();
	}

	public DefaultMenuModel(Object menu)
	{
		super(menu);
		current = getStartMenu();
	}

	public DefaultMenuModel(Object menu, Object... choices)
	{
		super(menu);
		current = getStartMenu();
		add(choices);
	}

	@Override
	public Object getStartMenu ()
	{
		return this;
	}

	@Override
    public Object getCurrentMenu()
    {
	    return current;
    }

	@Override
    public void setCurrentMenu(Object current)
    {
		this.current = current;
    }

	public boolean hasChoices (Object choice)
	{
		if (choice != null && choice instanceof DefaultMutableTreeNode)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) choice;
			return isNodeDescendant(node) && node.getChildCount() > 0;
		}
		
		return false;
	}

	public Object[] getChoices (Object choice)
	{
		Object[] choices = null;

		if (hasChoices(choice))
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) choice;
			choices = new Object[node.getChildCount()];

			int index = 0;
			Enumeration<?> enumeration = node.children();
			while (enumeration.hasMoreElements())
			{
				choices[index] = enumeration.nextElement();
				++index;
			}
		}

		return choices;
	}

	@Override
	public Object getParentMenu(Object current)
	{
		Object parent = null;

		if (current instanceof DefaultMutableTreeNode && current != this.getStartMenu())
			parent = ((DefaultMutableTreeNode) current).getParent();

		return parent;
	}

	@Override
	public boolean onlyStartMenuCanCancel ()
	{
		return true;
	}
	
	public void add (Object... choices)
	{
		for (Object choice : choices)
		{
			if (choice instanceof DefaultMenuModel)
				add((DefaultMenuModel)choice);
			else
				add(new DefaultMenuModel(choice));
		}
	}
}
