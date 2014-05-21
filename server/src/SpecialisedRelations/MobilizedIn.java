package SpecialisedRelations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import DatabaseScheme.RelationTable;
import DatabaseScheme.TableInvolvedIn;
import DatabaseScheme.TableMobilizedIn;
import ModelObjects.ModelObject;
import ModelObjects.Session;
import Requests.Relation;

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
