/**
 * 
 */
package utc.bsfile.model.menu;

import java.io.File;
import java.io.FileFilter;

import javax.swing.filechooser.FileSystemView;

public class FileChooserModel implements IMenuModel {
	/**
	 * @uml.property name="start"
	 */
	private File start;
	/**
	 * @uml.property name="current"
	 */
	private Object current;
	/**
	 * @uml.property name="filter"
	 */
	private FileFilter filter;

	public FileChooserModel(File start, FileFilter filter) {
		this.start = start.getAbsoluteFile();
		this.current = getStartMenu();
		this.filter = filter;
	}

	public FileChooserModel(File start) {
		this(start, null);
	}

	@Override
	public Object getStartMenu() {
		return start;
	}

	@Override
	public Object getCurrentMenu() {
		return current;
	}

	@Override
	public void setCurrentMenu(Object current) {
		this.current = current;
	}

	@Override
	public boolean hasChoices(Object choice) {
		if (choice instanceof File)
			return ((File) choice).listFiles(filter) != null;
		return false;
	}

	@Override
	public Object[] getChoices(Object choice) {
		File[] files = ((File) choice).listFiles(filter);
		if (files != null) {
			if (choice instanceof File) {
				return putDirectoriesOnTop(files);
			}
		}

		return null;
	}

	private File[] putDirectoriesOnTop(File[] listFiles) {
		File files[] = new File[listFiles.length];
		int i = 0;
		for (File file : listFiles) {
			if (file.isDirectory()) {
				files[i++] = file;
			}
		}
		for (File file : listFiles) {
			if (!file.isDirectory()) {
				files[i++] = file;
			}
		}
		return files;
	}

	@Override
	public Object getParentMenu(Object current) {
		if (current instanceof File)
			return FileSystemView.getFileSystemView().getParentDirectory(
					((File) current));
		return null;
	}

	@Override
	public boolean onlyStartMenuCanCancel() {
		return false;
	}

	/**
	 * @return
	 * @uml.property name="filter"
	 */
	public FileFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 * @uml.property name="filter"
	 */
	public void setFilter(FileFilter filter) {
		this.filter = filter;
	}
}
