package Requests;

import java.io.IOException;

import Constants.RequestConstants.requestTypes;
import ModelObjects.ModelObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class LinkRequest implements BaseRequest
{
	final ModelObject subject;
	final Relation relation;

	@JsonCreator
	public LinkRequest(@JsonProperty("subject")ModelObject subject,
			@JsonProperty("relation")Relation relation)
	{
		this.subject = subject;
		this.relation =  relation;
	}

	@Override @JsonIgnore
	public requestTypes getRequestType()
	{
		return requestTypes.link;
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
	
	static private String toSeparatedList(String[] elements, String separator)
	{
		String res = "";
		if(elements.length>1)
		{
			for(int i= 0; i < elements.length -1; i++)
			{
				res +=elements[i]+separator;
			}
			res+=elements[elements.length-1];
		}else if (elements.length == 1) {
			res = elements[0];
		}
			
		return res;
	};
	
	@Override
	public String toSQL()
	{
		String att="",table="",values="";
		
		att=toSeparatedList(this.relation.getRelationTable().getAttributes(), ", ");
		table = this.relation.getRelationTable().getName();
		
		String[] a = this.subject.getKeyValues();
		String[] b = this.relation.getSubject().getKeyValues();
		String[] c = new String[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		
		values=LinkRequest.toSeparatedList(c, ", ");
		
		return "INSERT INTO "+table +"("+att+") VALUES("+values+");";
	}

}
