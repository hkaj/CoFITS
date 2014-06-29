package SpecialisedRelations;

import DatabaseScheme.RelationTable;
import DatabaseScheme.TableInvolvedIn;
import ModelObjects.ModelObject;
import ModelObjects.Project;
import Requests.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class InvolvedIn implements Relation
{
	final Project subject;
	public InvolvedIn(@JsonProperty("subject")Project subject)
	{
		this.subject=subject;
	}
	@Override @JsonIgnore
	public RelationTable getRelationTable()
	{
		return (RelationTable)TableInvolvedIn.getInstance();
	}
	@Override
	public ModelObject getSubject()
	{
		return this.subject;
	}
}
