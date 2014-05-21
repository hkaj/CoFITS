package DatabaseScheme;

public class TableUsers implements ReferenceTable
{
	final private static String[] att ={"login","lastname","firstname"};
	final private static String[] keyAtt = {"login"};
	final private static TableUsers instance = new TableUsers();
	static public ReferenceTable getInstance(){return instance;};
	private TableUsers(){};
	
	@Override
	public String getName()
	{
		return "Users";
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
}
