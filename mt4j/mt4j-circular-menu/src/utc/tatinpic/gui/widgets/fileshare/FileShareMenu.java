package utc.tatinpic.gui.widgets.fileshare;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

import utc.tatinpic.ProjectBoot;
import utc.tatinpic.WhiteboardBoot;
import utc.tatinpic.gui.phase.TatinScene;
import utc.tatinpic.gui.widgets.menu.circularmenu.WorkbenchHandler;
import utc.tatinpic.model.BoardProject;
import utc.tatinpic.model.Workbench;

// position must be set after creation, since it depends on who opens Menu
public class FileShareMenu extends MTRectangle {

	private static final float TICK_BOX_SIZE = 18f;
	private static final int FONT_SIZE = 16;
	private static final int X_INNER_PADDING = 5;
	private static final int Y_INNER_PADDING = 5;
	private static final int FILE_SHARE_WIDTH = 100;
	private static final float BUTTON_HEIGHT = 30;
	private static final MTColor COLOR_ALREADY_SHARED = MTColor.GREEN;
	private static final MTColor COLOR_NOT_SHARED = MTColor.BLACK;
	private static final MTColor COLOR_SHARED = MTColor.BLUE;
	private File file;

	private List<String> alreadyHaveFileMembers = new ArrayList<String>();
	private List<String> shareWithMembers = new ArrayList<String>();
	private Map<String, MTRectangle> membersTickBox = new HashMap<String, MTRectangle>();
	private MTTextArea closeButton;
	private MTTextArea validateButton;

	public FileShareMenu(PApplet applet, Workbench workbench,
			List<Workbench> workbenchs, File file) {
		super(applet, 0, 0);
		this.file = file;
		removeAllGestureEventListeners();
		setAnchor(PositionAnchor.UPPER_LEFT);
		setFillColor(new MTColor(255, 255, 255, 150));
		setStrokeColor(MTColor.WHITE);

		addCurrentTeamMemberName(workbench);
		addOtherTeamMembers(workbench, workbenchs);
		createCloseButton(workbenchs);
		createValidateButton(workbenchs);
		closeButton.setVisible(true);
		closeButton.setEnabled(true);
		setSizeLocal(FILE_SHARE_WIDTH,
				Y_INNER_PADDING * 3 + 30f * workbenchs.size() + BUTTON_HEIGHT);
		FileShareMenuManager.add(this);
	}
	
	public FileShareMenu(PApplet applet, Workbench workbench, List<Workbench> workbenches, ArrayList<File> listFiles)
	{
		super(applet, 0, 0);
		
		removeAllGestureEventListeners();
		setAnchor(PositionAnchor.UPPER_LEFT);
		setFillColor(new MTColor(255, 255, 255, 150));
		setStrokeColor(MTColor.WHITE);
		setSizeLocal(FILE_SHARE_WIDTH * 3,
				Y_INNER_PADDING * 3 + 30f * 6 + BUTTON_HEIGHT);
		
		FileShareMenuManager.add(this);
	}

	private void addCurrentTeamMemberName(Workbench workbench) {
		MTTextArea teamMemberName = new MTTextArea(getRenderer(), FontManager
				.getInstance().createFont(getRenderer(), "calibri", FONT_SIZE,
						MTColor.WHITE, true));
		teamMemberName.setNoFill(true);
		teamMemberName.setNoStroke(true);
		teamMemberName.setAnchor(PositionAnchor.UPPER_LEFT);
		teamMemberName.setText(workbench.getTeamMember());
		addChild(teamMemberName);
		teamMemberName.setPositionRelativeToParent(new Vector3D(
				X_INNER_PADDING, Y_INNER_PADDING));
		teamMemberName.removeAllGestureEventListeners();
	}

	private void addOtherTeamMembers(Workbench workbench,
			List<Workbench> workbenchs) {

		float y = 35f;
		for (Workbench w : workbenchs) {
			if (!w.getTeamMember().equals(workbench.getTeamMember())) {
				if (FileShareHelper.hasFile(file, w.getTeamMember())) {
					alreadyHaveFileMembers.add(w.getTeamMember());
				}
				addShareLine(w.getTeamMember(), y);
				y += 30f;
			}
		}
	}

