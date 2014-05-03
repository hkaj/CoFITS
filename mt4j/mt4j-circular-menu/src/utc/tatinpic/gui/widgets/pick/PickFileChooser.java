package utc.tatinpic.gui.widgets.pick;

import jade.core.behaviours.OneShotBehaviour;

import java.io.File;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import utc.tatinpic.agent.exchange.mm.BrainstormingMMImporter;
import utc.tatinpic.agent.exchange.txt.BrainstormingTxtImporter;
import utc.tatinpic.gui.LocationReference;
import utc.tatinpic.gui.phase.TablePhaseScene;
import utc.tatinpic.gui.phase.paperboard.widget.ImageKlonkWidget;
import utc.tatinpic.gui.physics.PhysicWrapper;
import utc.tatinpic.gui.physics.PhysicsHelper;
import utc.tatinpic.gui.physics.shapes.IPhysicsComponent;
import utc.tatinpic.gui.widgets.browser.BrowserComponent;
import utc.tatinpic.gui.widgets.menu.FileChooser;
import utc.tatinpic.gui.widgets.menu.ListMenu;
import utc.tatinpic.gui.widgets.menu.ListMenu.ChoiceListener;
import utc.tatinpic.gui.widgets.metadata.MTMetadata;
import utc.tatinpic.gui.widgets.movie.MTMOVIE;
import utc.tatinpic.model.Identification;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.model.image.IMAGEModel;
import utc.tatinpic.model.klonk.Klonk;
import utc.tatinpic.model.menu.FileChooserModel;
import utc.tatinpic.model.metadata.UnknownFile;
import utc.tatinpic.model.movie.MovieModel;
import utc.tatinpic.model.pdf.PDFModel;
import utc.tatinpic.model.widgets.image.MTIMAGE;
import utc.tatinpic.model.widgets.pdf.MTPDF;
import utc.tatinpic.util.fileFilter.FileExtensionFilter;
import utc.tatinpic.util.manager.IDManager;
import utc.tatinpic.util.manager.PropertyManager;
import utc.tatinpic.util.positionSequencer.PositionSequencer;
import utc.tatinpic.util.positionSequencer.PositionSequencer.Orientation;

public class PickFileChooser extends FileChooser implements ChoiceListener {
	private MTRectangle filterWindow;

	private MTImageButton pdfButton;
	private MTImageButton movieButton;
	private MTImageButton imageButton;
	private MTImageButton htmlButton;
	private MTImageButton noFilterButton;

	private Workbench workbench;
	private List<Workbench> workbenchs;

	public PickFileChooser(PApplet applet, Workbench workbench,
			List<Workbench> workbenchs) {
		this(applet, 0, 0, 300, 5, workbench, workbenchs);
	}

	private PickFileChooser(PApplet applet, int x, int y, float width,
			int nbItem, Workbench workbench, List<Workbench> workbenchs) {
		super(applet, x, y, width, nbItem, PropertyManager.getInstance()
				.getDirProperty(PropertyManager.DROPBOX_DIR)
				+ workbench.getTeamMember() + "-tatin-pic/",
				FileExtensionFilter.NO_FILTER);
		this.workbench = workbench;
		this.workbenchs = workbenchs;

		this.setWorkbench(workbench);
		this.setWorkbenches(workbenchs);

		addChoiceListener(this);
		setCloseVisible(true);
		ButtonListener listener = new ButtonListener();

		PositionSequencer position2 = new PositionSequencer(new Vector3D(x
				- getSpacing() - iconWidth, y + getSpacing()), getSpacing(),
				Orientation.VERTICAL);

		pdfButton = createIconButton(position2.getPosition(),
				"IMG:/pdf-icon.png", listener);
		position2.nextPosition(pdfButton);
		movieButton = createIconButton(position2.getPosition(),
				"IMG:/mpg-icon.png", listener);
		position2.nextPosition(movieButton);
		imageButton = createIconButton(position2.getPosition(),
				"IMG:/img-icon.png", listener);
		position2.nextPosition(imageButton);
		htmlButton = createIconButton(position2.getPosition(),
				"IMG:/html-icon.png", listener);
		position2.nextPosition(htmlButton);
		noFilterButton = createIconButton(position2.getPosition(),
				"IMG:/no-filter-icon.png", listener);
		position2.nextPosition(noFilterButton);

		filterWindow = new MTRectangle(applet, x - getSpacing() - iconWidth, (y
				+ getSpacing() * 2f + iconHeight) * 5);
		// filterWindow.setFillColor(new MTColor(159, 182, 205, 200));
		filterWindow.setFillColor(new MTColor(70, 200, 200, 80));
		filterWindow.setStrokeColor(MTColor.BLACK);
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

		addChild(filterWindow);
	}

