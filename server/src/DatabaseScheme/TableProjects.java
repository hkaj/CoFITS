package DatabaseScheme;

public class TableProjects implements ReferenceTable
{

	final private static String[] att ={"name","description"};
	final private static String[] keyAtt = {"name"};
	final private static TableProjects instance = new TableProjects();
	static public ReferenceTable getInstance(){return instance;};
	
	private TableProjects(){};
	
	@Override
	public String getName()
	{
		return "Projects";
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
