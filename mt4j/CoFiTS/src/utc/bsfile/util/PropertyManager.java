package utc.bsfile.util;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.mt4j.util.MTColor;

/**
 * Class used for managing the project properties
 * give facilities for some properties
 * Give the path to directories where are stored binaries of certain type
 */

public class PropertyManager {
	
	public static String CONF_FILENAME = "rsc/config/bsfile.properties";
	public static String FILE_PATH = "file-path";
	public static String ICONE_DIR = "icone-dir";
	public static String MAIN_FONT = "main-font";
	public static String PDF_FONT = "pdf-font";
	public static String PICK_FONT = "pick-font";
	private static Properties props;
	private static PropertyManager propertymanager;
	

	public static PropertyManager getInstance() {
		if (propertymanager == null) {
			propertymanager = new PropertyManager();
			props = new Properties();
			try {
				props.load(new FileReader(CONF_FILENAME));
			} catch (FileNotFoundException e) {
				System.out.printf("Configuration file %s not found",
						CONF_FILENAME);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return propertymanager;
	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}

	public String getDirProperty (String key) {
		String dir = props.getProperty(key);
		if (dir.startsWith("~")) {
			dir = dir.substring(1);
			dir = System.getProperty("user.home").concat(dir);
			System.out.println("Directory of files" + dir);
		}
		return dir;
	}
	
	public Set<Object> keys() {
		return props.keySet();
	}

	public int getIntProperty(String key) {
		String s = getProperty(key);
		return Integer.parseInt(s);
	}
	
	public boolean getBooleanProperty(String key) {
		boolean r = false;
		String s = getProperty(key);
		if (s.equalsIgnoreCase("true")) {
			r = true;
		}
		
		return r;
	}

}