	private void addShareLine(String teamMember, float yPos) {
		TickFileListener listener = new TickFileListener(teamMember);
		addTickBox(teamMember, yPos, listener);
		addOtherTeamMemberName(teamMember, yPos, listener);
	}

	private void addOtherTeamMemberName(String teamMember, float yPos,
			TickFileListener listener) {
		MTTextArea teamMemberName = new MTTextArea(getRenderer(), FontManager
				.getInstance().createFont(getRenderer(), "calibri", FONT_SIZE,
						MTColor.BLACK, true));
		teamMemberName.setNoFill(true);
		teamMemberName.setNoStroke(true);
		teamMemberName.setAnchor(PositionAnchor.UPPER_LEFT);
		teamMemberName.setText(teamMember);
		addChild(teamMemberName);
		teamMemberName.setPositionRelativeToParent(new Vector3D(X_INNER_PADDING
				* 2f + TICK_BOX_SIZE, yPos));
		teamMemberName.removeAllGestureEventListeners();
		teamMemberName.registerInputProcessor(new TapProcessor(getRenderer()));
		teamMemberName.addGestureListener(TapProcessor.class, listener);
	}

	private void addTickBox(String teamMember, float yPos,
			TickFileListener listener) {
		MTRectangle tickBox = new MTRectangle(getRenderer(), TICK_BOX_SIZE,
				TICK_BOX_SIZE);
		tickBox.setName(teamMember);
		tickBox.setAnchor(PositionAnchor.UPPER_LEFT);
		tickBox.setStrokeColor(MTColor.BLACK);
		tickBox.setFillColor(alreadyHaveFileMembers.contains(teamMember) ? COLOR_ALREADY_SHARED
				: COLOR_NOT_SHARED);
		addChild(tickBox);
		tickBox.setPositionRelativeToParent(new Vector3D(X_INNER_PADDING, yPos
				+ Y_INNER_PADDING));
		tickBox.removeAllGestureEventListeners();
		tickBox.registerInputProcessor(new TapProcessor(getRenderer()));
		tickBox.addGestureListener(TapProcessor.class, listener);
		membersTickBox.put(teamMember, tickBox);
	}

