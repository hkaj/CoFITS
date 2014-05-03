package utc.tatinpic.model.widgets.pdf;

import java.util.EventListener;

public interface PDFListener extends EventListener
{
	/**
	 * Function called when the current page of a PDF document has changed.
	 * @param newPage new current page of the PDF.
	 */
	public void pageChanged(int newPage);
}
