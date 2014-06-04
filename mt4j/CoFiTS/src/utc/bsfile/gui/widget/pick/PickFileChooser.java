package utc.bsfile.gui.widget.pick;

import java.io.File;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.bsfile.gui.widget.image.MTIMAGE;
import utc.bsfile.gui.widget.menu.ChoiceListener;
import utc.bsfile.gui.widget.menu.FileChooser;
import utc.bsfile.util.PositionSequencer;
import utc.bsfile.util.PositionSequencer.Orientation;
import utc.bsfile.util.PropertyManager;
import utc.bsfile.gui.widget.metadata.MTMetadata;
import utc.bsfile.gui.widget.movie.MTMOVIE;
import utc.bsfile.gui.widget.pdf.MTPDF;
import utc.bsfile.model.image.IMAGEModel;
import utc.bsfile.model.menu.FileChooserModel;
import utc.bsfile.model.metadata.UnknownFile;
import utc.bsfile.model.movie.MovieModel;
import utc.bsfile.model.pdf.PDFModel;
import utc.bsfile.util.FileExtensionFilter;

public class PickFileChooser extends FileChooser implements ChoiceListener {
	private MTRectangle filterWindow;

	private MTImageButton pdfButton;
	private MTImageButton movieButton;
	private MTImageButton imageButton;
	private MTImageButton htmlButton;
	private MTImageButton noFilterButton;
	
	private MTImageButton pdfButton_activated;
	private MTImageButton movieButton_activated;
	private MTImageButton imageButton_activated;
	private MTImageButton htmlButton_activated;
	private MTImageButton noFilterButton_activated;

	public PickFileChooser(PApplet applet) {
		this(applet, 0, 0, 300, 7);
	}

	public PickFileChooser(PApplet applet, int x, int y, float width,
			int nbItem) {
		super(applet, x, y, width, nbItem, PropertyManager.getInstance()
				.getDirProperty(PropertyManager.FILE_PATH), FileExtensionFilter.NO_FILTER);

		addChoiceListener(this);
		setCloseVisible(true);
		ButtonListener listener = new ButtonListener();

		PositionSequencer position2 = new PositionSequencer(new Vector3D(x
				- getSpacing() - iconWidth, y + getSpacing()), getSpacing(),
				Orientation.VERTICAL);

		
		
		//Create the buttons on the left side for files filtering
		pdfButton = createIconButton(position2.getPosition(),
				"filter-pdf-icon.png", listener);
		pdfButton_activated = createIconButton(position2.getPosition(),
				"filter-pdf-icon-on.png", listener);
		position2.nextPosition(pdfButton);
		movieButton = createIconButton(position2.getPosition(),
				"filter-mpg-icon.png", listener);
		movieButton_activated = createIconButton(position2.getPosition(),
				"filter-mpg-icon-on.png", listener);
		position2.nextPosition(movieButton);
		imageButton = createIconButton(position2.getPosition(),
				"filter-img-icon.png", listener);
		imageButton_activated = createIconButton(position2.getPosition(),
				"filter-img-icon-on.png", listener);
		position2.nextPosition(imageButton);
		htmlButton = createIconButton(position2.getPosition(),
				"filter-html-icon.png", listener);
		htmlButton_activated = createIconButton(position2.getPosition(),
				"filter-html-icon-on.png", listener);
		position2.nextPosition(htmlButton);
		noFilterButton = createIconButton(position2.getPosition(),
				"no-filter-icon.png", listener);
		noFilterButton_activated = createIconButton(position2.getPosition(),
				"no-filter-icon-on.png", listener);
		position2.nextPosition(noFilterButton);

		
		
		
		

		filterWindow = new MTRectangle(applet, x - getSpacing() - iconWidth, (y
				+ getSpacing() * 2f + iconHeight) * 5);
		// filterWindow.setFillColor(new MTColor(159, 182, 205, 200));
		//filterWindow.setFillColor(new MTColor(70, 200, 200, 80));
		filterWindow.setFillColor(new MTColor(34, 83, 120, 255));
		//filterWindow.setStrokeColor(MTColor.BLACK);
		filterWindow.setNoStroke(true);
		filterWindow.removeAllGestureEventListeners();
		// filterWindow.setStrokeColor(MTColor.YELLOW);
		// filterWindow.setPositionGlobal(new Vector3D(-iconWidth, iconHeight));
		filterWindow.setPositionRelativeToParent(new Vector3D(-iconWidth + 17,
				iconHeight + 135));
		// filterWindow.setPositionGlobal(new Vector3D(x - getSpacing() -
		// iconWidth, y + getSpacing()
		// * 2f + iconHeight));

		filterWindow.addChild(pdfButton);
		filterWindow.addChild(movieButton);
		filterWindow.addChild(imageButton);
		filterWindow.addChild(htmlButton);
		filterWindow.addChild(noFilterButton);
		
		setFilterIconON(FilterName.NO_FILTER);

		addChild(filterWindow);
	}

	/**
	 * create a widget corresponding to the selected file type either create the
	 * widget on this thread or using agent thread
	 */
	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		final File file = new File(choiceEvent.getChoice());
		
