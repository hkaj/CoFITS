/**
 * 
 */
package utc.bsfile.util;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

public class PositionSequencer extends AbstractPositionSequencer
{
	/**
	 * @author  claude
	 */
	public enum Orientation { /**
	 * @uml.property  name="hORIZONTAL"
	 * @uml.associationEnd  
	 */
	HORIZONTAL, /**
	 * @uml.property  name="vERTICAL"
	 * @uml.associationEnd  
	 */
	VERTICAL };
	
	/**
	 * @uml.property  name="spacing"
	 */
	protected float spacing;
	/**
	 * @uml.property  name="orientation"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Orientation orientation;
	
	public PositionSequencer (Vector3D position, float spacing, Orientation orientation)
	{
		super(position);
		this.spacing = Math.abs(spacing);
		this.orientation = orientation;
	}
	
	public PositionSequencer (Vector3D position, Orientation orientation)
	{
		this(position, 5.f, orientation);
	}
	
	public Vector3D newPosition ()
	{
		Vector3D copy = (Vector3D) this.position.getCopy();
		
		switch (this.orientation)
        {
        case HORIZONTAL:
        	this.position.x += spacing;
        	break;
        	
        default:
        	this.position.y += spacing;
        }
		
    	return copy;
	}
	
	public void nextPosition (MTComponent component)
	{
		switch (this.orientation)
        {
        case HORIZONTAL:
        	this.position.x += component.getBounds().getWidthXY(TransformSpace.GLOBAL) + spacing;
        	break;
        	
        default:
        	this.position.y += component.getBounds().getHeightXY(TransformSpace.GLOBAL) + spacing;
        }
	}
	
	public Vector3D newPosition(MTComponent component)
	{
		Vector3D copy = (Vector3D) this.position.getCopy();
		
		switch (this.orientation)
        {
        case HORIZONTAL:
        	this.position.x += component.getBounds().getWidthXY(TransformSpace.GLOBAL) + spacing;
        	break;
        	
        default:
        	this.position.y += component.getBounds().getHeightXY(TransformSpace.GLOBAL) + spacing;
        }
		
    	return copy;
	}
}
