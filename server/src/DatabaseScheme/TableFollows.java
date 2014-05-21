package DatabaseScheme;

public class TableFollows implements RelationTable
{
	final static private String[] att ={"id","login"};
	final static private String[] keyAtt = {"id","login"};
	final static private TableFollows instance = new TableFollows();
	
	static public ReferenceTable getInstance(){return instance;};
	private TableFollows(){};
	
	public String getName()
	{
		return "Follows";
	}


	public String[] getAttributes()
	{
		return att;
	}

	public String[] getKeyAttributes()
	{
		return keyAtt;
	}

	@Override
	public ReferenceTable[] getRelativeTables()
	{
		final ReferenceTable[] res = {TableUsers.getInstance(), TableDocuments.getInstance()};
		return res;
	}

}
