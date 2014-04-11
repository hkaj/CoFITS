package utc.tatinpic.gui.widgets.image;

import java.io.File;
import java.util.List;

import processing.core.PApplet;

import utc.tatinpic.gui.widgets.menu.FileChooser;
import utc.tatinpic.gui.widgets.menu.ListMenu;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceListener;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.model.image.IMAGEModel;
import utc.tatinpic.util.fileFilter.FileExtensionFilter;
import utc.tatinpic.util.manager.PropertyManager;

public class IMAGEChooser extends FileChooser implements ChoiceListener {

	private Workbench workbench;
	private List<Workbench> workbenchs;

	public IMAGEChooser(PApplet applet, int x, int y, float width, int nbItem,
			File path, Workbench workbench) {
		super(applet, x, y, width, nbItem, path,  FileExtensionFilter.IMG_FILTER);
		this.workbench = workbench;
		addChoiceListener(this);
		setCloseVisible(true);
	}

	public IMAGEChooser(PApplet applet, int x, int y, float width, int nbItem,
			String path, Workbench workbench) {
		this(applet, x, y, width, nbItem, new File(path), workbench);
	}

	public IMAGEChooser(PApplet applet, Workbench workbench) {
		this(applet, 0, 0, 300, 5, PropertyManager.getInstance()
				.getDirProperty(PropertyManager.DROPBOX_DIR)
				+ workbench.getTeamMember() + "-tatin-pic/", workbench);
	}

	@Override
	// To Open an Image !!!
	public void choiceSelected(ChoiceEvent choiceEvent) {
		File file = new File(choiceEvent.getChoice());
		
		IMAGEModel image = new IMAGEModel(file.getAbsolutePath());
		MTIMAGE photo = new MTIMAGE(getRenderer(), image, true, workbench, workbenchs);
		
		ListMenu listMenu = choiceEvent.getListMenu();
		listMenu.getParent().addChild(photo);
		listMenu.setMustBeDestroy(true);
	}

	
	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
	}
}
