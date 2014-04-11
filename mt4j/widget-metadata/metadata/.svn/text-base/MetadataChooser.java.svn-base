package utc.tatinpic.gui.widgets.metadata;

import java.io.File;
import java.util.List;

import processing.core.PApplet;
import utc.tatinpic.gui.widgets.menu.FileChooser;
import utc.tatinpic.gui.widgets.menu.ListMenu;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceListener;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.util.fileFilter.FileExtensionFilter;

public class MetadataChooser  extends FileChooser implements ChoiceListener{
	private Workbench workbench;
	private List<Workbench> workbenchs;
	
	public MetadataChooser(PApplet applet, int x, int y, float width, int nbItem,
			File path, Workbench workbench, List<Workbench> workbenchs) {
		super(applet, x, y, width, nbItem, path, new FileExtensionFilter("*"));
		this.workbench = workbench;
		this.workbenchs = workbenchs;
		addChoiceListener(this);
		setCloseVisible(true);
	}

	public MetadataChooser(PApplet applet, int x, int y, float width, int nbItem,
			String path, Workbench workbench, List<Workbench> workbenchs) {
		this(applet, x, y, width, nbItem, new File(path), workbench, workbenchs);
	}

	public MetadataChooser(PApplet applet, Workbench workbench, List<Workbench> workbenchs) {
		this(applet, 0, 0, 300, 5, ".", workbench, workbenchs);
	}
	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		// TODO Auto-generated method stub
		
	
		
		//File file = new File(choiceEvent.getChoice());
		//PDFModel pdf = new PDFModel(file.getAbsolutePath());
		//System.out.println(choiceEvent.getChoice());
		//System.out.println(choiceEvent.getListMenu());
		ListMenu listMenu = choiceEvent.getListMenu();
		//MTMetadata temp = new MTMetadata(getRenderer(), 0, 0, 0, 0, choiceEvent.getChoice());
		//System.out.println("criou o MTMETADATA");
//		listMenu.getParent().addChild(temp);
//		listMenu.setMustBeDestroy(true);
//		MTPDF pdfWidget = new MTPDF(getRenderer(), pdf, true, workbench, workbenchs);
//		pdfWidget.setAnchor(PositionAnchor.CENTER);
//		pdfWidget.setPositionGlobal(listMenu.getCenterPointGlobal());
//		pdfWidget.setAnchor(PositionAnchor.UPPER_LEFT);
//		pdf.addPDFListener(pdfWidget);

		
	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
		// TODO Auto-generated method stub
		
	}

}
