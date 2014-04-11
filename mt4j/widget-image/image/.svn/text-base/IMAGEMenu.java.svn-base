package utc.tatinpic.gui.widgets.image;

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
import utc.tatinpic.gui.widgets.fileshare.FileShareMenu;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceEvent;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceListener;
import utc.tatinpic.gui.widgets.metadata.MetadataMenu;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.util.manager.PropertyManager;

/**
 * It's a context menu for the MTIMAGE master in TatinIMAGE.
 * <p>
 * There are five buttons :
 * <ul>
 * <li>Open an other IMAGE file</li>
 * <li>Open/Close the outlines of the current IMAGE file</li>
 * <li>Clone the current MTIMAGE</li>
 * <li>Quit the application TatinIMAGE</li>
 * <li>The center button closes this context menu</li>
 * </ul>
 * </p>
 */
public class IMAGEMenu extends MTCircularMenu implements ChoiceListener {
	/**
	 * The center button of this context menu which permits to close it.
	 * 
	 * @uml.property name="centre"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MTEllipse centre = null;
	/**
	 * @uml.property name="mtimage"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="menu:utc.tatinpic.gui.widgets.image.MTIMAGE"
	 */
	private MTIMAGE mtimage;

	public IMAGEMenu(PApplet pApplet, MTComponent container, float innerRadius,
			float outerRadius, MTIMAGE mtimage, TapAndHoldEvent th,
			final Workbench workbench, final List<Workbench> workbenchs) {
		super(pApplet, innerRadius, outerRadius);
		this.mtimage = mtimage;
		CircularMenuSegmentHandle segment;
		this.setPositionGlobal(th.getCursor().getPosition());
		final MTCircularMenu here = this;
		centre = new MTEllipse(pApplet, th.getCursor().getPosition(), 40, 40);
		centre.removeAllGestureEventListeners();
		centre.setFillColor(MTColor.RED);
		centre.setTexture(pApplet.loadImage(PropertyManager.getInstance()
				.getProperty(PropertyManager.IMAGE_DIR) + "cancel.png"));

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
				.getProperty(PropertyManager.IMAGE_DIR) + "img-icon-d.png");
		icon.resize(48, 48);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.GRAY);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationUtil.bounceOut(here, true);
				AnimationUtil.bounceOut(centre, true);
				IMAGEMenu.this.mtimage.cloneIMAGE();
			}
		});

		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(
				PropertyManager.IMAGE_DIR)
				+ "share-icon.png");
		icon.resize(48, 48);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.GRAY);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationUtil.bounceOut(here, true);
				AnimationUtil.bounceOut(centre, true);
				FileShareMenu fileShareMenu = new FileShareMenu(getRenderer(),
						workbench, workbenchs, IMAGEMenu.this.mtimage.getFile());
				IMAGEMenu.this.mtimage.addChild(fileShareMenu);
				fileShareMenu.setPositionRelativeToParent(new Vector3D(
						IMAGEMenu.this.mtimage.getWidthXY(TransformSpace.LOCAL),
						0f));
			}
		});

		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(
				PropertyManager.IMAGE_DIR)
				+ "exit.png");
		icon.resize(48, 48);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.BLACK);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationUtil.bounceOut(here, true);
				AnimationUtil.bounceOut(centre, true);
				IMAGEMenu.this.mtimage.remove();
			}
		});

		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(
				PropertyManager.IMAGE_DIR)
				+ "parent-icon.png");
		icon.resize(48, 48);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.GRAY);

		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationUtil.bounceOut(here, true);
				AnimationUtil.bounceOut(centre, true);

				IMAGEChooser imageChooser = new IMAGEChooser(getRenderer(), 10,
						10, 300.f, 5, PropertyManager.getInstance()
						.getDirProperty(PropertyManager.DROPBOX_DIR)
						+ workbench.getTeamMember() + "-tatin-pic/", workbench);
				imageChooser.addChoiceListener(IMAGEMenu.this);
				imageChooser.setAnchor(PositionAnchor.CENTER);
				imageChooser.setPositionGlobal(getCenterPointGlobal());
				imageChooser.setAnchor(PositionAnchor.UPPER_LEFT);
				getParent().addChild(imageChooser);

			}
		});
	}

	public void destroy() {
		centre.destroy();
		super.destroy();
	}

	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		this.mtimage.remove();
	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
	}
}
