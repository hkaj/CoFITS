package utc.bsfile.gui.widget.pdf;

import java.util.ArrayList;
import java.util.Iterator;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import utc.bsfile.model.pdf.Bookmark;
import utc.bsfile.model.pdf.PDFModel;
import utc.bsfile.util.PropertyManager;

public class PDFOutline extends MTRectangle
{
	/**
	 * @uml.property  name="pdf"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected PDFModel pdf;
	/**
	 * @uml.property  name="applet"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private MTApplication applet;
	/**
	 * @uml.property  name="width"
	 */
	private float width;
	/**
	 * @uml.property  name="list"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private MTList list;

	public PDFOutline(MTApplication applet, float x, float y, float width, float height, PDFModel pdf)
	{
		super(applet, x, y, width, height);
		this.applet = applet;
		this.width = width;
		this.pdf = pdf;
		setStrokeColor(MTColor.BLACK);
		setAnchor(PositionAnchor.UPPER_LEFT);
		list = new MTList(applet, x + 5, y + 50, width - 10, height - 100);
		list.setStrokeColor(MTColor.BLACK);
		String sfont = PropertyManager.getInstance().getProperty(PropertyManager.PDF_FONT);
		MTTextArea txtTitle = new MTTextArea(applet, x + 40, y + 10, width - 50, 40, FontManager.getInstance().createFont(applet, sfont, 25, MTColor.SILVER, true));
		txtTitle.setText("OUTLINE");
		txtTitle.removeAllGestureEventListeners();
		
		MTTextArea btnCancel = new MTTextArea(applet, x + width - 45, y + 5, 40, 40);
		PImage iconCancel = applet.loadImage(PropertyManager.getInstance().getProperty(PropertyManager.ICONE_DIR) + "cancel.png");
		iconCancel.set(38, 38, 0);
		btnCancel.setTexture(iconCancel);
		btnCancel.removeAllGestureEventListeners();
		btnCancel.registerInputProcessor(new TapProcessor(applet, 25, true, 350));
		btnCancel.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent arg0)
			{
				PDFOutline.this.destroy();
				return false;
			}		
		});
		
		@SuppressWarnings("unchecked")
		ArrayList<Bookmark> title = (ArrayList<Bookmark>) pdf.getBookmark().clone();
		title.add(0, new Bookmark("< First >", 0));
		title.add(new Bookmark("< Last >", pdf.getNumberOfPages() - 1));

		createBookmarkLevel(title, 0);
		
		addChild(txtTitle);
		addChild(btnCancel);
		addChild(list);
	}
	
	/**
	 * <p>Creates the different levels of the outline.</p>
	 * <p>At each title's child, we indent a little bit more and the color and font size change.</p>
	 * 
	 * @param kids list of bookmark.
	 * @param space level of the outline. First level is generally 0.
	 */
	private void createBookmarkLevel(ArrayList<Bookmark> kids, int space)
	{
		@SuppressWarnings("rawtypes")
		Iterator it = kids.iterator();
		int iFont = 0;
		MTColor iColor = null;
		switch(space)
		{
			case 0 : iFont = 20; iColor = MTColor.BLACK; break;
			case 1 : iFont = 18; iColor = MTColor.GREY; break;
			case 2 : iFont = 16; iColor = MTColor.SILVER; break;
			default : iFont = 14; iColor = MTColor.BLACK; break;
		}

		while(it.hasNext())
		{
			final Bookmark tmp = (Bookmark) it.next();
			MTListCell cell = new MTListCell(applet, width - 15, 40);
			cell.registerInputProcessor(new TapProcessor(applet, 25, true, 350));
			
			cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				public boolean processGestureEvent(MTGestureEvent ge)
				{
					if(((TapEvent) ge).isTapped())
					{
						pdf.setPageNumber(tmp.getPage() + 1);
					}
					return false;
				}
			});
			
			MTTextArea txtArea = new MTTextArea(applet, 10, 0, 300, 30);
			txtArea.setText(tmp.getTitle());	
			txtArea.setAnchor(PositionAnchor.UPPER_LEFT);
			Vector3D pos = this.getPosition(TransformSpace.LOCAL);
			pos.setX(pos.getX() + 10 + (15 * space));
			txtArea.setPositionRelativeToParent(pos);
			txtArea.setFont(FontManager.getInstance().createFont(applet, "Constantia", iFont, iColor, true));
			
			cell.addChild(txtArea);
			
			list.addListElement(cell);
			
			if (tmp.hasKids())
			{
				space += 1;
				createBookmarkLevel(tmp.getKids(), space);
				space -= 1;
			}
		}
	}

}
