package utc.tatinpic.gui.widgets.menu.circularmenu;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Quaternion;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.circularmenu.actions.DefaultSegmentSelectionVisualisationAction;
import org.mt4jx.util.animation.AnimationUtil;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import utc.tatinpic.gui.phase.PhaseScene;
import utc.tatinpic.gui.widgets.menu.ListMenu;
import utc.tatinpic.model.ApplicationDescription;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.semantics.Constants;
import utc.tatinpic.util.manager.ImageManager;
import utc.tatinpic.util.manager.PropertyManager;

import java.lang.Math;

public class WorkbenchHandler extends TatinArcCircularMenu {
	private static int aperture = 360;
	private Workbench workbench;
	protected CentralButton centralButton;
	/**
	 * a WorkbenchHandler may have submenus open when one clicks on a segment.
	 * As preDraw is called and destroys all TatinArcCircularMenu children
	 * children must be added. They must be registered.
	 */
	private ArrayList<TatinArcCircularMenu> subMenus;
	/** Menu open for selecting a new phase */
	private ListMenu phaseMenu;

	/** Circular Menu, main moderator menu of the phase */
	private WorkbenchHandler backMenu;

	/** Menu for selecting the TableAgent */
	private ListMenu agentMenu;

	private boolean minimized = false;

	private boolean rotate = false;
	
	public WorkbenchHandler(PApplet pApplet, float innerRadius, float outerRadius, String title,boolean rotate) {
		super(pApplet, new Vector3D(0,0), innerRadius, outerRadius,aperture);
		this.centralButton = new CentralButton(this, this.getCenterPointLocal(), getInnerRadius(),title);

		this.setName("Workbench Handler");
		this.rotate = rotate;

		IGestureEventListener[] menuBehavior = new IGestureEventListener[] { new DefaultSegmentSelectionVisualisationAction() };
		this.getBehavior().setSegmentSelectionActions(menuBehavior);
		subMenus = new ArrayList<TatinArcCircularMenu>();
	}

	public void registerChild(TatinArcCircularMenu menu) {
		subMenus.add(menu);
	}

	public WorkbenchHandler(PApplet pApplet, float innerRadius,
			float outerRadius, String title, MTColor centerColor) {
		super(pApplet, new Vector3D(0, 0), innerRadius, outerRadius, aperture);
		this.centralButton = new CentralButton(this,
				this.getCenterPointLocal(), getInnerRadius(), title);
		this.setName("Workbench Handler");
		centralButton.setFillColor(centerColor);
		IGestureEventListener[] menuBehavior = new IGestureEventListener[] { new DefaultSegmentSelectionVisualisationAction() };
		this.getBehavior().setSegmentSelectionActions(menuBehavior);
	}

	public void setWorkbench(Workbench workbench) {
		this.workbench = workbench;
		centralButton.setFillColor(workbench.getColor());
	}

	public Workbench getWorkbench() {
		return workbench;
	}

	public CentralButton getCentralButton() {
		return centralButton;
	}

	public void toogleSegmentsVisible() {
		// minimized = !minimized;
		for (MTComponent enfant : this.getChildren()) {
			if (enfant instanceof TatinArcCircularMenu) {
				enfant.setVisible(false);
				// enfant.setPickable(minimized);
				// enfant.setEnabled(minimized);
			}
			/*
			 * if (enfant instanceof TatinCircularMenuSegment) {
			 * enfant.setEnabled(minimized); enfant.setPickable(minimized);
			 * enfant.setVisible(minimized); // on change la visibilite }
			 */
		}
	}