		if (FileExtensionFilter.IMG_FILTER.accept(file)) {
			IMAGEModel img = new IMAGEModel(file.getAbsolutePath());
			MTIMAGE image = new MTIMAGE(getRenderer(), img);
			image.setWidthXYGlobal(200);
			image.setAnchor(PositionAnchor.CENTER);
			image.setPositionGlobal(getCenterPointGlobal());
			getParent().addChild(image);
			
		} else if (FileExtensionFilter.PDF_FILTER.accept(file)) {
			PDFModel pdf = new PDFModel(file.getAbsolutePath());
			MTPDF pdfWidget = new MTPDF(getRenderer(), pdf);
			pdfWidget.setAnchor(PositionAnchor.CENTER);
			pdfWidget.setPositionGlobal(getCenterPointGlobal());
			getParent().addChild(pdfWidget);
	 		pdf.addPDFListener(pdfWidget);
			
		} else if (FileExtensionFilter.VIDEO_FILTER.accept(file)) {
			MovieModel movie = new MovieModel(file.getAbsolutePath());
			MTMOVIE movieWidget = new MTMOVIE(getRenderer(), movie);
			movieWidget.setAnchor(PositionAnchor.CENTER);
			movieWidget.setPositionGlobal(getCenterPointGlobal());
			getParent().addChild(movieWidget);

		} else if (FileExtensionFilter.HTML_FILTER.accept(file)) {
			// BrowserComponent bc = new BrowserComponent(
			// (AbstractMTApplication) getRenderer(), "Browser",
			// file.getAbsolutePath(), 900, 450, true, workbench);
			// bc.setPositionGlobal(listMenu.getCenterPointGlobal());
			// final TablePhaseScene scene = (TablePhaseScene) ((MTApplication)
			// PickFileChooser.this.getRenderer()).getCurrentScene();
			// scene.getItemLayer().addChild(bc);
			//

		}

		else {
			UnknownFile unknownFile = new UnknownFile(file);
			MTMetadata metaWidget = new MTMetadata(getRenderer(), unknownFile);
			metaWidget.setAnchor(PositionAnchor.CENTER);
			metaWidget.setPositionGlobal(getCenterPointGlobal());
			getParent().addChild(metaWidget);
		}

	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
	}

	protected class ButtonListener implements IGestureEventListener {
		public boolean processGestureEvent(MTGestureEvent ge) {
			if (ge instanceof TapEvent) {
				TapEvent tapEvent = (TapEvent) ge;
				Object currentMenu = PickFileChooser.this.getModel()
						.getCurrentMenu();
				if (tapEvent.isTapped()) {

					if (tapEvent.getTarget() == pdfButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.PDF_FILTER));
						setFilterIconON(FilterName.PDF);
					} else if (tapEvent.getTarget() == movieButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.VIDEO_FILTER));
						setFilterIconON(FilterName.MPG);
					} else if (tapEvent.getTarget() == imageButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.IMG_FILTER));
						setFilterIconON(FilterName.IMG);
					} else if (tapEvent.getTarget() == htmlButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.HTML_FILTER));
						setFilterIconON(FilterName.HTML);
					} else if (tapEvent.getTarget() == noFilterButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.NO_FILTER));
						setFilterIconON(FilterName.NO_FILTER);
					}
				}
			}

			return false;
		}
	}
	
	enum FilterName {
		NO_FILTER,
		PDF,
		IMG,
		MPG,
		HTML
	}
	
	private void setFilterIconON(FilterName f) {
		switch (f) {
			case NO_FILTER :
				filterWindow.addChild(noFilterButton_activated);
				filterWindow.removeChild(noFilterButton);
				filterWindow.addChild(pdfButton);
				filterWindow.removeChild(pdfButton_activated);
				filterWindow.addChild(movieButton);
				filterWindow.removeChild(movieButton_activated);
				filterWindow.addChild(imageButton);
				filterWindow.removeChild(imageButton_activated);
				filterWindow.addChild(htmlButton);
				filterWindow.removeChild(htmlButton_activated);
				break;
			case PDF :
				filterWindow.addChild(pdfButton_activated);
				filterWindow.removeChild(pdfButton);
				filterWindow.addChild(noFilterButton);
				filterWindow.removeChild(noFilterButton_activated);
				filterWindow.addChild(movieButton);
				filterWindow.removeChild(movieButton_activated);
				filterWindow.addChild(imageButton);
				filterWindow.removeChild(imageButton_activated);
				filterWindow.addChild(htmlButton);
				filterWindow.removeChild(htmlButton_activated);
				break;
			case IMG :
				filterWindow.addChild(imageButton_activated);
				filterWindow.removeChild(imageButton);
				filterWindow.addChild(pdfButton);
				filterWindow.removeChild(pdfButton_activated);
				filterWindow.addChild(movieButton);
				filterWindow.removeChild(movieButton_activated);
				filterWindow.addChild(noFilterButton);
				filterWindow.removeChild(noFilterButton_activated);
				filterWindow.addChild(htmlButton);
				filterWindow.removeChild(htmlButton_activated);
				break;
			case MPG :
				filterWindow.addChild(movieButton_activated);
				filterWindow.removeChild(movieButton);
				filterWindow.addChild(pdfButton);
				filterWindow.removeChild(pdfButton_activated);
				filterWindow.addChild(noFilterButton);
				filterWindow.removeChild(noFilterButton_activated);
				filterWindow.addChild(imageButton);
				filterWindow.removeChild(imageButton_activated);
				filterWindow.addChild(htmlButton);
				filterWindow.removeChild(htmlButton_activated);
				break;
			case HTML :
				filterWindow.addChild(htmlButton_activated);
				filterWindow.removeChild(htmlButton);
				filterWindow.addChild(pdfButton);
				filterWindow.removeChild(pdfButton_activated);
				filterWindow.addChild(movieButton);
				filterWindow.removeChild(movieButton_activated);
				filterWindow.addChild(imageButton);
				filterWindow.removeChild(imageButton_activated);
				filterWindow.addChild(noFilterButton);
				filterWindow.removeChild(noFilterButton_activated);
				break;
		}
	}

}
