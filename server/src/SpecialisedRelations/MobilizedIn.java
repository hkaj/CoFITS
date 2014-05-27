package SpecialisedRelations;

import DatabaseScheme.RelationTable;
import DatabaseScheme.TableMobilizedIn;
import ModelObjects.ModelObject;
import ModelObjects.Session;
import Requests.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class MobilizedIn implements Relation
{
	final Session subject;
	public MobilizedIn(@JsonProperty("subject")Session subject)
	{
		this.subject = subject;
	}
	@Override @JsonIgnore
	public RelationTable getRelationTable()
	{
		return (RelationTable)TableMobilizedIn.getInstance();
	}
	@Override
	public ModelObject getSubject()
	{
		return this.subject;
	}
}
