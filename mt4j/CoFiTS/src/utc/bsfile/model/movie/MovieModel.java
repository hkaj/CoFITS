package utc.bsfile.model.movie;

import java.io.File;

public class MovieModel {

	/** Default frame per second */
	public static final int FPS = 30;

	/** The duration. */
	float duration = 0.0f;

	/**
	 * @uml.property name="movieFile"
	 */
	private File movieFile;

	/**
	 * @uml.property name="movieValid"
	 */
	private boolean movieValid = true;

	/**
	 * 
	 * @param file
	 *            MOVIE file
	 */
	public MovieModel(File file) {
		this(file.getAbsolutePath());
	}

	/**
	 * 
	 * @param fileName
	 *            absolute path of the MOVIE File
	 */
	public MovieModel(String fileName) {
		try {
			movieFile = new File(fileName);
		} catch (Exception e) {
			System.err.println("MovieModel.MOVIEExample - Error : " + e);
			this.setMovieValid(false);
			e.printStackTrace();
		}
	}

	/**
	 * Gets the movie File.
	 * 
	 * @return a File.
	 */
	public File getFile() {
		return movieFile;
	}

	/**
	 * @param movieValid
	 * @uml.property name="movieValid"
	 */
	public void setMovieValid(boolean movieValid) {
		this.movieValid = movieValid;
	}

	/**
	 * @return
	 * @uml.property name="movieValid"
	 */
	public boolean isMovieValid() {
		return movieValid;
	}

}