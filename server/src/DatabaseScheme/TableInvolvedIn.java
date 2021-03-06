package DatabaseScheme;

public class TableInvolvedIn implements RelationTable
{
	final private static String[] att ={"login","project","admin"};
	final private static String[] keyAtt = {"login","project"};
	final private static TableInvolvedIn instance = new TableInvolvedIn();
	static public ReferenceTable getInstance(){return instance;};
	
	private TableInvolvedIn(){};
	@Override
	public String getName()
	{
		return "InvolvedIn";
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
		final ReferenceTable[] res = {TableUsers.getInstance(), TableProjects.getInstance()};
		return res;
	}
}