	private void createCloseButton(List<Workbench> workbenchs) {
		closeButton = new MTTextArea(getRenderer(), FontManager.getInstance()
				.createFont(getRenderer(), "calibri", FONT_SIZE, MTColor.WHITE,
						true));
		closeButton.setAnchor(PositionAnchor.UPPER_LEFT);
		closeButton.setFillColor(MTColor.RED);
		closeButton.setText(" FERMER ");
		addChild(closeButton);
		closeButton.setPositionRelativeToParent(new Vector3D(X_INNER_PADDING,
				Y_INNER_PADDING * 2 + 30f * workbenchs.size()));
		closeButton.removeAllGestureEventListeners();
		closeButton.registerInputProcessor(new TapProcessor(getRenderer()));
		closeButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						close();
						return false;
					}
				});
		closeButton.setVisible(false);
		closeButton.setEnabled(false);
	}

	private void createValidateButton(List<Workbench> workbenchs) {
		validateButton = new MTTextArea(getRenderer(), FontManager
				.getInstance().createFont(getRenderer(), "calibri", FONT_SIZE,
						MTColor.WHITE, true));
		validateButton.setAnchor(PositionAnchor.UPPER_LEFT);
		validateButton.setFillColor(MTColor.GREEN);
		validateButton.setText("PARTAGER");
		addChild(validateButton);
		validateButton
				.setPositionRelativeToParent(new Vector3D(X_INNER_PADDING,
						Y_INNER_PADDING * 2 + 30f * workbenchs.size()));
		validateButton.removeAllGestureEventListeners();
		validateButton.registerInputProcessor(new TapProcessor(getRenderer()));
		validateButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						shareFile();
						validateButton.setEnabled(false); // avoid double tap
						return false;
					}
				});
		validateButton.setVisible(false);
		validateButton.setEnabled(false);
	}

	private class TickFileListener implements IGestureEventListener {

		private String teamMember;

		public TickFileListener(String teamMember) {
			this.teamMember = teamMember;
		}

		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			if (ge instanceof TapEvent && ((TapEvent) ge).isTapped()) {
				if (!alreadyHaveFileMembers.contains(teamMember)) {
					if (shareWithMembers.contains(teamMember)) {
						shareWithMembers.remove(teamMember);
						membersTickBox.get(teamMember).setFillColor(
								COLOR_NOT_SHARED);
					} else {
						shareWithMembers.add(teamMember);
						membersTickBox.get(teamMember).setFillColor(
								COLOR_SHARED);
					}
					updateCloseValidateButton();
				}
			}
			return false;
		}
	}

	public void updateCloseValidateButton() {
		if (shareWithMembers.isEmpty()) {
			closeButton.setVisible(true);
			closeButton.setEnabled(true);
			validateButton.setVisible(false);
			validateButton.setEnabled(false);
		} else {
			closeButton.setVisible(false);
			closeButton.setEnabled(false);
			validateButton.setVisible(true);
			validateButton.setEnabled(true);
		}
	}

	protected void shareFile() {
		WorkbenchHandler wbH[] = null;
		TatinScene scene = null;

		if (ProjectBoot.getInstance() != null
				&& ProjectBoot.getInstance().getCurrentScene() != null
				&& ProjectBoot.getInstance().getCurrentScene() instanceof TatinScene) {
			scene = (TatinScene) ProjectBoot.getInstance().getCurrentScene();
		} else if (WhiteboardBoot.getInstance() != null
				&& WhiteboardBoot.getInstance().getCurrentScene() != null
				&& WhiteboardBoot.getInstance().getCurrentScene() instanceof TatinScene) {
			scene = (TatinScene) WhiteboardBoot.getInstance().getCurrentScene();
		}
		if (scene != null) {
			wbH = scene.getMenuHandlers();
		}
		for (String member : shareWithMembers) {
			// copy the file
			FileShareHelper.copy(file, member);
			// inform receiver of the file
			if (wbH != null && wbH.length > 0)
			for (WorkbenchHandler handler : wbH) {
				if (handler.getWorkbench().getTeamMember().equals(member)) {
					handler.displayUserInfo(file.getName() + " a été ajouté à votre dropbox");
				}
			}
			
		}
		FileShareMenuManager.onShare(FileShareMenu.this, shareWithMembers);
		close();
	}

	private void close() {
		IAnimation closeAnimation = new Animation("FileShareMenuClose",
				new MultiPurposeInterpolator(1, 0, 500, 0.9f, 0.1f, 1), this);
		closeAnimation.addAnimationListener(new IAnimationListener() {
			public void processAnimationEvent(AnimationEvent ae) {
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
					scaleGlobal(currentVal, currentVal, 0,
							getCenterPointGlobal());
					break;
				case AnimationEvent.ANIMATION_ENDED:
					setVisible(false);
					destroy();
					break;
				default:
					break;
				}
			}
		});
		closeAnimation.start();
	}

	@Override
	public void destroy() {
		FileShareMenuManager.remove(this);
		super.destroy();
	}

	protected File getFile() {
		return file;
	}

	public void setAlreadySharedWith(List<String> alreadyHaveFileMembers) {
		this.alreadyHaveFileMembers.clear();
		this.alreadyHaveFileMembers.addAll(alreadyHaveFileMembers);

		for (String teamMember : this.alreadyHaveFileMembers) {
			shareWithMembers.remove(teamMember);
			MTRectangle component = (MTRectangle) getChildByName(teamMember);
			if (component != null) {
				component.setFillColor(COLOR_ALREADY_SHARED);
			}
		}
		updateCloseValidateButton();
	}
}
