/**
 * 
 */
package utc.bsfile.model.menu;

/**
 * @author  Aymeric PELLE
 */
public interface IMenuModel
{
	/**
	 * @uml.property  name="startMenu"
	 */
	public Object getStartMenu();

	/**
	 * @uml.property  name="currentMenu"
	 */
	public Object getCurrentMenu();
	
	/**
	 * @param current
	 * @uml.property  name="currentMenu"
	 */
	public void setCurrentMenu(Object current);
	
	public boolean hasChoices (Object choice);

	public Object[] getChoices (Object choice);

	public Object getParentMenu(Object object);

	public boolean onlyStartMenuCanCancel ();
}