	public void preDraw(PGraphics graphics) {
		super.preDraw(graphics);
		for (TatinArcCircularMenu m : subMenus) {
			addChild(m);
		}
		this.addChild(centralButton);
		
		if( rotate )
		{
			Vector3D pos = new Vector3D();
			Vector3D rot = new Vector3D();
			this.getLocalMatrix().decompose(pos, rot, new Vector3D());
			
			float coef = (float) graphics.height / (float) graphics.width;
			float y = -(pos.getY() - graphics.height);
			boolean abovebt = (y > coef * pos.getX() );
			boolean abovetb = (y > ( -coef * pos.getX() ) + graphics.height);
			
			//System.out.println("menu at "+pos.getX() + " " + y);
			//System.out.println("eq "+coef * pos.getX());
			
			Quaternion currentq = this.getLocalMatrix().toRotationQuat();
			Quaternion newq = new Quaternion();
			
			//float distbt = (float) (Math.abs( coef * pos.getX() - y )  / Math.sqrt( 1 + Math.pow(coef, 2)));
			//float disttb = (float) (Math.abs( -coef * pos.getX() - y + graphics.height )  / Math.sqrt( 1 + Math.pow(-coef, 2)));
			
			//bottom no rotation
			if( !abovebt && !abovetb)
			{
				//System.out.println("at bottom");
				newq.fromAngleAxis(0, new Vector3D(0,0,1));
			}
			//left -PI/2
			else if ( abovebt && !abovetb )
			{
				//System.out.println("at left");
				newq.fromAngleAxis((float) ( -java.lang.Math.PI/2.f), new Vector3D(0,0,-1));
			}
			//top -PI
			else if ( abovebt && abovetb )
			{
				//System.out.println("at top");
				newq.fromAngleAxis((float) (-java.lang.Math.PI), new Vector3D(0,0,1));
			}
			//right PI
			else if ( !abovebt && abovetb )
			{
				//System.out.println("right");
				newq.fromAngleAxis((float) (-java.lang.Math.PI/2.f), new Vector3D(0,0,1));
			}
			
			/*float ammount = 0;
			if(distbt < disttb && distbt <= 50)
				ammount = distbt/50;
			else if ( disttb < distbt && disttb <=50)
				ammount = disttb/50;
			else
				ammount = 1;*/
			
			
			currentq.slerp(newq, 0.1f);
			
			Matrix loc = this.getLocalMatrix();
			//loc.rotateVect(currentq.to)
			//loc.mult(currentq.toRotationMatrix());
			loc.setRotationQuaternion(currentq);
			this.setLocalMatrix(loc);
			
			
			
		/*Vector3D pos = new Vector3D();
		Vector3D rot = new Vector3D();
		this.getGlobalMatrix().decompose(pos, rot, new Vector3D());
		float x = pos.getX() - graphics.width/2;
		float y = pos.getY() - graphics.height/2;
		// = (float) java.lang.Math.atan(y/x);
		float angle = (float) java.lang.Math.atan2(y,x);
		if(angle > (-java.lang.Math.PI/4 ) && angle < (java.lang.Math.PI/4 ))
		{		
			//on left
			this.rotateZGlobal(this.getCenterPointGlobal(), (float) (-java.lang.Math.PI/2 + rot.length()));
		}
		if(angle > ( java.lang.Math.PI/4 ) && angle < (3*java.lang.Math.PI/4 ))
		{
			//at bottom
			this.rotateZGlobal(this.getCenterPointGlobal(), -rot.length());
		}
		if(angle > (-3*java.lang.Math.PI/4 ) && angle < (-java.lang.Math.PI/4 ))
		{
			//at top
			this.rotateZGlobal(this.getCenterPointGlobal(), (float) (java.lang.Math.PI - rot.length()));
		}
		if(angle > (3*java.lang.Math.PI/4 ) || angle < (-3*java.lang.Math.PI/4 ))
		{
			//on right
			this.rotateZGlobal(this.getCenterPointGlobal(),(float) (java.lang.Math.PI/2 - rot.length()));
		}*/
		}
	}

	/**
	 * Close all sub-menus except noCloseMenu
	 * 
	 * @param noCloseMenu
	 */
	public void closeSubmenus(TatinArcCircularMenu noCloseMenu) {
		for (TatinArcCircularMenu menu : subMenus) {
			if (noCloseMenu == null || menu != noCloseMenu)
				menu.setVisible(false);
		}
	}

	public void closeSubmenus() {
		closeSubmenus(null);
	}

	public ListMenu getPhaseMenu() {
		return phaseMenu;
	}

	public void setPhaseMenu(ListMenu phaseMenu) {
		this.phaseMenu = phaseMenu;
	}

	public WorkbenchHandler getBackMenu() {
		return backMenu;
	}

	public void setBackMenu(WorkbenchHandler backMenu) {
		this.backMenu = backMenu;
	}

	public ListMenu getAgentMenu() {
		return agentMenu;
	}

	public void setAgentMenu(ListMenu agentMenu) {
		this.agentMenu = agentMenu;
	}

	public void updateEchoText(String text) {
		workbench.updateEchoText(text);
	}
	
	

	private List<MTTextArea> userInfos = new ArrayList<MTTextArea>();

	private void slideDownUserInfos() {
		for (final MTTextArea userInf : userInfos) {
			IAnimation slideDownUserInfo = new Animation("SlideDownUserInfo",
					new MultiPurposeInterpolator(0f,
							userInf.getHeightXY(TransformSpace.LOCAL), 500f,
							0.25f, 0.25f, 1), this);
			slideDownUserInfo.addAnimationListener(new IAnimationListener() {
				Vector3D initialPosition = userInf
						.getPosition(TransformSpace.RELATIVE_TO_PARENT);

				@Override
				public void processAnimationEvent(AnimationEvent ae) {
					userInf.setPositionRelativeToParent(initialPosition
							.getAdded(new Vector3D(0, ae.getValue())));
				}
			});
			slideDownUserInfo.start();
		}
	}

	public void displayUserInfo(String info) {
		System.out.println("displayUserInfo : " + info);
		final MTTextArea userInfo = new MTTextArea(getPApplet(), getFont());
		userInfo.setText(info);
		userInfo.setFillColor(new MTColor(0, 0, 0, 50));
		userInfo.setStrokeColor(new MTColor(0, 0, 0, 150));
		int pauseLength = 5000; // ms
		IAnimation animation = new Animation("AnimationPause",
				new MultiPurposeInterpolator(0, 1, pauseLength, 0.1f, 0.1f, 1),
				this);

		animation.addAnimationListener(new IAnimationListener() {

			@Override
			public void processAnimationEvent(AnimationEvent ae) {
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
					WorkbenchHandler.this.addChild(userInfo);
					userInfo.setPositionRelativeToParent(new Vector3D(
							0,
							-(getHeightXY(TransformSpace.LOCAL) * 5f / 8f + userInfos
									.size()
									* userInfo
											.getHeightXY(TransformSpace.LOCAL))));
					userInfos.add(userInfo);
					AnimationUtil.scaleIn(userInfo);
					break;
				case AnimationEvent.ANIMATION_UPDATED:
					break;
				case AnimationEvent.ANIMATION_ENDED:
					AnimationUtil.scaleOut(userInfo, true);
					userInfos.remove(userInfo);
					if (!userInfos.isEmpty()) {
						slideDownUserInfos();
					}
					break;
				default:
					break;
				}

			}

		});

		animation.start();

	}
}
