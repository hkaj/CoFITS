package utc.tatinpic.gui.widgets.menu.circularmenu;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.mt4j.util.MTColor;
import org.mt4jx.components.visibleComponents.shapes.MTEllipseSegmentComplexPoly;

public class TatinCircularMenuSegment extends MTEllipseSegmentComplexPoly{
	private boolean isDown = false;
	private float scaleDelta = 0.04f;
	
	private TatinArcCircularMenu parentMenu;
	private float innerRadius;
	private float outerRadius;
	private float segmentAngle;
	private float orientationAngleDegrees;
	private static long idCounter =0;
	private long segmentId;
	
	private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
	
	public TatinCircularMenuSegment(TatinArcCircularMenu parentMenu, float innerRadius, float outerRadius, float segmentAngle, float orientationAngleDegrees){
		super(parentMenu.getPApplet(), innerRadius, outerRadius, segmentAngle, orientationAngleDegrees);
		this.parentMenu = parentMenu;
		this.segmentId = createSegmentId();
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.segmentAngle = segmentAngle;
		this.orientationAngleDegrees = orientationAngleDegrees;
		this.setStrokeColor(new MTColor(255,255,255));
		this.setComposite(true);
		this.removeAllGestureEventListeners();
	}
	public boolean hasChildren(){
		return false; //TODO: implement when children are supported
	}
	public synchronized void segmentUp(){
		if(isDown){
			this.scale(1f*(1f+scaleDelta), 1f*(1f+scaleDelta), 1f, this.getCenterPointLocal());
			this.isDown = false;
		}
	}
	public synchronized void segmentDown(){
		if(!isDown){
			this.scale(1f/(1f+scaleDelta), 1f/(1f+scaleDelta), 1f, this.getCenterPointLocal());
			this.isDown = true;
		}
	}
	public void setRandomFillColor(float alpha){
		MTColor randomColor = new MTColor((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random()), alpha);
		this.setFillColor(randomColor);
	}
	private synchronized long createSegmentId(){
		return idCounter++;
	}
	public TatinArcCircularMenu getParentMenu() {
		return parentMenu;
	}
	public float getOuterRadius() {
		return outerRadius;
	}
	public float getSegmentAngle() {
		return segmentAngle;
	}
	public long getSegmentId() {
		return segmentId;
	}
	public void addActionListener(ActionListener al){
		this.actionListeners.add(al);
	}
	public void removeActionListeners(){
		this.actionListeners.clear();
	}
}
