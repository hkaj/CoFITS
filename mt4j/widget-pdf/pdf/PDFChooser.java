package utc.tatinpic.gui.widgets.pdf;

import java.io.File;
import java.util.List;

import processing.core.PApplet;
import utc.tatinpic.gui.widgets.menu.FileChooser;
import utc.tatinpic.gui.widgets.menu.ListMenu;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceListener;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.model.pdf.PDFModel;
import utc.tatinpic.util.fileFilter.FileExtensionFilter;
import utc.tatinpic.util.manager.PropertyManager;

public class PDFChooser extends FileChooser implements ChoiceListener {
	private Workbench workbench;
	private List<Workbench> workbenchs;

	public PDFChooser(PApplet applet, int x, int y, float width, int nbItem,
			File path, Workbench workbench, List<Workbench> workbenchs) {
		super(applet, x, y, width, nbItem, path, new FileExtensionFilter("pdf"));
		this.workbench = workbench;
		this.workbenchs = workbenchs;
		addChoiceListener(this);
		setCloseVisible(true);
	}

	public PDFChooser(PApplet applet, int x, int y, float width, int nbItem,
			String path, Workbench workbench, List<Workbench> workbenchs) {
		this(applet, x, y, width, nbItem, new File(path), workbench, workbenchs);
	}

	public PDFChooser(PApplet applet, Workbench workbench, List<Workbench> workbenchs) {
		this(applet, 0, 0, 300, 5, PropertyManager.getInstance()
				.getDirProperty(PropertyManager.DROPBOX_DIR)
				+ workbench.getTeamMember() + "-tatin-pic/", workbench, workbenchs);
	}

	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		File file = new File(choiceEvent.getChoice());
		PDFModel pdf = new PDFModel(file.getAbsolutePath());

		ListMenu listMenu = choiceEvent.getListMenu();

		MTPDF pdfWidget = new MTPDF(getRenderer(), pdf, true, workbench, workbenchs);
		pdfWidget.setAnchor(PositionAnchor.CENTER);
		pdfWidget.setPositionGlobal(listMenu.getCenterPointGlobal());
		pdfWidget.setAnchor(PositionAnchor.UPPER_LEFT);
		//pdf.addPDFListener(pdfWidget);

		listMenu.getParent().addChild(pdfWidget);
		listMenu.setMustBeDestroy(true);
	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
	}
}
