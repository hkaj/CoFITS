package DatabaseScheme;

public class TableMobilizedIn implements RelationTable
{
	final private static String[] att ={"id","name","date"};
	final private static String[] keyAtt = {"id","name","date"};
	final private static TableMobilizedIn instance = new TableMobilizedIn();
	
	private TableMobilizedIn(){};
	
	static public ReferenceTable getInstance(){return instance;};
	@Override
	public String getName()
	{
		return "MobilizedIn";
	}

	@Override
	public String[] getAttributes()
	{
		return att;
	}

	@Override
	public String[] getKeyAttributes()
	{
		return keyAtt;
	}

	@Override
	public ReferenceTable[] getRelativeTables()
	{
		final ReferenceTable[] res = {TableDocuments.getInstance(), TableSessions.getInstance()};
		return res;
	}

}
