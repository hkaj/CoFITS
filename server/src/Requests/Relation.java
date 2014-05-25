package Requests;

import DatabaseScheme.RelationTable;
import ModelObjects.ModelObject;
import SpecialisedRelations.InvolvedIn;
import SpecialisedRelations.MobilizedIn;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "type")
	@JsonSubTypes(
			{@Type(value = InvolvedIn.class, name = "InvolvedIn") ,
			@Type(value = MobilizedIn.class, name = "MobilizeIn") ,
			})
public interface Relation extends Predicate
{
	public abstract ModelObject getSubject();	
	public abstract RelationTable getRelationTable();
}
