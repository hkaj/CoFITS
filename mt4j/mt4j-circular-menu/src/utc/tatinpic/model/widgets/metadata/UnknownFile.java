package utc.tatinpic.model.widgets.metadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UnknownFile {

	private File file;
	private MetadataDictionary metadata;
	private List<String> NamesMetadata;
	
	public UnknownFile(File file)
	{
		this.file = file;
		execute();
	}
	public UnknownFile(String pathFile)
	{
		this.file = new File(pathFile);
		execute();

	}
	
	public File getFile()
	{
		return this.file;
	}
	
	private void execute()
	{
		this.metadata = new MetadataDictionary();
		this.NamesMetadata = new ArrayList<String>();
		
		BasicFileAttributes attr;
		try {
			
			this.NamesMetadata.add("owner");
			this.metadata.putMetadata("owner", Files.getOwner(file.toPath()).getName());
			
			this.NamesMetadata.add("name");
			this.metadata.putMetadata("name", this.file.getName());
			
			attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			this.NamesMetadata.add("creationTime");
			this.metadata.putMetadata("creationTime", maskDate(attr.creationTime().toMillis()));
			this.NamesMetadata.add("lastAccessTime");
			this.metadata.putMetadata("lastAccessTime",  maskDate(attr.lastAccessTime().toMillis()));
			this.NamesMetadata.add("lastModifiedTime");
			this.metadata.putMetadata("lastModifiedTime",  maskDate(attr.lastModifiedTime().toMillis()));
			this.NamesMetadata.add("isDirectory");
			this.metadata.putMetadata("isDirectory",attr.isDirectory() +"");			
			this.NamesMetadata.add("isOther");
			this.metadata.putMetadata("isOther", attr.isOther()+"");
			this.NamesMetadata.add("isRegularFile");
			this.metadata.putMetadata("isRegularFile", attr.isRegularFile()+"");
			this.NamesMetadata.add("isSymbolicLink");
			this.metadata.putMetadata("isSymbolicLink", attr.isSymbolicLink() + "");
			this.NamesMetadata.add("size");
			this.metadata.putMetadata("size", maskSize(attr.size() + ""));
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String getMetadata(String meta)
	{
		if(this.metadata.getMetadata(meta) != null)
			return this.metadata.getMetadata(meta);
		else
			return "Unknown";
	}
	
	public String getNameFile()
	{
		return getMetadata("name");
	}
	
	public String getOwer()
	{
		return getMetadata("owner");
	}
	
	
	public String getDateCreation()
	{
		return getMetadata("creationTime");
	}
	
	public String getLastAccess()
	{
		return getMetadata("lastAccessTime");
	}
	
	public String getLastModified()
	{
		return getMetadata("lastModifiedTime");
	}
	
	public String getSize()
	{
		return getMetadata("size");
	}
	
	
	
	private String maskSize(String value)
	{
		int size = value.length();
		String result = "";
		if(size <= 3)
		{
			result = value + " bytes";
		}
		else if(size <= 6)
		{
			result = value.substring(0, size-3) +","+rounding(value.substring(size-3)) + " Kb";
		}
		else if(size <= 9)
		{
			result = value.substring(0, size-6) +","+rounding(value.substring(size-6)) + " Mb";
		}
		else
		{
			result = value.substring(0, size-9) +","+rounding(value.substring(size-9)) + " Gb";
		}
		
		return result;
	}
	private String rounding(String value)
	{
		String result = value;
		if(value.length() > 1)
		{
			if(Integer.parseInt(value.charAt(1)+"") < 5)
			{
				result = value.charAt(1)+"";
			}
			else
			{
				result = (Integer.parseInt(value.charAt(0)+"") + 1)+"";
			}
		}
		return result;
	}
	
	public List<String> getListMetaTags()
	{
		return this.NamesMetadata;
	}
	
	public String getDescription(String Key)
	{
		return this.metadata.getMetadata(Key);
	}
	public boolean isDescription(String Key)
	{
		return this.metadata.hasMetadata(Key);
	}
	
	private String maskDate(long timeInMillis)
	{
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMillis);
		return formatter.format(calendar.getTime()); 

	}
	public String ToStringMetada()
	{
		return this.metadata.toString();
	}
	
	public String ToStringMetada(List<String> keys)
	{
		return this.metadata.toString(keys);
	}
	
}
