package utc.tatinpic.gui.widgets.fileshare;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
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
import utc.tatinpic.gui.widgets.menu.FileChooser;
import utc.tatinpic.model.Workbench;

public class MultiFileDeleteMenu extends MTRectangle 
{
	private static final int FONT_SIZE = 16;
	private ArrayList<File> files;
	
	private FileChooser fileChooser;
	
	private MTTextArea deleteConfirmText;
	private MTTextArea cancelDeleteButton;
	private MTTextArea confirmDeleteButton;
	
	public MultiFileDeleteMenu(PApplet applet, Workbench workbench,
			List<Workbench> workbenchs, ArrayList<File> files, FileChooser fc) {
		super(applet, 0, 0);
		removeAllGestureEventListeners();
		setAnchor(PositionAnchor.UPPER_LEFT);
		translate(new Vector3D(300, 0));
		setNoFill(false);
		setNoStroke(false);
		//setFillColor(new MTColor(255, 255, 255, 150));
		setFillColor(MTColor.BLACK);
		setStrokeColor(MTColor.WHITE);
		
		this.files = files;
		this.fileChooser = fc;
		
		createDeleteConfirmText();
		createCancelDeleteButton(workbenchs);
		createConfirmDeleteButton(workbenchs);
	}
	
	private void createCancelDeleteButton(List<Workbench> workbenchs) {
		cancelDeleteButton = new MTTextArea(getRenderer(), FontManager
				.getInstance().createFont(getRenderer(), "calibri", FONT_SIZE,
						MTColor.WHITE, true));
		cancelDeleteButton.setAnchor(PositionAnchor.LOWER_LEFT);
		cancelDeleteButton.setFillColor(MTColor.BLACK);
		cancelDeleteButton.setText("Annuler");
		cancelDeleteButton.setFontColor(MTColor.WHITE);
		cancelDeleteButton.translate(new Vector3D(0, 50));	
		cancelDeleteButton.removeAllGestureEventListeners();
		
		cancelDeleteButton.registerInputProcessor(new TapProcessor(getRenderer()));
		cancelDeleteButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						close();
						return false;
					}
				});
		
		addChild(cancelDeleteButton);
	}
	
	private void createConfirmDeleteButton(List<Workbench> workbenchs) {
		confirmDeleteButton = new MTTextArea(getRenderer(), FontManager
				.getInstance().createFont(getRenderer(), "calibri", FONT_SIZE,
						MTColor.WHITE, true));
		confirmDeleteButton.setAnchor(PositionAnchor.LOWER_LEFT);
		confirmDeleteButton.setFillColor(MTColor.RED);
		confirmDeleteButton.setText("Supprimer");
		confirmDeleteButton.removeAllGestureEventListeners();
		confirmDeleteButton.translate(new Vector3D(0, 100));
		
		confirmDeleteButton.registerInputProcessor(new TapProcessor(getRenderer()));
		confirmDeleteButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {

					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						delete();
						return false;
					}
				});
		
		addChild(confirmDeleteButton);
	}
	
	private void createDeleteConfirmText() {
		deleteConfirmText = new MTTextArea(getRenderer(), FontManager
				.getInstance().createFont(getRenderer(), "calibri", FONT_SIZE,
						MTColor.WHITE, true));	
		deleteConfirmText.removeAllGestureEventListeners();
		deleteConfirmText.setNoFill(true);
		deleteConfirmText.setNoStroke(true);
		deleteConfirmText.setText("Supprimer ?");
		
		addChild(deleteConfirmText);
	}
	
	public void close() {
		IAnimation closeAnimation = new Animation("FileDeleteMenuClose",
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
	
	private void delete() {
		for (File f : files) {
			f.delete();
		}
		
		close();
		this.fileChooser.updateList();
	}
}
