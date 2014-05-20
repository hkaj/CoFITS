package utc.bsfile.gui.widget.movie;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import org.apache.commons.lang.text.StrBuilder;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.util.animation.AnimationUtil;

import codeanticode.gsvideo.GSMovie;

import processing.core.PApplet;

import utc.bsfile.model.movie.MovieListener;
import utc.bsfile.model.movie.MovieModel;
import utc.bsfile.util.ImageManager;

public class MTMOVIE extends MTRectangle implements MovieListener {
	private static final float MOVIE_PLAYER_HEIGHT = 90f;
	private static final float MOVIE_PLAYER_WIDTH = 160f;
	private static final float GESTURE_AREA_HEIGHT = 100f;
	private static final float TIME_SLIDER_HEIGHT = 30f;
	float gestureAreaXPadding = 10;
	float gestureAreaYPadding = 10;
	private static final int IFPS = 30;
	private static final float BUTTON_SIZE = 40f;
	private static final float VOLUME_SLIDER_WIDTH = 40;
	private static final int VOLUME_SLIDER_HEIGHT = 40;
	private static final float GESTURE_AREA_MAX_WIDTH = 500f;
	/**
	 * @uml.property name="pdf"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MovieModel movieModel;

	/**
	 * @uml.property name="gestureArea"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	protected MTRectangle gestureArea;

	/**
	 * @uml.property name="menu"
	 * @uml.associationEnd 
	 *                     inverse="mtpdf:utc.tatinpic.gui.widgets.movie.MOVIEMenu"
	 */
	private MovieMenu menu = null;

	protected MovieView movieView;
	private MTSlider timeSlider;
	private boolean stopSliderAdvance = false;
	private MTSlider volumeSlider;
	private MTRectangle view;
	private boolean isSlave = false;
	private MTEllipse pauseButton;
	private MTEllipse playButton;
	private MTTextArea timeTextArea;

	private MTTextArea fileName;

	public MTMOVIE(PApplet pApplet, MovieModel movieFile) {
		super(pApplet, MOVIE_PLAYER_WIDTH, MOVIE_PLAYER_HEIGHT);
		this.movieModel = movieFile;

		this.setNoFill(true);
		this.setNoStroke(true);

		buildMovieViewer();

		setGestureClassic();
		createGestureArea();
	}

	public MTMOVIE(MTMOVIE master) {
		super(master.getRenderer(), master.getWidthXY(TransformSpace.LOCAL),
				master.getHeightXY(TransformSpace.LOCAL));
		this.movieModel = master.movieModel;
		this.setNoFill(true);
		this.setNoStroke(true);
		buildCloneView(master.movieView);

		setGestureClassic();
		addCloneFileName();

	}

	private void addCloneFileName() {
		MTTextArea fileName = new MTTextArea(getRenderer(), FontManager
				.getInstance().createFont(getRenderer(), "calibri", 16,
						MTColor.WHITE, true));
		fileName.setAnchor(PositionAnchor.CENTER);
		fileName.removeAllGestureEventListeners();
		fileName.setText(movieModel.getFile().getName());
		fileName.setFillColor(new MTColor(128, 128, 128, 50));
		fileName.setStrokeColor(new MTColor(100, 100, 100, 75));
		addChild(fileName);
		fileName.setPositionRelativeToParent(new Vector3D(view
				.getWidthXY(TransformSpace.LOCAL) / 2f, view
				.getHeightXY(TransformSpace.LOCAL)
				+ (fileName.getHeightXY(TransformSpace.LOCAL) / 2f)));
		delegateProcessor(fileName, MTMOVIE.this, DragProcessor.class);
	}

	private void buildCloneView(MovieView movieView) {
		view = new MTRectangle(getRenderer(),
				movieView.getWidthXY(TransformSpace.LOCAL),
				movieView.getHeightXY(TransformSpace.LOCAL));
		view.setTexture(movieView.getTexture());

		this.addChild(view);
		view.removeAllGestureEventListeners();
		delegateProcessor(view, MTMOVIE.this, DragProcessor.class);
		delegateProcessor(view, MTMOVIE.this, ScaleProcessor.class);
		delegateProcessor(view, MTMOVIE.this, RotateProcessor.class);

	}

