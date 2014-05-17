/**
 * 
 */
package utc.bsfile.gui.widget.keyboard;

import java.beans.PropertyChangeSupport;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.bsfile.text.ListMenuKeyListener;


/**
 * @author Aymeric PELLE
 * 
 */
public class AttachmentKeyboard extends DefaultKeyboard
{
	public String NAME = "Attachment mt-keyboard";
	/** 
	 * @uml.property  name="attachedComponent"
	 * @uml.associationEnd  
	 */
	private MTComponent attachedComponent;
	
	public AttachmentKeyboard(PApplet pApplet) {
		this(pApplet, 0, 0, 0, 700, 245, 30, 30);
	}

	public AttachmentKeyboard(PApplet pApplet,float x, float y, float z, float width, float height, float arcWidth, float arcHeight) {
		super(pApplet, x, y, z, width, height, arcWidth, arcHeight);
		this.setName(NAME);
		setVisible(false);
	}
	
	public void close()
	{
		//this.closeKeyboard();
		setVisible(false);

	}


	public void attach(MTRectangle rectComponent)
	{
		addChild(rectComponent);
		PositionAnchor anchor = rectComponent.getAnchor();
		rectComponent.setAnchor(PositionAnchor.CENTER);
		rectComponent.setPositionRelativeToParent(new Vector3D(getWidthXY(TransformSpace.LOCAL) / 2, -rectComponent.getHeightXY(TransformSpace.LOCAL) / 2));
		rectComponent.setAnchor(anchor);
		this.attachedComponent = rectComponent;
	}

	public void attach(MTRoundRectangle roundRectComponent)
	{
		addChild(roundRectComponent);
		roundRectComponent.setPositionRelativeToParent(new Vector3D(getWidthXY(TransformSpace.LOCAL) / 2, -roundRectComponent.getHeightXY(TransformSpace.LOCAL) / 2));
		this.attachedComponent = roundRectComponent;
	}
	public void attachAutoComplete(ListMenuKeyListener auto)
	{
		addTextInputListener(auto);
		addChild(auto);
		auto.setAnchor(PositionAnchor.LOWER_RIGHT);
		auto.setPositionRelativeToParent(new Vector3D(getWidthXY(TransformSpace.LOCAL), 0));
	}

	/**
	 * @return
	 * @uml.property  name="attachedComponent"
	 */
	public MTComponent getAttachedComponent()
	{
		return attachedComponent;
	}
	
}
