package utc.tatinpic.gui.widgets.fileshare;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import utc.tatinpic.util.manager.PropertyManager;

public class FileShareHelper {

	public static boolean hasFile(File file, String teamMember) {
		String dir = PropertyManager.getInstance().getDirProperty(PropertyManager.DROPBOX_DIR) + teamMember + "-tatin-pic/";
		File memberRootFolder = new File(dir);
		if (!memberRootFolder.exists()) {
			memberRootFolder.mkdir();
			return false;
		}
		File[] files = memberRootFolder.listFiles();
		return explore(files, file);
		
	}

	private static boolean explore(File[] files, File file) {
		for(File f : files) {
			if (f.isFile()) {
				if (isSameFile(f, file)) {
					return true;
				}
			} else {
				if (f.isDirectory()) {
					if (explore(f.listFiles(), file)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean isSameFile(File f, File file) {
		return f.getName().equals(file.getName())/* && f.lastModified() == file.lastModified()*/;
	}

	public static void copy(File file, String member) {
		File copy = new File(PropertyManager.getInstance().getDirProperty(PropertyManager.DROPBOX_DIR) + member + "-tatin-pic/" + file.getName());
		try {
			Files.copy(file.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.println("copy failed");
			e.printStackTrace();
		}
	}
}
