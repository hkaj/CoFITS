package utc.bsfile.model.pdf;

import java.util.ArrayList;
import java.util.Enumeration;

import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.action.GoToAction;

public class Bookmark
{
	/**
	 * @uml.property  name="title"
	 */
	private String title;
	/**
	 * @uml.property  name="page"
	 */
	private int page;
	/**
	 * @uml.property  name="kids"
	 */
	private ArrayList<Bookmark> kids = new ArrayList<Bookmark>();
	
	public Bookmark(String title, int page){
		this.title = title;
		this.page = page;
	}
	
	public Bookmark(PDFFile f, OutlineNode node)
	{
		try
		{
			title = node.toString();
			GoToAction action = (GoToAction) node.getAction();
			
			page = f.getPageNumber(action.getDestination().getPage());
		
			if (node.children() != null)
			{
				@SuppressWarnings("unchecked")
				Enumeration<OutlineNode> e = node.children();
				while(e.hasMoreElements())
				{
					kids.add(new Bookmark(f, (OutlineNode)e.nextElement()));
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("model.bookmark - Error : " + e);
		}
	}

	/**
	 * @return
	 * @uml.property  name="title"
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 * @uml.property  name="title"
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return
	 * @uml.property  name="page"
	 */
	public int getPage()
	{
		return page;
	}

	/**
	 * @param page
	 * @uml.property  name="page"
	 */
	public void setPage(int page)
	{
		this.page = page;
	}

	public ArrayList<Bookmark> getKids()
	{
		return kids;
	}

	public void setKids(ArrayList<Bookmark> kids)
	{
		this.kids = kids;
	}
	
	public boolean hasKids()
	{
		return !kids.isEmpty();
	}
	
	public String toString()
	{
		return   "{ - Bookmark\n Title: \t" + title
				+ "\n Page:  \t" + page
				+ "\n Kids:  \t" + kids + "\n}";
	}
	
}
