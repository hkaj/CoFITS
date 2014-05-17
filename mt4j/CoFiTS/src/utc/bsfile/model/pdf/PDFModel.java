package utc.bsfile.model.pdf;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.event.EventListenerList;

import utc.bsfile.gui.widget.pdf.PDFListener;

import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PDFModel
{
	/**
	 * @uml.property  name="pdfFile"
	 */
	private File pdfFile;
	/**
	 * @uml.property  name="pageNumber"
	 */
	private int pageNumber = 1;
	/**
	 * @uml.property  name="pdfRenderer"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.sun.pdfview.OutlineNode"
	 */
	private PDFFile pdfRenderer;
	/**
	 * @uml.property  name="currentPage"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private PDFPage currentPage;
	/**
	 * @uml.property  name="pdfValid"
	 */
	private boolean pdfValid = true;
	/**
	 * @uml.property  name="lstBookmark"
	 * @uml.associationEnd  
	 */
	private ArrayList<Bookmark> lstBookmark = new ArrayList<Bookmark>();

	/**
	 * @uml.property  name="listeners"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private final EventListenerList listeners = new EventListenerList();

	/**
	 * 
	 * @param fileName absolute path of the PDF File
	 */
	public PDFModel(String fileName)
	{
		try
		{
			pdfFile = new File(fileName);
			pdfRenderer = null;

			RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			pdfRenderer = new PDFFile(buf);

			currentPage = pdfRenderer.getPage(pageNumber);
		}
		catch(Exception e)
		{
			System.out.println("PDFExample.PDFExample - Error : " + e);
			this.setPdfValid(false);
		}
	}

	/**
	 * 
	 * @param file PDF file
	 */
	public PDFModel(File file)
	{
		this(file.getAbsolutePath());
	}

	/**
	 * Gets the number of page.
	 * 
	 * @return the number of page.
	 */
	public int getNumberOfPages()
	{
		return pdfRenderer.getNumPages();
	}

	/**
	 * Sets the page number of the document. It's start with 0.
	 * @param pageNumber  number from 0 to  {@link #getNumberOfPage()}  -1.
	 * @throws Exception  if you try to access an non-existent page.
	 * @uml.property  name="pageNumber"
	 */
	public void setPageNumber(int pageNumber)
	{
		if(pageNumber <= getNumberOfPages() && pageNumber >= 1)
		{
			this.pageNumber = pageNumber;

		}
		else
		{
			if(pageNumber < 1)
				this.pageNumber = 1;
			if(pageNumber > getNumberOfPages())
				this.pageNumber = getNumberOfPages();
		}
		this.currentPage = pdfRenderer.getPage(this.pageNumber);
		firePageChanged(this.pageNumber);
	}

	/**
	 * Gets the current page number.
	 * @return  the current page number.
	 * @uml.property  name="pageNumber"
	 */
	public int getPageNumber()
	{
		return pageNumber;
	}

	/**
	 * Gets the pdf File.
	 * 
	 * @return a File.
	 */
	public File getFile()
	{
		return pdfFile;
	}

	/**
	 * Gets the bookmarks of the pdf. Returns an empty array if there is no bookmark.
	 * 
	 * @return An array of bookmarks, or an empty array if there is no bookmark.
	 */
	public ArrayList<Bookmark> getBookmark()
	{
		try
		{
			if(lstBookmark.isEmpty()) // we avoid getting several times the bookmarks with this trick
			{
				OutlineNode o = pdfRenderer.getOutline();
				@SuppressWarnings("unchecked")
				Enumeration<OutlineNode> e = o.children();
				while(e.hasMoreElements())
				{
					lstBookmark.add(new Bookmark(pdfRenderer, (OutlineNode) e.nextElement()));
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("models.getBookmark - Error : " + ex);
		}

		return lstBookmark;
	}

	/**
	 * Turns the page to get the next one.
	 */
	public void nextPage()
	{
		if(pageNumber < getNumberOfPages())
		{
			currentPage = pdfRenderer.getPage(++pageNumber);
			firePageChanged(pageNumber);
		}
	}

	/**
	 * Turns the page to get the previous one.
	 */
	public void previousPage()
	{
		if(pageNumber > 1)
		{
			currentPage = pdfRenderer.getPage(--pageNumber);
			firePageChanged(pageNumber);
		}
	}

	/**
	 * Gets the image of the current page.
	 * 
	 * @param width width of the image.
	 * @param height height of the image.
	 * @return An Image.
	 * @see #getPageNumber()
	 */
	public Image getImagePage(int width, int height)
	{
		Rectangle clip = new Rectangle(0, 0, (int) currentPage.getBBox().getWidth(), (int) currentPage.getBBox().getHeight());
		Image img = currentPage.getImage(
				width, height, // width & height
				clip, // clip rect
				null, // null for the ImageObserver
				true, // fill background with white
				true // block until drawing is done
				);
		return img;
	}

	/**
	 * Gets the image of the current page.
	 * 
	 * @param scaleFactor scale you want to use to calculate the dimensions of the image.
	 * @return An Image.
	 * @see #getPageNumber()
	 */
	public Image getImagePage(double scaleFactor)
	{
		if(currentPage != null)
		{
			int w = (int) (currentPage.getBBox().getWidth());
			int h = (int) (currentPage.getBBox().getHeight());

			Rectangle clip = new Rectangle(0, 0, w, h);
			Image img = currentPage.getImage(
					(int) (w * scaleFactor), (int) (h * scaleFactor), // width & height
					clip, // clip rect
					null, // null for the ImageObserver
					true, // fill background with white
					true // block until drawing is done
					);
			return img;
		}
		return null;
	}

	/**
	 * Adds a PDFListener to the PDF.
	 * 
	 * @param l listener.
	 * @see #removePDFListener(PDFListener)
	 */
	public void addPDFListener(PDFListener l)
	{
		listeners.add(PDFListener.class, l);
	}

	/**
	 * Removes a PDFListener to the PDF.
	 * 
	 * @param l listener.
	 * @see #addPDFListener(PDFListener)
	 */
	public void removePDFListener(PDFListener l)
	{
		listeners.remove(PDFListener.class, l);
	}

	/**
	 * Gets the list of PDFListener that the PDF holds.
	 * 
	 * @return a {@link PDFListener} list.
	 */
	public PDFListener[] getPDFListeners()
	{
		return listeners.getListeners(PDFListener.class);
	}

	/**
	 * Functions called when the page number has changed.
	 * 
	 * @param newPage new current page number.
	 */
	protected void firePageChanged(int newPage)
	{
		for(PDFListener l : getPDFListeners())
		{
			l.pageChanged(newPage);
		}
	}

	/**
	 * @param pdfValid
	 * @uml.property  name="pdfValid"
	 */
	public void setPdfValid(boolean pdfValid)
	{
		this.pdfValid = pdfValid;
	}

	/**
	 * @return
	 * @uml.property  name="pdfValid"
	 */
	public boolean isPdfValid()
	{
		return pdfValid;
	}
}