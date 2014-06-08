package SpecialisedRelations;

import DatabaseScheme.RelationTable;
import DatabaseScheme.TableMobilizedIn;
import ModelObjects.ModelObject;
import ModelObjects.Project;
import ModelObjects.Session;
import Requests.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class MobilizedIn implements Relation
{
	final Project subject;
	public MobilizedIn(@JsonProperty("subject")Project subject)
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
