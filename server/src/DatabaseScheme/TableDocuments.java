package DatabaseScheme;

public class TableDocuments implements ReferenceTable
{
	
<<<<<<< HEAD
	final private static String[] att ={"id","name","type", "owner", "last_modified"};
=======
	final private static String[] att ={"id","name","type", "owner"};
>>>>>>> agentAndroid
	final private static String[] keyAtt = {"id"};
	
	final private static TableDocuments instance = new TableDocuments();
	private TableDocuments(){};
	
	static public ReferenceTable getInstance(){return instance;};
	
	@Override
	public String getName()
	{
		return "Documents";
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
