package DatabaseScheme;

public interface ReferenceTable
{
	public abstract String getName();
	public abstract String[] getAttributes();
	public abstract String[] getKeyAttributes();
}
