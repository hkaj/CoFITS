package utc.bsfile.util;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * @class ImageManager
 * @brief Singleton to load an Image from memory into PImage object *
 */
public class ImageManager 
{	
	private static volatile ImageManager instance;
	
	public final static ImageManager getInstance()
	{
		if (instance == null)
		{
			synchronized (ImageManager.class)
			{
				if (instance == null)
					instance = new ImageManager();
			}
		}
		
		return instance;
    }

	private ImageManager ()
	{
		
	}
	
	/**
	 * @uml.property  name="applet"
	 * @uml.associationEnd  
	 */
	private PApplet applet;
		
	public void init (PApplet applet)
	{
		this.applet = applet;
	}

    public PImage load(String imagePath)
    {
    	String path = PropertyManager.getInstance().getProperty(PropertyManager.ICONE_DIR) + imagePath;
		PImage image = applet.loadImage(path);
		if (image == null)
			throw new NullPointerException("ERR - Image load failed for " + imagePath + ".");
		System.out.println("INFO - " + imagePath + " is loaded.");
		
		return image; 
    }
}
