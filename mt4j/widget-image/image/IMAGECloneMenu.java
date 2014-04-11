package utc.tatinpic.gui.widgets.image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
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
import utc.tatinpic.model.Workbench;
import utc.tatinpic.util.manager.PropertyManager;

/**
 * It's a context menu for the MTIMAGE clones in TatinIMAGE.
 * <p>
 * There are three buttons :
 * <ul>
 * <li>Quit the clone</li>
 * <li>Clone the MTIMAGE</li>
 * <li>Close the context menu</li>
 * </ul>
 * </p>
 */
public class IMAGECloneMenu extends MTCircularMenu {
	/**
	 * @uml.property name="mtimage"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="menuClone:utc.tatinpic.gui.widgets.image.MTIMAGEF"
	 */
	private MTIMAGE mtimage;
	private MTEllipse centre;

	public IMAGECloneMenu(PApplet pApplet, MTComponent container,
			final MTIMAGE mtimage, TapAndHoldEvent th, final Workbench workbench,
			final List<Workbench> workbenchs) {
		super(pApplet, 40, 120);
		this.mtimage = mtimage;

		CircularMenuSegmentHandle segment;
		this.setPositionGlobal(th.getCursor().getPosition());

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
							AnimationUtil.bounceOut(IMAGECloneMenu.this, true);
							AnimationUtil.bounceOut(centre, true);
						}
						return false;
					}
				});

		container.addChild(this);
		container.addChild(centre);

		PImage icon = pApplet.loadImage(PropertyManager.getInstance()
				.getProperty(PropertyManager.IMAGE_DIR) + "img-icon-d.png");
		icon.resize(40, 40);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.GRAY);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationUtil.bounceOut(IMAGECloneMenu.this, true);
				IMAGECloneMenu.this.mtimage.cloneIMAGE();
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
				AnimationUtil.bounceOut(IMAGECloneMenu.this, true);
				AnimationUtil.bounceOut(centre, true);
				FileShareMenu fileShareMenu = new FileShareMenu(getRenderer(),
						workbench, workbenchs, mtimage.getFile());
				mtimage.addChild(fileShareMenu);
				fileShareMenu.setPositionRelativeToParent(new Vector3D(mtimage
						.getWidthXY(TransformSpace.LOCAL), 0f));
			}
		});
		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(
				PropertyManager.IMAGE_DIR)
				+ "exit.png");
		icon.resize(40, 40);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.BLACK);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationUtil.bounceOut(IMAGECloneMenu.this, true);
				IMAGECloneMenu.this.mtimage.removeClone(mtimage);
			}
		});
	}

}
