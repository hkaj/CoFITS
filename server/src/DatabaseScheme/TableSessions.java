package DatabaseScheme;

public class TableSessions implements ReferenceTable
{
	
	final private static String[] att ={"name","date"};
	final private static String[] keyAtt = {"name","date"};
	final private static TableSessions instance = new TableSessions();
	static public ReferenceTable getInstance(){return instance;};
	
	private TableSessions(){};
	
	@Override
	public String getName()
	{
		return "Sessions";
	}

	@Override
	public String[] getAttributes()
	{
		// TODO Auto-generated method stub
		return att;
	}

	@Override
	public String[] getKeyAttributes()
	{
		// TODO Auto-generated method stub
		return keyAtt;
	}

}
