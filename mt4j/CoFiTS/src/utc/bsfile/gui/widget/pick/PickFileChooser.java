package utc.bsfile.gui.widget.pick;

import java.io.File;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.bsfile.gui.widget.image.MTIMAGE;
import utc.bsfile.gui.widget.menu.ChoiceListener;
import utc.bsfile.gui.widget.menu.FileChooser;
import utc.bsfile.gui.widget.metadata.MTMetadata;
import utc.bsfile.gui.widget.movie.MTMOVIE;
import utc.bsfile.gui.widget.pdf.MTPDF;
import utc.bsfile.model.image.IMAGEModel;
import utc.bsfile.model.menu.IMenuModel;
import utc.bsfile.model.menu.ProjectArchitectureModel;
import utc.bsfile.model.menu.TwoLinkedJsonNode;
import utc.bsfile.model.metadata.UnknownFile;
import utc.bsfile.model.movie.MovieModel;
import utc.bsfile.model.pdf.PDFModel;
import utc.bsfile.util.FileExtensionFilter;
import utc.bsfile.util.PositionSequencer;
import utc.bsfile.util.PositionSequencer.Orientation;
import utc.bsfile.util.PropertyManager;

public class PickFileChooser extends FileChooser implements ChoiceListener {
	private MTRectangle filterWindow;

	//private MTImageButton pdfButton;
	private MTSvgButton pdfButton;
	//private MTImageButton movieButton;
	private MTSvgButton movieButton;
	//private MTImageButton imageButton;
	private MTSvgButton imageButton;
	//private MTImageButton htmlButton;
	private MTSvgButton htmlButton;
	//private MTImageButton noFilterButton;
	private MTSvgButton noFilterButton;
	
	//private MTImageButton pdfButton_activated;
	private MTSvgButton pdfButton_activated;
	//private MTImageButton movieButton_activated;
	private MTSvgButton movieButton_activated;
	//private MTImageButton imageButton_activated;
	private MTSvgButton imageButton_activated;
	//private MTImageButton htmlButton_activated;
	private MTSvgButton htmlButton_activated;
	//private MTImageButton noFilterButton_activated;
	private MTSvgButton noFilterButton_activated;

	public PickFileChooser(PApplet applet, IMenuModel model, TwoLinkedJsonNode start) {
		this(applet, 0, 0, 300, 7, model, start);
	}

	public PickFileChooser(PApplet applet, int x, int y, float width, int nbItem, IMenuModel model, TwoLinkedJsonNode start) {
		super(applet, x, y, width, nbItem, model, start);

		addChoiceListener(this);
		setCloseVisible(true);

		filterWindow = new MTRectangle(applet, x - getSpacing() - iconWidth, (y
				+ getSpacing() * 2f + iconHeight) * 5);
		filterWindow.setFillColor(new MTColor(34, 83, 120, 255));
		filterWindow.setNoStroke(true);
		filterWindow.removeAllGestureEventListeners();
		filterWindow.setPositionRelativeToParent(new Vector3D(-iconWidth + 17,
				iconHeight + 135));
		
		PickButtonListener listener = new PickButtonListener();
		Vector3D position = new Vector3D(filterWindow.getPosition(TransformSpace.GLOBAL).x - filterWindow.getWidthXY(TransformSpace.GLOBAL)/2 + getSpacing() + 15,
										 filterWindow.getPosition(TransformSpace.GLOBAL).y - filterWindow.getHeightXY(TransformSpace.GLOBAL)/2 + getSpacing() + 5 );
		
		pdfButton = createSvgIconButton(position, "newPDFFilterIcon.svg", listener);
		pdfButton.setSizeXYGlobal(35,  40);
		pdfButton_activated = createSvgIconButton(position, "newPDFFilterIconON.svg", listener);
		pdfButton_activated.setSizeXYGlobal(35,  40);
		pdfButton_activated.setEnabled(false);
		
		position.y += 40 + getSpacing();
				
		movieButton = createSvgIconButton(position, "newVIDFilterIcon.svg", listener);
		movieButton.setSizeXYGlobal(37,  28);
		movieButton_activated = createSvgIconButton(position, "newVIDFilterIconON.svg", listener);
		movieButton_activated.setSizeXYGlobal(37,  28);
		movieButton_activated.setEnabled(false);
		
		position.y += 40 + getSpacing();
		
		htmlButton = createSvgIconButton(position, "newWEBFilterIcon.svg", listener);
		htmlButton.setSizeXYGlobal(35,  35);
		htmlButton_activated = createSvgIconButton(position, "newWEBFilterIconON.svg", listener);
		htmlButton_activated.setSizeXYGlobal(35,  35);
		htmlButton_activated.setEnabled(false);
		
		position.y += 40 + getSpacing();
		
		imageButton = createSvgIconButton(position, "newIMGFilterIcon.svg", listener);
		imageButton.setSizeXYGlobal(40,  35);
		imageButton_activated = createSvgIconButton(position, "newIMGFilterIconON.svg", listener);
		imageButton_activated.setSizeXYGlobal(40,  35);
		imageButton_activated.setEnabled(false);
		
		position.y += 40 + getSpacing();
		
		noFilterButton = createSvgIconButton(position, "newNoFilterIcon.svg", listener);
		noFilterButton.setSizeXYGlobal(35,  40);
		noFilterButton_activated = createSvgIconButton(position, "newNoFilterIconON.svg", listener);
		noFilterButton_activated.setSizeXYGlobal(35,  40);
		noFilterButton_activated.setEnabled(false);
		
		position.y += 40 + getSpacing();

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

	}

