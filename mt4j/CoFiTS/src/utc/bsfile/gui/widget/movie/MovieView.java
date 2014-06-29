package utc.bsfile.gui.widget.movie;

import javax.swing.event.EventListenerList;

import org.mt4j.components.bounds.BoundsZPlaneRectangle;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.opengl.GLTexture;

import processing.core.PApplet;
import utc.bsfile.model.movie.MovieListener;
import codeanticode.gsvideo.GSMovie;
import codeanticode.gsvideo.GSVideo;

public class MovieView extends MTRectangle {

	/** The movie. */
	protected GSMovie movie;

	/** The m. */
	private GSMovie m;

	/** The first time read. */
	private boolean firstTimeRead = true;

	/** The duration. */
	float duration;

	/**
	 * @uml.property name="listeners"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final EventListenerList listeners = new EventListenerList();

	/**
	 * Instantiates a new MovieView
	 * 
	 * @param movieFile
	 *            the movie file - located in the ./data directory
	 * @param upperLeft
	 *            the upper left movie position
	 * @param ifps
	 *            the ifps the frames per second
	 * @param pApplet
	 *            the applet
	 */
	public MovieView(String movieFile, int ifps, PApplet pApplet,
			float maxWidth, float maxHeight) {
		super(pApplet, maxWidth, maxHeight);
		this.duration = 0.0f;
		this.setName("movieclip: " + movieFile);
		try {
		//	movie = new GSMovie(pApplet, movieFile, ifps);
			GSVideo.localGStreamerPath = "gst/library/gstreamer/windows64";
			movie = new GSMovie(pApplet, movieFile);
			movie.frameRate(ifps);
			movie.setEventHandlerObject(this);
			if (MT4jSettings.getInstance().isOpenGlMode()) {
				this.setUseDirectGL(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected IBoundingShape computeDefaultBounds() {
		return new BoundsZPlaneRectangle(this);
	}

	/**
	 * Movie event.
	 * 
	 * @param myMovie
	 *            the my movie
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void movieEvent(GSMovie myMovie) throws InterruptedException {
		m = myMovie;
	}

	protected void onFirstFrame() {
		if (m.available()) {
			m.read();
			System.out
					.println("onFirstFrame() : Movie img format: " + m.format);

			this.setSizeLocal(m.width, m.height);

			this.setTexture(m);
			this.setTextureEnabled(true);
			for (MovieListener l : getMovieListeners()) {
				l.onFirstFrame(m);
			}
		}
	}

	protected void onNewFrame() {
		for (MovieListener l : getMovieListeners()) {
			l.onNewFrame(m);
		}
	}

	@Override
	public void updateComponent(long timeDelta) {
		super.updateComponent(timeDelta);

		if (m != null) {
			if (firstTimeRead && m.available()) {
				this.onFirstFrame();
				firstTimeRead = false;
			} else {
				if (m != null && m.isPlaying() && m.available()) {
					if (this.getTexture() instanceof GLTexture) {
						if (this.isUseDirectGL()
								&& MT4jSettings.getInstance().isOpenGlMode()) {
//							((GLTexture) this.getTexture()).updateGLTexture(m
//									.getMoviePixelsBuffer());
							((GLTexture) this.getTexture()).updateGLTexture(m
									.pixels);
						} else {
							m.read();
							((GLTexture) this.getTexture())
									.loadPImageTexture(m);
						}
					} else {
						// Usually all textures should be GLTextures instances,
						// but just to be sure..
						m.read();
						this.setTexture(m); // SLOW!!!
					}

					this.onNewFrame();
				}
			}
		}
	}

	@Override
	protected void destroyComponent() {
		super.destroyComponent();

		if (m != null) {
			m.dispose();
		}
	}

	public GSMovie getMovie() {
		return this.movie;
	}

	/**
	 * Gets the duration.
	 * 
	 * @return the duration
	 */
	public float getDuration() {// duration only valid if video is playing
		if (getMovie().duration() == 0.0) {
			return duration;
		} else {
			duration = getMovie().duration();
			return duration;
		}
	}

	/**
	 * Play.
	 */
	public void play() {
		getMovie().play();
		for (MovieListener l : getMovieListeners()) {
			l.onPlay();
		}
	}

	/**
	 * Pause.
	 */
	public void pause() {
		getMovie().pause();
		for (MovieListener l : getMovieListeners()) {
			l.onPause();
		}
	}

	/**
	 * Stop.
	 */
	public void stop() {
		getMovie().stop();
		for (MovieListener l : getMovieListeners()) {
			l.onStop();
		}
	}

	/**
	 * Loop the movie.
	 */
	public void loop() {
		getMovie().loop();
		for (MovieListener l : getMovieListeners()) {
			l.onLoop();
		}
	}

	/**
	 * No looping.
	 */
	public void noLoop() {
		getMovie().noLoop();
	}

	/**
	 * Jump.
	 * 
	 * @param where
	 *            the where
	 */
	public void jump(float where) {
		getMovie().jump(where);
		for (MovieListener l : getMovieListeners()) {
			l.onJump(m);
		}
	}

	/**
	 * Time.
	 * 
	 * @return the time the movie plays in float
	 */
	public float getTime() {
		return getMovie().time();
	}

	/**
	 * Go to beginning.
	 */
	public void goToBeginning() {
		getMovie().goToBeginning();
	}

	/**
	 * Change the volume. Values are from 0 to 1.
	 * 
	 * @param v
	 *            the new volume
	 */
	public void setVolume(double v) {
		//getMovie().volume(v);
		getMovie().volume((float) v);
	}

	/**
	 * Adds a MOVIEListener to the MOVIE.
	 * 
	 * @param l
	 *            listener.
	 * @see #removeMOVIEListener(MovieListener)
	 */
	public void addMovieListener(MovieListener l) {
		listeners.add(MovieListener.class, l);
	}

	/**
	 * Removes a MOVIEListener to the MOVIE.
	 * 
	 * @param l
	 *            listener.
	 * @see #addMOVIEListener(MovieListener)
	 */
	public void removeMovieListener(MovieListener l) {
		listeners.remove(MovieListener.class, l);
	}

	/**
	 * Gets the list of MOVIEListener that the MOVIE holds.
	 * 
	 * @return a {@link MovieListener} list.
	 */
	public MovieListener[] getMovieListeners() {
		return listeners.getListeners(MovieListener.class);
	}

	public boolean isPlaying() {
		return m != null 
				&& m.isPlaying();
	}


}
