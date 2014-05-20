package utc.bsfile.util;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;

public class FileExtensionFilter implements FileFilter {
	public static final FileExtensionFilter IMG_FILTER = new FileExtensionFilter(
			"bmp", "png", "jpg", "jpeg");
	public static final FileExtensionFilter VIDEO_FILTER = new FileExtensionFilter(
			"mov", "mpeg", "mpg", "m4v", "wmv", "mp4","avi");
	public static final FileFilter PDF_FILTER = new FileExtensionFilter("pdf");
	public static final FileFilter HTML_FILTER = new FileExtensionFilter(
			"html", "htm");
	public static final FileFilter MM_FILTER = new FileExtensionFilter(
			"mm");
	public static final FileFilter TXT_FILTER = new FileExtensionFilter(
	"txt");
	public static final FileExtensionFilter NO_FILTER = new FileExtensionFilter(
			"*");
	/**
	 * @uml.property name="extensions"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private LinkedList<String> extensions;

	public FileExtensionFilter(Object... extensions) {
		this.extensions = new LinkedList<String>();
		for (Object extension : extensions)
			if (extension instanceof String) {
				this.extensions.add(((String) extension).toLowerCase());
				this.extensions.add(((String) extension).toUpperCase());
			}
	}

	public boolean accept(File file) {
		if (!file.isHidden()) {
			if (file.isDirectory() && !file.getName().startsWith("."))
				return true;

			if (file.isFile()) {
				
				if (this.equals(NO_FILTER) && !file.getName().startsWith(".")) {
					return true;
				}
				String fileName = file.getName();
				int pt = fileName.lastIndexOf(".");
				if (pt >= 0) {

					String ext = fileName.substring(pt + 1);
					if (extensions.contains(ext))
						return true;
				}
			}
		}
		return false;
	}

	public LinkedList<String> getExtensios() {
		return this.extensions;
	}

	@Override
	public boolean equals(Object obj) {

		return this.extensions.containsAll(((FileExtensionFilter) obj)
				.getExtensios());
	}

}
