package utc.tatinpic.gui.widgets.fileshare;

import java.util.ArrayList;
import java.util.List;

public class FileShareMenuManager {

	private static List<FileShareMenu> menus = new ArrayList<FileShareMenu>();

	public static void add(FileShareMenu fileShareMenu) {
		menus.add(fileShareMenu);
	}

	public static void remove(FileShareMenu fileShareMenu) {
		menus.remove(fileShareMenu);
	}

	public static void onShare(FileShareMenu fileShareMenu, List<String> shareWithMembers) {
		List<FileShareMenu> toUpdateMenus = new ArrayList<FileShareMenu>();
		for (FileShareMenu menu : menus) {
			if (menu != fileShareMenu && menu.getFile().equals(fileShareMenu.getFile())) {
				toUpdateMenus.add(menu);
			}
		}
		for (FileShareMenu menu : toUpdateMenus) {
			menu.setAlreadySharedWith(shareWithMembers);
		}
		
		
	}
}
