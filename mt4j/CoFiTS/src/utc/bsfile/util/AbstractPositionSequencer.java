package utc.bsfile.util;

import org.mt4j.components.MTComponent;
import org.mt4j.util.math.Vector3D;


/**
 * @author Aymeric PELLE
 *
 */
public abstract class AbstractPositionSequencer
{
	/**
	 * @uml.property  name="position"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Vector3D position;
	
	public AbstractPositionSequencer (Vector3D position)
	{
		this.position = position;
	}
	
	/**
	 * @return
	 * @uml.property  name="position"
	 */
	public final Vector3D getPosition ()
	{
		return position;
	}
	
	public abstract Vector3D newPosition (MTComponent component);

	public abstract void nextPosition (MTComponent component);

	public Vector3D newPosition ()
	{
		return newPosition(null);
	}
}
