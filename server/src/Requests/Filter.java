package Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public class Filter implements Predicate
{

	final String property,value;
	public Filter(@JsonProperty("property") String property, @JsonProperty("value") String value)
	{
		this.property=property;
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
	
	@Override
	public String toString()
	{
		return "Property= "+this.property + " value="+ this.value;
	}
	
	public String getProperty()
	{
		return property;
	}
}