package utc.tatinpic.model.image;

import java.io.File;

public class IMAGEModel
{
	/**
	 * @uml.property  name="imageFile"
	 */
	private File imageFile;
	
	/**
	 * @uml.property  name="imageValid"
	 */
	private boolean imageValid = true;
	
	/**
	 * 
	 * @param fileName absolute path of the IMAGE File
	 */
	public IMAGEModel(String fileName)
	{
		try
		{
			imageFile = new File(fileName);
			
		}
		catch(Exception e)
		{
			System.out.println("IMAGEExample.IMAGEExample - Error : " + e);
			this.setImageValid(false);
		}
	}

	/**
	 * 
	 * @param file image file
	 */
	public IMAGEModel(File file)
	{
		this(file.getAbsolutePath());
	}

	
	/**
	 * Gets the image File.
	 * 
	 * @return a File.
	 */
	public File getFile()
	{
		return imageFile;
	}


	/**
	 * @param imageValid
	 * @uml.property  name="imageValid"
	 */
	public void setImageValid(boolean imageValid)
	{
		this.imageValid = imageValid;
	}

	/**
	 * @return
	 * @uml.property  name="pdfValid"
	 */
	public boolean isImageValid()
	{
		return imageValid;
	}
}