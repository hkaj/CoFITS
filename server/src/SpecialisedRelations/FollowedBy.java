package SpecialisedRelations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import DatabaseScheme.RelationTable;
import DatabaseScheme.TableFollows;
import ModelObjects.ModelObject;
import ModelObjects.User;
import Requests.Relation;

public final class FollowedBy implements Relation
{
	User subject;
	@JsonCreator
	public FollowedBy(@JsonProperty("subject")User subject)
	{
		this.subject=subject;
	}

	@Override @JsonIgnore
	public RelationTable getRelationTable()
	{
		return (RelationTable)TableFollows.getInstance();
	}

	@Override
	public ModelObject getSubject()
	{
		return this.subject;
	}
}
