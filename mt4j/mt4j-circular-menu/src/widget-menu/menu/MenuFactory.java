package utc.tatinpic.gui.widgets.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import utc.tatinpic.ProjectBoot;
import utc.tatinpic.WhiteboardBoot;
import utc.tatinpic.gui.LocationReference;
import utc.tatinpic.gui.widgets.menu.circularmenu.TatinArcCircularMenu;
import utc.tatinpic.gui.widgets.menu.circularmenu.TatinCircularMenuSegmentHandle;
import utc.tatinpic.gui.widgets.menu.circularmenu.WorkbenchHandler;
import utc.tatinpic.model.Workbench;
import utc.tatinpic.model.klonk.Klonk;
import utc.tatinpic.model.menu.DefaultMenuModel;
import utc.tatinpic.model.menu.MenuItem;
import utc.tatinpic.model.phase.Phase;
import utc.tatinpic.model.phase.PhaseList;
import utc.tatinpic.semantics.Constants;
import utc.tatinpic.util.manager.PropertyManager;
import utc.tatinpic.util.manager.language.ResourceBundleManager;

public class MenuFactory {
	private static String BS_PHASE = "bs-phase";
	private static String PLANNING_PHASE = "planning";
	private static String LINKED_DATA_PHASE = "linked-data";
	private static String CAUSAL_ANALYSIS_PHASE = "causal-analysis";
	private static String GENERAL = "general";
	private static String TASK = "task";
	public static String RISK = "risk";
	private static String PAPER_BOARD_PHASE = "paperboard";
	
	public static String SEPARATOR = "#";
	public static String PHASE_CREATION = "CREATE";
	public static String BS_CREATION = "CREATE_BS";
	public static String PHASE_CHOISE = "CHOICE";

	private static String PROJECT = "project";
	private static String TABLE_AGENT = "table-agent";
	public static String PHASE = "phase";
	public static String BRAINSTORMING_PDF = "bs-pdf";
	public static String BRAINSTORMING_MM = "bs-mm";
	public static String BRAINSTORMING_RA = "bs-ra";
	public static String BRAINSTORMING_MODEL = "bs-model";
	public static String CREATE = "create";

	public static String MIN_START_DATE = "min-start-date";
	public static String MAX_START_DATE = "max-start-date";

	public static String MIN_END_DATE = "min-end-date";
	public static String MAX_END_DATE = "max-end-date";

	public static String INIT_DATE = "init-date";
	public static String END_DATE = "end-date";
	public static String DURATION = "duration";
	public static String FREE_FLOAT = "free-float";
	public static String TOTAL_FLOAT = "total-float";

	//private static String CREATE_PHASE = "create-phase";
	//private static String CHOOSE_PHASE = "choose-phase";
	private static String MANAGE_TABLE_PHASE = "table-phase";
	private static String SELECT_CREATE_PHASE = "phase";
	
	//public static final int CA_PHASE_ROW = 2;

	public static final int CREATE_GEN_BS_PHASE = 11;
	public static final int CREATE_TASK_BS_PHASE = 12;
	public static final int CREATE_RISK_BS_PHASE = 13;
	public static final int CREATE_PLANNING_PHASE = 14;
	public static final int CREATE_LINKED_DATA_PHASE = 15;
	public static final int CREATE_CAUSAL_ANALYSIS_PHASE = 16;
	public static final int CREATE_PAPER_BOARD_PHASE = 17;
	
	public static final int BS_PHASE_CHOICE = 10;
	public static final int GEN_BS_PHASE_CHOICE = 110;
	public static final int TASK_BS_PHASE_CHOICE = 120;
	public static final int RISK_BS_PHASE_CHOICE = 130;
	public static final int PLANNING_PHASE_CHOICE = 30;
	public static final int LINKED_DATA_PHASE_CHOICE = 40;
	public static final int CAUSAL_ANALYSIS_PHASE_CHOICE = 50;
	public static final int PROJECT_CHOICE = 140;
	public static final int PAPER_BOARD_CHOICE = 150;