	/**
	 * @param filepath
	 */
	public void createFileViewer(final File filepath) {
		System.out.println(filepath);
		if (FileExtensionFilter.IMG_FILTER.accept(filepath)) {
			IMAGEModel img = new IMAGEModel(filepath.getAbsolutePath());
			MTIMAGE image = new MTIMAGE(getRenderer(), img);
			image.setWidthXYGlobal(200);
			image.setAnchor(PositionAnchor.CENTER);
			image.setPositionGlobal(getCenterPointGlobal());
			if (PropertyManager.getInstance().getProperty(PropertyManager.DEVICE).equals("table")) {
				image.updateOrientation();
			}
			getParent().addChild(image);
			
		} else if (FileExtensionFilter.PDF_FILTER.accept(filepath)) {
			PDFModel pdf = new PDFModel(filepath.getAbsolutePath());
			MTPDF pdfWidget = new MTPDF(getRenderer(), pdf);
			pdfWidget.setAnchor(PositionAnchor.CENTER);
			pdfWidget.setPositionGlobal(getCenterPointGlobal());
			if (PropertyManager.getInstance().getProperty(PropertyManager.DEVICE).equals("table")) {
				pdfWidget.updateOrientation();
			}
			getParent().addChild(pdfWidget);
	 		pdf.addPDFListener(pdfWidget);
			
		} else if (FileExtensionFilter.VIDEO_FILTER.accept(filepath)) {
			MovieModel movie = new MovieModel(filepath.getAbsolutePath());
			MTMOVIE movieWidget = new MTMOVIE(getRenderer(), movie);
			movieWidget.setAnchor(PositionAnchor.CENTER);
			movieWidget.setPositionGlobal(getCenterPointGlobal());
			getParent().addChild(movieWidget);

		} else if (FileExtensionFilter.HTML_FILTER.accept(filepath)) {
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
			UnknownFile unknownFile = new UnknownFile(filepath);
			MTMetadata metaWidget = new MTMetadata(getRenderer(), unknownFile);
			metaWidget.setAnchor(PositionAnchor.CENTER);
			metaWidget.setPositionGlobal(getCenterPointGlobal());
			getParent().addChild(metaWidget);
		}
	}

	@Override
	public void choiceCancelled(ChoiceEvent choiceEvent) {
	}

	protected class PickButtonListener implements IGestureEventListener {
		@SuppressWarnings("static-access")
		public boolean processGestureEvent(MTGestureEvent ge) {
			if (ge instanceof TapEvent) {
				TapEvent tapEvent = (TapEvent) ge;
				Object currentMenu = PickFileChooser.this.getModel().getCurrentMenu();
				int currentLvl = ((ProjectArchitectureModel)getModel()).getCurrentLevel();
				System.out.println("CURRENT LVL : " + currentLvl);
				if (tapEvent.isTapped()) {
					
					// fichier contenant l'architecture du projet
					File jsonFile = new File(PropertyManager.getInstance().JSON_STRUCTURE_FILENAME);
					ProjectArchitectureModel newModel = null;
					
					if (tapEvent.getTarget() == pdfButton) {
						newModel = new ProjectArchitectureModel(jsonFile, 
								(TwoLinkedJsonNode) currentMenu, currentLvl,
								ProjectArchitectureModel.FILE_LEVEL, ProjectArchitectureModel.PDF_FILTER);
						setFilterIconON(FilterName.PDF);
						
					} else if (tapEvent.getTarget() == movieButton) {
						newModel = new ProjectArchitectureModel(jsonFile, 
								(TwoLinkedJsonNode) currentMenu, currentLvl,
								ProjectArchitectureModel.FILE_LEVEL, ProjectArchitectureModel.VIDEO_FILTER);
						setFilterIconON(FilterName.MPG);
						
					} else if (tapEvent.getTarget() == imageButton) {
						newModel = new ProjectArchitectureModel(jsonFile, 
								(TwoLinkedJsonNode) currentMenu, currentLvl,
								ProjectArchitectureModel.FILE_LEVEL, ProjectArchitectureModel.IMG_FILTER);
						setFilterIconON(FilterName.IMG);
						
					} else if (tapEvent.getTarget() == htmlButton) {
						newModel = new ProjectArchitectureModel(jsonFile, 
								(TwoLinkedJsonNode) currentMenu, currentLvl,
								ProjectArchitectureModel.FILE_LEVEL, ProjectArchitectureModel.HTML_FILTER);
						setFilterIconON(FilterName.HTML);
						
					} else if (tapEvent.getTarget() == noFilterButton) {
						newModel = new ProjectArchitectureModel(jsonFile, 
								(TwoLinkedJsonNode) currentMenu, currentLvl,
								ProjectArchitectureModel.FILE_LEVEL, null);
						setFilterIconON(FilterName.NO_FILTER);
					}
					
					setModel(newModel);
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
