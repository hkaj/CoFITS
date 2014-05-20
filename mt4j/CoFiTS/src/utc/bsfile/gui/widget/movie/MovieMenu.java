package utc.bsfile.gui.widget.movie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.circularmenu.CircularMenuSegmentHandle;
import org.mt4jx.components.visibleComponents.widgets.circularmenu.MTCircularMenu;
import org.mt4jx.util.animation.AnimationUtil;

import processing.core.PApplet;
import processing.core.PImage;
import utc.bsfile.gui.widget.menu.ListMenu.ChoiceEvent;
import utc.bsfile.gui.widget.menu.ListMenu.ChoiceListener;
import utc.bsfile.gui.widget.movie.MTMOVIE;
import utc.bsfile.util.PropertyManager;

public class MovieMenu extends MTCircularMenu implements ChoiceListener {
	/**
	 * The center button of this context menu which permits to close it.
	 * 
	 * @uml.property name="centre"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MTEllipse centre = null;
	/**
	 * @uml.property name="MTMOVIE"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="menu:utc.tatinpic.gui.widgets.movie.MTMOVIE"
	 */
	private MTMOVIE MTMOVIE;

	public MovieMenu(PApplet pApplet, MTComponent container, float innerRadius,
			float outerRadius, MTMOVIE MTMOVIE, TapAndHoldEvent th) {
		super(pApplet, innerRadius, outerRadius);
		this.MTMOVIE = MTMOVIE;

		CircularMenuSegmentHandle segment;
		this.setPositionGlobal(th.getCursor().getPosition());
		final MTCircularMenu here = this;
		centre = new MTEllipse(pApplet, th.getCursor().getPosition(), 40, 40);
		centre.removeAllGestureEventListeners();
		centre.setFillColor(MTColor.RED);
		centre.setTexture(pApplet.loadImage(PropertyManager.getInstance()
				.getProperty(PropertyManager.ICONE_DIR) + "cancel.png"));

		centre.registerInputProcessor(new TapProcessor(pApplet));
		centre.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {

					public boolean processGestureEvent(MTGestureEvent ge) {
						if (ge instanceof TapEvent) {

							AnimationUtil.bounceOut(here, true);
							AnimationUtil.bounceOut(centre, true);

						}
						return false;
					}
				});

		container.addChild(this);
		container.addChild(centre);

		PImage icon = pApplet.loadImage(PropertyManager.getInstance()
				.getProperty(PropertyManager.ICONE_DIR) + "mpg-icon-d.png");
		icon.resize(48, 48);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.GRAY);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationUtil.bounceOut(here, true);
				AnimationUtil.bounceOut(centre, true);
			}
		});

		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(
				PropertyManager.ICONE_DIR)
				+ "share-icon.png");
		icon.resize(48, 48);
		

		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(
				PropertyManager.ICONE_DIR)
				+ "exit.png");
		icon.resize(48, 48);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.BLACK);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationUtil.bounceOut(here, true);
				AnimationUtil.bounceOut(centre, true);
				MovieMenu.this.MTMOVIE.remove();
			}
		});

		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(
				PropertyManager.ICONE_DIR)
				+ "parent-icon.png");
		icon.resize(48, 48);
		
	}

	public void destroy() {
		centre.destroy();
		super.destroy();
	}

	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		this.MTMOVIE.remove();
	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
	}
}