	private static int DELAY = 500;
	private static String TATIN = "Tatin";
	private static MTColor[] segmentColors = new MTColor[] {
			new MTColor(Constants.BOOT_SEGMENT_COLORS[0][0],
					Constants.BOOT_SEGMENT_COLORS[0][1],
					Constants.BOOT_SEGMENT_COLORS[0][2],
					Constants.BOOT_SEGMENT_COLORS[0][3]),
			new MTColor(Constants.BOOT_SEGMENT_COLORS[1][0],
					Constants.BOOT_SEGMENT_COLORS[1][1],
					Constants.BOOT_SEGMENT_COLORS[1][2],
					Constants.BOOT_SEGMENT_COLORS[1][3]),
			new MTColor(Constants.BOOT_SEGMENT_COLORS[2][0],
					Constants.BOOT_SEGMENT_COLORS[2][1],
					Constants.BOOT_SEGMENT_COLORS[2][2],
					Constants.BOOT_SEGMENT_COLORS[2][3]),
			new MTColor(Constants.BOOT_SEGMENT_COLORS[3][0],
					Constants.BOOT_SEGMENT_COLORS[3][1],
					Constants.BOOT_SEGMENT_COLORS[3][2],
					Constants.BOOT_SEGMENT_COLORS[3][3]),
			new MTColor(Constants.BOOT_SEGMENT_COLORS[4][0],
					Constants.BOOT_SEGMENT_COLORS[4][1],
					Constants.BOOT_SEGMENT_COLORS[4][2],
					Constants.BOOT_SEGMENT_COLORS[4][3]) };

	// =================== Menus on table ===================================

	/**
	 * Create a ListMenu for choosing, creating a phase on the table
	 */
	public static ListMenu createTablePhaseMenu(
			final AbstractMTApplication app, final TatinArcCircularMenu menu) {
		final ProjectBoot application = (ProjectBoot) app;
		ListMenu phaseMenu;
		final ResourceBundleManager rbm = ResourceBundleManager.getInstance();
		
		final DefaultMenuModel firstleveldmm = new DefaultMenuModel();
		
		
		final DefaultMenuModel bsmodel = new DefaultMenuModel(
				rbm.getString(BS_PHASE));

		
		DefaultMenuModel generalBSmodel = createBSmodelMenu(GENERAL,Klonk.Category.IDEA_NOTE,app);
		
		DefaultMenuModel taskBSmodel = createBSmodelMenu(TASK,Klonk.Category.TASK,app);
		
		DefaultMenuModel riskBSmodel = createBSmodelMenu(RISK,Klonk.Category.RISK,app);

		bsmodel.add(generalBSmodel, taskBSmodel, riskBSmodel);
		firstleveldmm.add(bsmodel);
		
	firstleveldmm.add(createModelMenu(PLANNING_PHASE,
			Phase.Category.PLANNING,app));
	firstleveldmm.add(createModelMenu(LINKED_DATA_PHASE,
			Phase.Category.RISK_ANALYSIS,app));
	firstleveldmm.add(createModelMenu(CAUSAL_ANALYSIS_PHASE,
			Phase.Category.CAUSAL_ANALYSIS,app));
	firstleveldmm.add(createModelMenu(PAPER_BOARD_PHASE,
			Phase.Category.PAPER_BOARD,app));
	firstleveldmm.add(createModelMenu("flowchart",
			Phase.Category.FLOWCHART,app));
	
	phaseMenu = new ListMenu(app, 0, 0, 200.f, 4, firstleveldmm);
	final IBoundingShape bounds = phaseMenu.getBounds();
	phaseMenu.transform(menu.getGlobalMatrix());
	phaseMenu.setPositionRelativeToOther(menu,
			new Vector3D(bounds.getWidthXY(TransformSpace.GLOBAL) / 2,
					-bounds.getHeightXY(TransformSpace.GLOBAL) / 2));
	phaseMenu.setCloseVisible(true);
	phaseMenu.setMustBeDestroy(false);
	return phaseMenu;
	}
	
	public static DefaultMenuModel createBSmodelMenu(String n,Klonk.Category category,AbstractMTApplication app)
	{
		ResourceBundleManager rbm = ResourceBundleManager.getInstance();
		
		DefaultMenuModel model = new DefaultMenuModel(rbm.getString(n));
		final MenuItem item13 = new MenuItem(0,rbm.getString(CREATE),BS_CREATION+SEPARATOR+category);
		model.add(item13);
		for (String s : ((ProjectBoot) app).getBrainstormingPhaseNames(category)) {
			String name = Constants.shortPhaseName(s);
			MenuItem item = new MenuItem(0,name,PHASE_CHOISE+SEPARATOR+Phase.Category.BRAINSTORMING);
			model.add(item);
		}
		
		return model;
	}
	
