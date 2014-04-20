package utc.tatinpic.gui.widgets.pdf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import utc.tatinpic.gui.widgets.movie.MovieCloneMenu;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.util.manager.PropertyManager;

/** 
 * It's a context menu for the MTPDF clones in TatinPDF.
 * <p>There are three buttons :
 * <ul>
 * <li> Quit the clone</li>
 * <li> Clone the MTPDF</li>
 * <li> Close the context menu</li>
 * </ul>
 * </p>
 */
public class PDFCloneMenu extends MTCircularMenu
{
	/**
	 * @uml.property  name="mtpdf"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="menuClone:utc.tatinpic.gui.widgets.pdf.MTPDF"
	 */
	private MTPDF mtpdf;
	private MTEllipse centre;

	public PDFCloneMenu(PApplet pApplet, MTComponent container, float innerRadius, float outerRadius, final MTPDF mtpdf, TapAndHoldEvent th,final MTPDF tmp,
			final Workbench workbench, final List<Workbench> workbenchs)
	{
		super(pApplet, innerRadius, outerRadius);
		this.mtpdf = mtpdf;
		
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
							AnimationUtil.bounceOut(PDFCloneMenu.this, true);
							AnimationUtil.bounceOut(centre, true);

						}
						return false;
					}
				});
		
		container.addChild(this);
		container.addChild(centre);
		
		PImage icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(PropertyManager.IMAGE_DIR) + "pdf-icon-d.png");
		icon.resize(40, 40);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.RED);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AnimationUtil.bounceOut(PDFCloneMenu.this, true);
				PDFCloneMenu.this.mtpdf.clonePDF();
			}
		});
		
		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(PropertyManager.IMAGE_DIR) + "share-icon.png");
		icon.resize(48, 48);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.GRAY);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AnimationUtil.bounceOut(PDFCloneMenu.this, true);
				AnimationUtil.bounceOut(centre, true);
				FileShareMenu fileShareMenu = new FileShareMenu(getRenderer(), workbench, workbenchs, mtpdf.getFile());
				mtpdf.addChild(fileShareMenu);
				fileShareMenu.setPositionRelativeToParent(new Vector3D(mtpdf.getWidthXY(TransformSpace.LOCAL), 0f));
			}
		});
		
		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(PropertyManager.IMAGE_DIR) + "exit.png");
		icon.resize(40, 40);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.BLACK);
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AnimationUtil.bounceOut(PDFCloneMenu.this, true);
				PDFCloneMenu.this.mtpdf.removeClone(tmp);
			}
		});


		
		icon = pApplet.loadImage(PropertyManager.getInstance().getProperty(PropertyManager.IMAGE_DIR) + "cancel.png");
		icon.resize(40, 40);
		segment = this.createSegment(icon);
		segment.setFillColor(MTColor.PURPLE);
		
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AnimationUtil.bounceOut(PDFCloneMenu.this, true);
			}
		});
	}
	

}
