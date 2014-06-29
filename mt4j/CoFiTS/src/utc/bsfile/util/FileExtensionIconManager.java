/**
 * 
 */
package utc.bsfile.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;
import utc.bsfile.util.FileExtensionFilter;

public class FileExtensionIconManager {
	private static volatile FileExtensionIconManager instance;

	public final static FileExtensionIconManager getInstance() {
		if (FileExtensionIconManager.instance == null) {
			synchronized (FileExtensionIconManager.class) {
				if (FileExtensionIconManager.instance == null)
					FileExtensionIconManager.instance = new FileExtensionIconManager();
			}
		}

		return FileExtensionIconManager.instance;
	}

	private FileExtensionIconManager() {
		fileExtensionIcons = new HashMap<String, PImage>();
	}

	/**
	 * @uml.property name="applet"
	 * @uml.associationEnd
	 */
	private PApplet applet;
	/**
	 * @uml.property name="fileExtensionIcons"
	 * @uml.associationEnd 
	 *                     qualifier="extension:java.lang.String processing.core.PImage"
	 */
	private HashMap<String, PImage> fileExtensionIcons;

	public void init(PApplet applet) {
		this.applet = applet;
	}

	public PImage getIcon(String extension) throws IOException {
		
		PImage icon = fileExtensionIcons.get(extension);
		
		if (icon == null) {
			File extDirectory = new File(PropertyManager.getInstance().getDirProperty(PropertyManager.ICONE_DIR));
			File[] files = extDirectory.listFiles(FileExtensionFilter.IMG_FILTER);

			for (File file : files)
				if (file.getName().indexOf(extension) == 0) {
					icon = applet.loadImage(file.getAbsolutePath());
					fileExtensionIcons.put(extension, icon);
					break;
				}
		}

		return icon;
	}

	public PImage getIcon(File file) throws IOException {
		if (file.isDirectory())
			return getIcon("directory");

		String fileName = file.getName();
		String ext = fileName.substring(fileName.length() - 4);

		if (ext.charAt(0) == '.') {
			ext = ext.substring(ext.length() - 3);
			return getIcon(ext);
		}

		return null;
	}
}
