package Requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import Constants.RequestConstants;
import Constants.RequestConstants.objectTypes;
import Constants.RequestConstants.requestTypes;
import DatabaseScheme.ReferenceTable;
import DatabaseScheme.TableDocuments;
import DatabaseScheme.TableProjects;
import DatabaseScheme.TableSessions;
import DatabaseScheme.TableUsers;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
include = JsonTypeInfo.As.PROPERTY,
property = "type")
public class SelectRequest implements BaseRequest
{

	private final objectTypes targetObject;
	private final Predicate[] predicates;
	
	@JsonCreator
	public SelectRequest(@JsonProperty("targetObject") objectTypes target, @JsonProperty("predicates") Predicate[] p)
	{
		this.targetObject = target;
		this.predicates = p.clone();
	}

	public SelectRequest(objectTypes target)
	{
		this(target,new Predicate[0]);
	}
	
	@Override @JsonIgnore
	public requestTypes getRequestType()
	{
		return requestTypes.select;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
	}
	
	@Override
	public String toString()
	{
		String res = "Sujet : "+this.targetObject.toString();
		for (Predicate p : this.predicates)
		{
			res += ", "+ p.toString();
		}
		return res;
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
		//Tri des prédicats en filtre et relation
		List<Relation> listRelation = new ArrayList<Relation>();
		List<Filter> listFilter = new ArrayList<Filter>();
		for(Predicate p : this.predicates)
		{
			if(p instanceof Filter)
			{
				listFilter.add((Filter)p);
			}else //Que spécialisation exclusive en deux classes seulement
			{
				listRelation.add((Relation)p);
			}
		}
		
		final ReferenceTable host;
		
		
		switch(this.targetObject)
		{
		case document :
			host=TableDocuments.getInstance();
			break;
		case project :
			host=TableProjects.getInstance();
			break;
		case session :
			host=TableSessions.getInstance();
			break;
		case user :
			host=TableUsers.getInstance();
			break;
		default :
			host=null;
			break;
		}
		
		final String nameHost = host.getName();
		final String mainAttributes, tablesList, conditions;

		//Définition des attributs principaux
		String[] temp =host.getAttributes();
		String[] att = new String[temp.length];
		for (int i=0; i < temp.length; i++)
		{
			att[i]=nameHost+"."+temp[i];
		}
		
		mainAttributes = toSeparatedList(att,", ");
		
		//Définition de la liste des tables à joindre
		Set<String> jointedTables = new TreeSet<String>();
		jointedTables.add(host.getName());
		for(Relation r : listRelation)
		{
			jointedTables.add(r.getRelationTable().getName());
		}
		tablesList = toSeparatedList(jointedTables.toArray(new String[0]),", ");
		
		//Définition des conditions
		//Conditions filtres
		List<String> formattedConditions = new LinkedList<String>();
		for(Filter f : listFilter)
		{
			formattedConditions.add(nameHost+"."+f.getProperty()+"='"+f.getValue()+"'");
		}
		//Conditions relation
		for(Relation r : listRelation)
		{
			//Jointure
			
			final String nameObject = r.getRelationTable().getName();
			final String[] keyHost = host.getKeyAttributes();
			for(String k : keyHost)
			{
				formattedConditions.add(nameHost+"."+k+" = "+nameObject+"."+k);
			}
			
			//Restriction
			final String[] keyObject = r.getSubject().getKeyConditions();
			for(String k : keyObject)
			{
				formattedConditions.add(nameObject+"."+k);
			}
		}

		//Assemblage conditions
		conditions=toSeparatedList(formattedConditions.toArray(new String[0]), " AND ");

		//Assemblage général
		String res ="SELECT " + mainAttributes + " FROM " + tablesList;
		if(!conditions.equals(""))
		{
			res += " WHERE " + conditions;
		}
		res +=";";
		return res;
	}
	
	public RequestConstants.objectTypes getTargetObject()
	{
		return this.targetObject;
	}

}
