package utc.bsfile.gui.widget.pdf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4jx.components.visibleComponents.widgets.circularmenu.CircularMenuSegmentHandle;
import org.mt4jx.components.visibleComponents.widgets.circularmenu.MTCircularMenu;
import org.mt4jx.util.animation.AnimationUtil;

import processing.core.PApplet;
import processing.core.PImage;
import utc.bsfile.gui.widget.menu.ListMenu.ChoiceEvent;
import utc.bsfile.gui.widget.menu.ListMenu.ChoiceListener;
import utc.bsfile.util.PropertyManager;

/**
 * It's a context menu for the MTPDF .
 * <p>
 * There are two buttons :
 * <ul>
 * <li>Open/Close the outlines of the current PDF file</li>
 * <li>Close the current pdf
 * <li>The center button closes this context menu</li>
 * </ul>
 * </p>
 */
public class PDFMenu extends MTCircularMenu implements ChoiceListener {
	/**
	 * The center button of this context menu which permits to close it.
	 * 
	 * @uml.property name="centre"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MTEllipse centre = null;
	/**
	 * @uml.property name="mtpdf"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="menu:utc.tatinpic.gui.widgets.pdf.MTPDF"
	 */
	private MTPDF mtpdf;

	public PDFMenu(PApplet pApplet, MTComponent container, float innerRadius,
			float outerRadius, MTPDF mtpdf, TapAndHoldEvent th) {
		super(pApplet, innerRadius, outerRadius);
		this.mtpdf = mtpdf;

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
				.getProperty(PropertyManager.ICONE_DIR) + "parent-icon.png");
		icon.resize(48, 48);

		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(
				PropertyManager.ICONE_DIR)
				+ "bookmark.png");
		icon.resize(48, 48);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.SILVER);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationUtil.bounceOut(here, true);
				AnimationUtil.bounceOut(centre, true);
				PDFMenu.this.mtpdf.showOutline();
			}
		});

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
				PDFMenu.this.mtpdf.remove();
			}
		});

	}

	public void destroy() {
		centre.destroy();
		super.destroy();
	}

	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		this.mtpdf.remove();
	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
	}
}
