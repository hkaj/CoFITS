package SpecialisedRelations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import DatabaseScheme.RelationTable;
import DatabaseScheme.TableFollows;
import DatabaseScheme.TableInvolvedIn;
import ModelObjects.ModelObject;
import ModelObjects.Session;
import Requests.Relation;

public final class InvolvedIn implements Relation
{
	final Session subject;
	public InvolvedIn(@JsonProperty("subject")Session subject)
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
