package utc.tatinpic.model.widgets.metadata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MetadataDictionary {

	private HashMap<String, String> dic;
	
	public MetadataDictionary()
	{
		this.dic = new HashMap<String, String>();
	}
	
	
	public void putMetadata(String Key, String value)
	{
		this.dic.put(Key, value);
	}
	
	public boolean hasMetadata(String Key)
	{
		return this.dic.containsKey(Key);
	}
	
	public String getMetadata(String Key)
	{
		return this.dic.get(Key);
	}
	
	@Override
	public String toString()
	{
		String result = "";
		
		Iterator iterator = dic.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Map.Entry pairs = (Map.Entry)iterator.next();
	        result += pairs.getKey() + " = " + pairs.getValue()+"\r\n";
		}
		
		return result;
	}
	
	public String toString(List<String> Keys)
	{
		String result = "";
		Iterator iterator = Keys.iterator();
		
		while(iterator.hasNext())
		{
			String item = (String)iterator.next();
	        if(this.dic.containsKey(item))
			result += item + " = " + this.dic.get(item);
		}
		
		
		return result;
	}
}
