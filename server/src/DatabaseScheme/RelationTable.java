package DatabaseScheme;

public interface RelationTable extends ReferenceTable
{
	ReferenceTable[] getRelativeTables();
}