	public static DefaultMenuModel createModelMenu(String n,Phase.Category category,AbstractMTApplication app)
	{
		ResourceBundleManager rbm = ResourceBundleManager.getInstance();
		
		final DefaultMenuModel model = new DefaultMenuModel(rbm.getString(n));
		final MenuItem item41 = new MenuItem(0,
				rbm.getString(CREATE),PHASE_CREATION+SEPARATOR+category);
		model.add(item41);
		final ArrayList<String> causalanalysis = ((ProjectBoot) app).getPhaseNames(category);
		for (String s : causalanalysis) {
			model.add(new MenuItem(0,s,PHASE_CHOISE+SEPARATOR+category));
		}
		
		return model;
	}
	
	// =================== Menus for planning on board ===================================
	/**
	 * Create a ListMenu for choosing a phase in order to import its model
	 */
	public static ListMenu createBoardPlanningImportPhaseMenu(
			final AbstractMTApplication app, 
			final TatinArcCircularMenu menu,
			PhaseList phlist) {
		ListMenu phaseMenu;
		final ResourceBundleManager rbm = ResourceBundleManager.getInstance();
		
		final DefaultMenuModel firstleveldmm = new DefaultMenuModel();
		final DefaultMenuModel bsmodel = new DefaultMenuModel(
				rbm.getString(BS_PHASE));

		final ArrayList<String> bstorming = phlist.getNames(Phase.Category.BRAINSTORMING);
		if (!bstorming.isEmpty()) {
			for (String s : bstorming) {
			String name = Constants.shortPhaseName(s);
			MenuItem item = new MenuItem(BS_PHASE_CHOICE,name);
			bsmodel.add(item);
		    }
			firstleveldmm.add(bsmodel);
		}
		final DefaultMenuModel planningModel = new DefaultMenuModel(
				rbm.getString(PLANNING_PHASE));
	
		final ArrayList<String> planning = phlist.getNames(Phase.Category.PLANNING);
	
	if (!planning.isEmpty()) {
		
		for (final String s : planning) {
			planningModel.add(new MenuItem(PLANNING_PHASE_CHOICE, s));
		}
		firstleveldmm.add(planningModel);
	}
	phaseMenu = new ListMenu(app, 0, 0, 200.f, 2, firstleveldmm);
	final IBoundingShape bounds = phaseMenu.getBounds();
	phaseMenu.transform(menu.getGlobalMatrix());
	phaseMenu.setPositionRelativeToOther(menu,
			new Vector3D(bounds.getWidthXY(TransformSpace.GLOBAL) / 2,
					-bounds.getHeightXY(TransformSpace.GLOBAL) / 2));
	phaseMenu.setCloseVisible(true);
	phaseMenu.setMustBeDestroy(false);
	return phaseMenu;
	}
	
	// =================== Menus for Causal Analysis on board ===================================
	/**
	 * Create a ListMenu for choosing a phase in order to import its model
	 */
	public static ListMenu createBoardCausalImportPhaseMenu(
			final AbstractMTApplication app, 
			final TatinArcCircularMenu menu,
			PhaseList phlist) {
		ListMenu phaseMenu;
		final ResourceBundleManager rbm = ResourceBundleManager.getInstance();
		
		final DefaultMenuModel firstleveldmm = new DefaultMenuModel();
		final DefaultMenuModel bsmodel = new DefaultMenuModel(
				rbm.getString(BS_PHASE));

		final ArrayList<String> bstorming = phlist.getNames(Phase.Category.BRAINSTORMING);
		if (!bstorming.isEmpty()) {
			for (String s : bstorming) {
			String name = Constants.shortPhaseName(s);
			MenuItem item = new MenuItem(BS_PHASE_CHOICE,name);
			bsmodel.add(item);
		    }
			firstleveldmm.add(bsmodel);
		}
	phaseMenu = new ListMenu(app, 0, 0, 200.f, 1, firstleveldmm);
	final IBoundingShape bounds = phaseMenu.getBounds();
	phaseMenu.transform(menu.getGlobalMatrix());
	phaseMenu.setPositionRelativeToOther(menu,
			new Vector3D(bounds.getWidthXY(TransformSpace.GLOBAL) / 2,
					-bounds.getHeightXY(TransformSpace.GLOBAL) / 2));
	phaseMenu.setCloseVisible(true);
	phaseMenu.setMustBeDestroy(false);
	return phaseMenu;
	}
	
	
	
