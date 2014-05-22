package ModelObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BinaryContent
{
	final byte[] content;
	
	@JsonCreator
	public BinaryContent(@JsonProperty("content")byte[] content)
	{
		this.content = content;
	}
	
	public byte[] getContent()
	{
		return this.content;
	}

}
