package ModelObjects;

import DatabaseScheme.ReferenceTable;


public abstract class ModelObject
{
	public ModelObject(){}

	public abstract String[] getKeyConditions();
	
	public abstract String[] getValue();
	
	public abstract String[] getKeyValues();
	
	public abstract ReferenceTable getReferenceTable();

}
