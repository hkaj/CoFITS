package Requests;

import java.io.IOException;

import Constants.RequestConstants.requestTypes;
import ModelObjects.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DownloadRequest implements BaseRequest
{
	final Document subject;
	final String user;
	
	@JsonCreator
	public DownloadRequest(@JsonProperty("subject")Document subject, @JsonProperty("user")String user)
	{
		this.subject = subject;
		this.user = user;
	}

	@Override @JsonIgnore
	public requestTypes getRequestType()
	{
		return requestTypes.download;
	}

	@Override
	public String toJSON()
	{
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibilityChecker(
			     mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY)
			  );
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
		try
		{
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e)
		{
			e.printStackTrace();
			return "error";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
	}

	@Override
	public String toSQL()
	{
		return "";
	}
	
	public Document getSubject()
	{
		return subject;		
	}

}
