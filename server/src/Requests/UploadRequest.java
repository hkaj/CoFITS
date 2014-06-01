package Requests;

import java.io.FileOutputStream;
import java.io.IOException;

import Constants.RequestConstants;
import Constants.RequestConstants.requestTypes;
import ModelObjects.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UploadRequest implements BaseRequest
{
	final Document subject;
	final byte[] content;
	@JsonCreator
	public UploadRequest(@JsonProperty("subject")Document subject, @JsonProperty("content")byte[] content)
	{
		this.subject = subject;
		this.content = content;
	}

	@Override @JsonIgnore
	public requestTypes getRequestType()
	{
		return requestTypes.upload;
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
	
	public void exec()
	{
		//NB : pour l'instant, aucune vérification pour vérifier que le document est bien présent n'est réalisée.
		final String path = RequestConstants.documentAgentDirectory+this.subject.getName()+this.subject.getType();
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream(path);
			fos.write(this.content);
			fos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}		
	}
}