	/**
	 * create a widget corresponding to the selected file type
	 * either create the widget on this thread or using agent thread
	 */
	@Override
	public void choiceSelected(ChoiceEvent choiceEvent) {
		final File file = new File(choiceEvent.getChoice());
		final ListMenu listMenu = choiceEvent.getListMenu();
		System.out.println("Tah pegando o evento");
		MTRectangle widget = null;
		PhysicWrapper physic = null;
		LoadOnAgent behaviour = null;
		
		if (FileExtensionFilter.IMG_FILTER.accept(file)) {
			
			/*behaviour = new LoadOnAgent(widget,physic,listMenu.getCenterPointGlobal()){						
				@Override
				public void action() {
					IMAGEModel img = new IMAGEModel(file.getAbsolutePath());
					widget = new MTIMAGE(getRenderer(), img, true, workbench,
							workbenchs);
					if( ((MTApplication) PickFileChooser.this.applet).getCurrentScene() instanceof TablePhaseScene )
					{*/
						final TablePhaseScene scene = (TablePhaseScene) ((MTApplication) PickFileChooser.this.getRenderer()).getCurrentScene();
						
						IMAGEModel img = new IMAGEModel(file.getAbsolutePath());
						widget = new MTIMAGE(getRenderer(), img, true, workbench,
								workbenchs);
						
						String ref = IDManager.getInstance().nextID(
								workbench.getRow());
						Klonk klonk = new Klonk(new Identification(ref, workbench.getTeamMember()));
						ImageKlonkWidget ikw =  new ImageKlonkWidget(applet,klonk,(MTIMAGE) widget);
						widget.setUserData("klonkwidget",ikw);
						physic = new PhysicWrapper(widget, ((TablePhaseScene)((MTApplication) PickFileChooser.this.applet).getCurrentScene()));
						
						
						//listMenu.setMustBeDestroy(true);
						widget.setWidthXYGlobal(200);
						//if physic component, set the position of the body
						if( physic != null )
						{
							Vector3D scaledPos = PhysicsHelper.scaleDown(listMenu.getCenterPointGlobal());
							physic.getBody().setTransform(new Vec2(scaledPos.x, scaledPos.y),0);
						}
						
					/*}
					
					//this.addToScene();
				}		
			};	*/		
			
		} else if (FileExtensionFilter.PDF_FILTER.accept(file)) {
			PDFModel pdf = new PDFModel(file.getAbsolutePath());
			widget = new MTPDF(getRenderer(), pdf, true, workbench, workbenchs);
			
			if( ((MTApplication) this.applet).getCurrentScene() instanceof TablePhaseScene )
			{
				physic = new PhysicWrapper(widget, ((TablePhaseScene)((MTApplication) this.applet).getCurrentScene()));
			}
			
		} else if (FileExtensionFilter.VIDEO_FILTER.accept(file)) {
			MovieModel movie = new MovieModel(file.getAbsolutePath());
			widget = new MTMOVIE(getRenderer(), movie, workbench, workbenchs);
			
			if( ((MTApplication) this.applet).getCurrentScene() instanceof TablePhaseScene )
			{
				physic = new PhysicWrapper(widget, ((TablePhaseScene)((MTApplication) this.applet).getCurrentScene()));
			}
			
		} else if (FileExtensionFilter.HTML_FILTER.accept(file)) {
			System.out.println("open html");
			BrowserComponent bc = new BrowserComponent(
							(AbstractMTApplication) getRenderer(), "Browser",
							file.getAbsolutePath(), 900, 450, true, workbench);
					bc.setPositionGlobal(listMenu.getCenterPointGlobal());
					final TablePhaseScene scene = (TablePhaseScene) ((MTApplication) PickFileChooser.this.getRenderer()).getCurrentScene();
					scene.getItemLayer().addChild(bc);
			
					
					
			
		}
		else if( FileExtensionFilter.MM_FILTER.accept(file) )
		{
			BrainstormingMMImporter imp = new BrainstormingMMImporter((TablePhaseScene) ((MTApplication)this.applet).getCurrentScene(),workbench);
			imp.importFile(file.getAbsolutePath());
		}
		else if( FileExtensionFilter.TXT_FILTER.accept(file) )
		{
			final BrainstormingTxtImporter imp = new BrainstormingTxtImporter((TablePhaseScene) ((MTApplication)this.applet).getCurrentScene(),workbench);
			OneShotBehaviour beh = new OneShotBehaviour(){						
				@Override
				public void action() {				
					imp.importFile(file.getAbsolutePath());
				}
			};
			this.workbench.getAgent().addBehaviour(beh);
			
		}
		else {
			UnknownFile unknownFile = new UnknownFile(file);
			widget = new MTMetadata(getRenderer(), unknownFile, workbench,
					workbenchs);
		}
		
		//if behaviour is not null create widget from agent thread
		if(behaviour != null)
			this.workbench.getAgent().addBehaviour(behaviour);
		//else add directly from this tread
		else if (widget != null) {
			LocationReference lr = LocationReference.getLocation(workbench.getRow());
			listMenu.getParent().addChild(widget);
			widget.rotateZGlobal(widget.getCenterPointGlobal(), lr.getDirection());
			widget.setAnchor(PositionAnchor.CENTER);
			widget.setPositionGlobal(listMenu.getCenterPointGlobal());
			widget.setAnchor(PositionAnchor.UPPER_LEFT);		
			
			widget.setWidthXYGlobal(200);
			//if physic component, set the position of the body
			if( physic != null )
			{
				Vector3D scaledPos = PhysicsHelper.scaleDown(listMenu.getCenterPointGlobal());
				physic.getBody().setTransform(new Vec2(scaledPos.x, scaledPos.y),0);
			}
		}
		
		listMenu.setMustBeDestroy(true);
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
				if (tapEvent.isTapped() && currentMenu != null
						&& currentMenu instanceof File) {

					if (tapEvent.getTarget() == pdfButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.PDF_FILTER));
					} else if (tapEvent.getTarget() == movieButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.VIDEO_FILTER));
					} else if (tapEvent.getTarget() == imageButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.IMG_FILTER));
					} else if (tapEvent.getTarget() == htmlButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.HTML_FILTER));
					} else if (tapEvent.getTarget() == noFilterButton) {
						PickFileChooser.this.setModel(new FileChooserModel(
								(File) currentMenu,
								FileExtensionFilter.NO_FILTER));
					}
				}
			}

			return false;
		}
	}
	
	/**
	 * create the widget on agent thread and add then to the scene with an IPreDrawAction
	 */
	private abstract class LoadOnAgent extends  OneShotBehaviour
	{
		MTRectangle widget;
		PhysicWrapper physic;
		Vector3D globalPos;
		
		public LoadOnAgent(MTRectangle widget,PhysicWrapper physic,Vector3D globalPos) {
			this.widget = widget;
			this.physic = physic;
			this.globalPos = globalPos;
		}

		protected void addToScene()
		{
			try{				
			
			System.out.println("add to scene");
			final TablePhaseScene scene = (TablePhaseScene) ((MTApplication) PickFileChooser.this.getRenderer()).getCurrentScene();
			scene.registerPreDrawAction( new IPreDrawAction(){

				@Override
				public boolean isLoop() {
					return false;
				}

				@Override
				public void processAction() {
					

					if (widget != null) {
						LocationReference lr = LocationReference.getLocation(workbench.getRow());
						scene.getItemLayer().addChild(widget);
						widget.rotateZGlobal(widget.getCenterPointGlobal(), lr.getDirection());
						widget.setAnchor(PositionAnchor.CENTER);
						widget.setPositionGlobal(globalPos);
						widget.setAnchor(PositionAnchor.UPPER_LEFT);
						//widget.getTexture().u
						
						//listMenu.setMustBeDestroy(true);
						widget.setWidthXYGlobal(200);
						//if physic component, set the position of the body
						if( physic != null )
						{
							Vector3D scaledPos = PhysicsHelper.scaleDown(globalPos);
							physic.getBody().setTransform(new Vec2(scaledPos.x, scaledPos.y),0);
						}
					}				
				}			
			});
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