	/**
	 * Create a ListMenu for choosing a project on the table
	 */
	public static ListMenu createProjectMenu(
			final AbstractMTApplication app, final WorkbenchHandler menu,
			ArrayList<String> projectList) {
		final ProjectBoot application = (ProjectBoot) app;
		final DefaultMenuModel pmodel = new DefaultMenuModel();
		for (String s : projectList) {
			MenuItem item = new MenuItem(PROJECT_CHOICE,s);
			pmodel.add(item);
		}
		ListMenu projectMenu = new ListMenu(app, 0, 0, 200.f, 5, pmodel);
		final IBoundingShape bounds = projectMenu.getBounds();
		projectMenu.transform(menu.getGlobalMatrix());
		projectMenu.setPositionRelativeToOther(menu,
				new Vector3D(bounds.getWidthXY(TransformSpace.GLOBAL) / 2,
						-bounds.getHeightXY(TransformSpace.GLOBAL) / 2));
		projectMenu.setCloseVisible(true);
		projectMenu.setMustBeDestroy(false);
		
		return projectMenu;
	}
	
	/**
	 * Create a ListMenu for choosing an agent action on the table The final
	 * result is sent back to the whiteboard agent
	 */
	public static ListMenu createTableAgentMenu(
			final AbstractMTApplication app, final WorkbenchHandler menu) {
		ListMenu btMenu;
		final ResourceBundleManager rbm = ResourceBundleManager.getInstance();
		final MenuItem item1 = new MenuItem(1, rbm.getString(BRAINSTORMING_PDF));
		final MenuItem item2 = new MenuItem(2, rbm.getString(BRAINSTORMING_MM));
		final MenuItem item3 = new MenuItem(3, rbm.getString(BRAINSTORMING_RA));
		final MenuItem item4 = new MenuItem(4,
				rbm.getString(BRAINSTORMING_MODEL));
		btMenu = new ListMenu(app, 0, 0, 300.f, 4, item1, item2, item3, item4);
		final IBoundingShape bounds = btMenu.getBounds();
		btMenu.transform(menu.getGlobalMatrix());
		btMenu.setPositionRelativeToOther(menu,
				new Vector3D(bounds.getWidthXY(TransformSpace.GLOBAL) / 2,
						-bounds.getHeightXY(TransformSpace.GLOBAL) / 2));
		btMenu.setCloseVisible(true);
		btMenu.setMustBeDestroy(false);
		return btMenu;
	}

	/*
	 * Create a WorkbenchHandler (for the table) that allows: Changing phase
	 * sending an element to the board (Starting the JADE Agent platform but
	 * should be done from the register action
	 */
	public static WorkbenchHandler createTableProjectMenu(
			final AbstractMTApplication app) {
		final PropertyManager pm = PropertyManager.getInstance();
		final ResourceBundleManager rbm = ResourceBundleManager.getInstance();
		TatinCircularMenuSegmentHandle segment;
		final WorkbenchHandler projectMenu = new WorkbenchHandler(app,
				pm.getIntProperty(PropertyManager.CIRCULAR_MENU_INNERRADIUS),
				pm.getIntProperty(PropertyManager.CIRCULAR_MENU_OUTERRADIUS),
				rbm.getString(PROJECT),true);
		projectMenu.setWorkbench(new Workbench("Moderator", 0));
		segment = projectMenu.createSegment(rbm.getString(PHASE));
		segment.setFillColor(new MTColor(127, 0, 0, 255 - 16));
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				projectMenu.getPhaseMenu().setVisible(true);
			}
		});

		segment = projectMenu.createSegment(app.loadImage(pm
				.getProperty(PropertyManager.IMAGE_DIR_EXT) + "config.png"));
		segment.setFillColor(new MTColor(255, 127, 0, 255 - 16));
		segment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				projectMenu.setVisible(false);
				projectMenu.getBackMenu().setVisible(true);
			}
		});
		projectMenu.setPositionGlobal(LocationReference.SOUTH.getPosition());
		return projectMenu;
	}
}