	private void buildMovieViewer() {
		if (this.movieModel.isMovieValid()) {
			movieView = new MovieView(movieModel.getFile().getAbsolutePath(),
					IFPS, getRenderer(), MOVIE_PLAYER_WIDTH,
					MOVIE_PLAYER_HEIGHT);

			movieView.addMovieListener(this);
			this.addChild(movieView);
			movieView.play();

			removeAllGestureEventListeners();
			movieView.removeAllGestureEventListeners();
			// addMovieViewGestureListerners();

			delegateProcessor(movieView, MTMOVIE.this, DragProcessor.class);
			delegateProcessor(movieView, MTMOVIE.this, RotateProcessor.class);
			delegateProcessor(movieView, MTMOVIE.this, ScaleProcessor.class);

			addGestureListener(DragProcessor.class, new DefaultDragAction());
			addGestureListener(RotateProcessor.class, new DefaultRotateAction());

			addGestureListener(ScaleProcessor.class,
					new IGestureEventListener() {

						@Override
						public boolean processGestureEvent(MTGestureEvent ge) {
							if (ge instanceof ScaleEvent) {
								ScaleEvent se = (ScaleEvent) ge;
								float factor = se.getScaleFactorX();
								movieView.scale(factor, factor, 1,
										getCenterPointLocal());
								movieView.setAnchor(PositionAnchor.UPPER_LEFT);
								movieView
										.setPositionRelativeToParent(new Vector3D());
								gestureArea.setAnchor(PositionAnchor.CENTER);
								gestureArea
										.setPositionRelativeToParent(new Vector3D(
												movieView
														.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) / 2,
												movieView
														.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)
														+ gestureArea
																.getHeightXY(TransformSpace.LOCAL)
														/ 2f));
								fileName.setPositionRelativeToParent(new Vector3D(
										movieView
												.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) / 2,
										movieView
												.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)
												+ GESTURE_AREA_HEIGHT
												+ (fileName
														.getHeightXY(TransformSpace.LOCAL) / 2f)));

							}
							return false;
						}
					});
		}
	}

	private void delegateProcessor(MTComponent from, final MTComponent to,
			Class<? extends IInputProcessor> processor) {
		from.addGestureListener(processor, new IGestureEventListener() {

			@Override
			public boolean processGestureEvent(MTGestureEvent gestureEvent) {
				gestureEvent.setTarget(to);
				return to.processGestureEvent(gestureEvent);
			}
		});
	}

	private void addVolumeSlider(GSMovie m) {
		float widthToUse = m.width > GESTURE_AREA_MAX_WIDTH ? GESTURE_AREA_MAX_WIDTH
				: m.width;
		this.setAnchor(PositionAnchor.UPPER_LEFT);
		Vector3D gestureAreaPosition = gestureArea
				.getPosition(TransformSpace.LOCAL);
		volumeSlider = new MTSlider(getRenderer(),

		gestureAreaPosition.x, gestureAreaPosition.y, VOLUME_SLIDER_WIDTH,
				VOLUME_SLIDER_HEIGHT, 0, 1);

		volumeSlider.getOuterShape().setFillColor(new MTColor(0, 0, 0, 80));
		volumeSlider.getOuterShape().setStrokeColor(new MTColor(0, 0, 0, 80));
		volumeSlider.getKnob().setFillColor(new MTColor(100, 100, 100, 80));
		volumeSlider.getOuterShape().setStrokeColor(
				new MTColor(100, 100, 100, 80));
		volumeSlider.setVisible(true);
		volumeSlider.setValue(1);
		volumeSlider.addPropertyChangeListener("value",
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent e) {
						movieView.setVolume(((Float) e.getNewValue())
								.doubleValue());
					}
				});
		gestureArea.addChild(volumeSlider);
		volumeSlider.rotateZ(new Vector3D(gestureAreaPosition.x,
				gestureAreaPosition.y), -90f);

		volumeSlider.translate(new Vector3D(widthToUse - gestureAreaXPadding
				- VOLUME_SLIDER_HEIGHT, VOLUME_SLIDER_WIDTH
				+ gestureAreaYPadding + VOLUME_SLIDER_HEIGHT));
	}

	private void addTimeSlider(GSMovie m) {
		float widthToUse = m.width > GESTURE_AREA_MAX_WIDTH ? GESTURE_AREA_MAX_WIDTH
				: m.width;

		Vector3D gestureAreaPosition = gestureArea
				.getPosition(TransformSpace.LOCAL);
		timeSlider = new MTSlider(getRenderer(), gestureAreaPosition.x
				+ gestureAreaXPadding, gestureAreaPosition.y
				+ gestureAreaYPadding, widthToUse - gestureAreaXPadding * 2f,
		// slider width
				TIME_SLIDER_HEIGHT, 0, 10);// min max values
		timeSlider.getOuterShape().setFillColor(new MTColor(0, 0, 0, 80));
		timeSlider.getOuterShape().setStrokeColor(new MTColor(0, 0, 0, 80));
		timeSlider.getKnob().setFillColor(new MTColor(100, 100, 100, 80));
		timeSlider.getOuterShape().setStrokeColor(
				new MTColor(100, 100, 100, 80));
		timeSlider.getKnob().addGestureListener(DragProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						DragEvent de = (DragEvent) ge;
						switch (de.getId()) {
						case MTGestureEvent.GESTURE_STARTED:
							stopSliderAdvance = true;
							break;
						case MTGestureEvent.GESTURE_UPDATED:
							break;
						case MTGestureEvent.GESTURE_ENDED:
							if (movieView != null
									&& movieView.getMovie() != null) {
								float currValue = timeSlider.getValue();
								movieView.jump(currValue);
							}
							stopSliderAdvance = false;
							break;
						default:
							break;
						}
						return false;
					}
				});
		timeSlider.setValueRange(0, m.duration());

		timeSlider.getOuterShape().addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@SuppressWarnings("deprecation")
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						switch (te.getTapID()) {
						case TapEvent.BUTTON_DOWN:
							stopSliderAdvance = true;
							break;
						case TapEvent.BUTTON_UP:
							stopSliderAdvance = false;
							break;
						case TapEvent.BUTTON_CLICKED:
							if (movieView != null
									&& movieView.getMovie() != null) {
								float currValue = timeSlider.getValue();
								movieView.jump(currValue);
							}
							stopSliderAdvance = false;
							break;
						default:
							break;
						}
						return false;
					}
				});
		timeSlider.setValue(0);
		gestureArea.addChild(timeSlider);
	}

	public void createGestureArea() {
		gestureArea = new MTRectangle(getRenderer(), MOVIE_PLAYER_WIDTH,
				GESTURE_AREA_HEIGHT);
		gestureArea.setAnchor(PositionAnchor.UPPER_LEFT);
		gestureArea.setFillColor(new MTColor(128, 128, 128, 50));
		gestureArea.setStrokeColor(new MTColor(100, 100, 100, 75));
		gestureArea.removeAllGestureEventListeners();
		addChild(gestureArea);

		gestureArea.setPositionRelativeToParent(new Vector3D(0f,
				MOVIE_PLAYER_HEIGHT));

		addPlayButton();
		addPauseButton();
		addStopButton();
		delegateProcessor(gestureArea, MTMOVIE.this, DragProcessor.class);
		delegateProcessor(gestureArea, MTMOVIE.this, ScaleProcessor.class);
		delegateProcessor(gestureArea, MTMOVIE.this, RotateProcessor.class);
	}

	private void addPlayButton() {
		playButton = new MTEllipse(getRenderer(), new Vector3D(),
				BUTTON_SIZE / 2f, BUTTON_SIZE / 2f);
		playButton.setTexture(ImageManager.getInstance()
				.load("media_start.png"));
		gestureArea.addChild(playButton);
		playButton.setPositionRelativeToParent(new Vector3D(gestureAreaXPadding
				+ BUTTON_SIZE / 2f, TIME_SLIDER_HEIGHT + gestureAreaYPadding
				* 2f + BUTTON_SIZE / 2f));
		playButton.removeAllGestureEventListeners();
		playButton.registerInputProcessor(new TapProcessor(getRenderer(), 25,
				true, 350));
		playButton.addGestureListener(TapProcessor.class, new TapController() {

			@Override
			public void action() {
				movieView.play();
			}
		});

	}

	private void addPauseButton() {
		pauseButton = new MTEllipse(getRenderer(), new Vector3D(),
				BUTTON_SIZE / 2f, BUTTON_SIZE / 2f);
		pauseButton.setTexture(ImageManager.getInstance().load(
				"media_pause.png"));
		gestureArea.addChild(pauseButton);
		pauseButton.setPositionRelativeToParent(new Vector3D(
				gestureAreaXPadding + BUTTON_SIZE / 2f, TIME_SLIDER_HEIGHT
						+ gestureAreaYPadding * 2f + BUTTON_SIZE / 2f));
		pauseButton.removeAllGestureEventListeners();

		pauseButton.registerInputProcessor(new TapProcessor(getRenderer(), 25,
				true, 350));
		pauseButton.addGestureListener(TapProcessor.class, new TapController() {

			@Override
			public void action() {
				if (movieView.isPlaying()) {
					movieView.pause();
				}
			}
		});

	}

	private void addStopButton() {

		MTEllipse stopButton = new MTEllipse(getRenderer(), new Vector3D(),
				BUTTON_SIZE / 2f, BUTTON_SIZE / 2f);
		stopButton
				.setTexture(ImageManager.getInstance().load("media_stop.png"));
		gestureArea.addChild(stopButton);
		stopButton.setPositionRelativeToParent(new Vector3D(gestureAreaXPadding
				* 2f + BUTTON_SIZE + BUTTON_SIZE / 2f, TIME_SLIDER_HEIGHT
				+ gestureAreaYPadding * 2f + BUTTON_SIZE / 2f));
		stopButton.removeAllGestureEventListeners();

		stopButton.registerInputProcessor(new TapProcessor(getRenderer(), 25,
				true, 350));
		stopButton.addGestureListener(TapProcessor.class, new TapController() {

			@Override
			public void action() {
				movieView.jump(0);
				movieView.pause();
			}
		});
	}

	private void updateTimeInfo(GSMovie m) {
		int totalElapsedSeconds = (int) m.time();
		int se = totalElapsedSeconds % 60;
		totalElapsedSeconds -= se;
		int totalElapsedMin = totalElapsedSeconds / 60;

		int me = totalElapsedMin % 60;
		totalElapsedMin -= me;
		int totalElapsedHours = totalElapsedMin / 60;
		int he = totalElapsedHours;

		totalElapsedSeconds = (int) movieView.getDuration();
		int sd = totalElapsedSeconds % 60;
		totalElapsedSeconds -= sd;
		totalElapsedMin = totalElapsedSeconds / 60;

		int md = totalElapsedMin % 60;
		totalElapsedMin -= md;
		totalElapsedHours = totalElapsedMin / 60;
		int hd = totalElapsedHours;

		displayTime(getTimeString(he, me, se, hd, md, sd));
	}

	private void displayTime(String timeString) {
		if (timeTextArea == null) {
			timeTextArea = new MTTextArea(getRenderer(), FontManager
					.getInstance().createFont(getRenderer(), "calibri", 16,
							MTColor.WHITE, true));
			timeTextArea.removeAllGestureEventListeners();
			timeTextArea.setNoFill(true);
			timeTextArea.setNoStroke(true);
			gestureArea.addChild(timeTextArea);
			timeTextArea.setPositionRelativeToParent(new Vector3D(
					gestureAreaXPadding * 2f + BUTTON_SIZE * 2f + BUTTON_SIZE
							/ 2f, TIME_SLIDER_HEIGHT + gestureAreaYPadding * 2f
							+ BUTTON_SIZE / 2f));
		}
		timeTextArea.setText(timeString);
	}

	private String getTimeString(int he, int me, int se, int hd, int md, int sd) {
		StrBuilder builder = new StrBuilder();
		if (hd > 0) {
			if (he < 10) {
				builder.append('0').append(he);
			} else {
				builder.append(he);
			}
			builder.append(":");
		}
		if (me < 10) {
			builder.append('0').append(me);
		} else {
			builder.append(me);
		}
		builder.append(":");
		if (se < 10) {
			builder.append('0').append(se);
		} else {
			builder.append(se);
		}

		builder.append(" / ");

		if (hd > 0) {
			if (hd < 10) {
				builder.append('0').append(hd);
			} else {
				builder.append(hd);
			}
			builder.append(":");
		}
		if (md < 10) {
			builder.append('0').append(md);
		} else {
			builder.append(md);
		}
		builder.append(":");
		if (sd < 10) {
			builder.append('0').append(sd);
		} else {
			builder.append(sd);
		}
		return builder.toString();
	}

	public void setGestureClassic() {

		movieView.registerInputProcessor(new TapAndHoldProcessor(
				(AbstractMTApplication) getRenderer(), 1000));

		movieView.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer((AbstractMTApplication) getRenderer(),
						this));

		setMasterGestureMenu();
	}

	/**
	 * Sets the configuration of the MTMOVIE master context menu.
	 */
	public void setMasterGestureMenu() {
		movieView.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent th = (TapAndHoldEvent) ge;
						switch (th.getId()) {
						case TapAndHoldEvent.GESTURE_STARTED:
							break;
						case TapAndHoldEvent.GESTURE_UPDATED:
							break;
						case TapAndHoldEvent.GESTURE_ENDED:
							if (th.isHoldComplete()) {
								if (menu != null) {
									menu.destroy();
								}
								menu = new MovieMenu(getRenderer(),
										getParent(), 40, 120, MTMOVIE.this, th);
								menu.rotateZGlobal(menu.getCenterPointGlobal(),
										0);
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
	}

	/**
	 * Sets the configuration of the MTMOVIE clones context menu.
	 */
	public void setCloneGestureMenu() {
		view.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent th = (TapAndHoldEvent) ge;
						switch (th.getId()) {
						case TapAndHoldEvent.GESTURE_STARTED:
							break;
						case TapAndHoldEvent.GESTURE_UPDATED:
							break;
						case TapAndHoldEvent.GESTURE_ENDED:
							if (th.isHoldComplete()) {
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
	}

	public void remove() {
		AnimationUtil.bounceOut(this, true);
	}

	public abstract class TapController implements IGestureEventListener {

		public boolean processGestureEvent(MTGestureEvent ge) {
			if (ge instanceof TapEvent && ((TapEvent) ge).isTapped()) {
				action();
			}
			return false;
		}

		public abstract void action();
	}

	@Override
	public void onPlay() {
		if (timeSlider != null) {
			timeSlider.setVisible(true);
		}
		enableButton(playButton, false);
		enableButton(pauseButton, true);
	}

	private void enableButton(MTEllipse button, boolean enable) {
		if (button != null) {
			button.setVisible(enable);
			button.setEnabled(enable);
		}
	}

	@Override
	public void onPause() {
		enableButton(pauseButton, false);
		enableButton(playButton, true);
	}

	@Override
	public void onLoop() {
		onPlay();
	}

	@Override
	public void onJump(GSMovie m) {
		if (timeSlider != null) {
			timeSlider.setVisible(true);
			timeSlider.setValue(m.time());
		}
	}

	@Override
	public void onStop() {
		enableButton(pauseButton, false);
		enableButton(playButton, true);
		if (isSlave) {
			view.setFillColor(MTColor.BLACK);
		} else {
			movieView.setFillColor(MTColor.BLACK);
		}
	}

	@Override
	public void onNewFrame(GSMovie m) {
		if (!stopSliderAdvance && m != null && timeSlider != null) {
			timeSlider.setValue(m.time());
		}
		updateTimeInfo(m);
	}

	@Override
	public void onFirstFrame(GSMovie m) {
		addTimeSlider(m);
		addVolumeSlider(m);
		addFileName(m);
		updateTimeInfo(m);
		movieView.loop();
		movieView.pause();
		updateSize(m);
	}

	private void addFileName(GSMovie m) {
		fileName = new MTTextArea(getRenderer(), FontManager.getInstance()
				.createFont(getRenderer(), "calibri", 16, MTColor.WHITE, true));
		fileName.setAnchor(PositionAnchor.CENTER);
		fileName.removeAllGestureEventListeners();
		fileName.setText(movieModel.getFile().getName());
		fileName.setFillColor(new MTColor(128, 128, 128, 50));
		fileName.setStrokeColor(new MTColor(100, 100, 100, 75));
		addChild(fileName);
		fileName.setPositionRelativeToParent(new Vector3D(m.width / 2f,
				m.height + GESTURE_AREA_HEIGHT
						+ (fileName.getHeightXY(TransformSpace.LOCAL) / 2f)));
		delegateProcessor(fileName, this, DragProcessor.class);
	}

	private void updateSize(GSMovie m) {
	
			gestureArea.setPositionRelativeToParent(new Vector3D(0f, m.height));
			if (m.width > GESTURE_AREA_MAX_WIDTH) {
				gestureArea.setWidthLocal(GESTURE_AREA_MAX_WIDTH);
			} else {
				gestureArea.setWidthLocal(m.width);
			}
	}

	@Override
	protected void destroyComponent() {
		super.destroyComponent();
			this.movieView.noLoop();
	}

	public File getFile() {
		return movieModel.getFile();
	}

	public float getWidth() {
			return movieView.getWidthXY(TransformSpace.LOCAL);
	}
}
