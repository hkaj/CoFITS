package Requests;

import java.io.IOException;

import Constants.RequestConstants.requestTypes;
import DatabaseScheme.ReferenceTable;
import ModelObjects.Document;
import ModelObjects.ModelObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddRequest implements BaseRequest
{

	final ModelObject subject;
	@JsonCreator
	public AddRequest(@JsonProperty("subject")ModelObject subject)
	{
		this.subject = subject;
	}

	@Override @JsonIgnore
	public requestTypes getRequestType()
	{
		return Constants.RequestConstants.requestTypes.create;
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
		final String att, val, res;
		final ReferenceTable host=this.subject.getReferenceTable();
		if(!(this.subject instanceof Document))
		{
			
			att = toSeparatedList(host.getAttributes(),", ");
			val = toSeparatedList(this.subject.getValue(),",");
		}else
		{//Cas particulier de document dont l'id est gérée automatiquement
			Document temp = (Document)this.subject;
			att = "name, type";
			val = "'"+temp.getName().replace("'", "''")+"', '"+temp.getType()+"'";
		}
		res ="INSERT INTO "+host.getName()+"("+att+") VALUES ("+val+");";
		return res;
	}

}
