package Constants;

public abstract class RequestConstants
{
	public enum objectTypes {document, project, session, user};
	public enum requestTypes {select, represent, upload, download, create, link, quit};
	public static String SELECT_PROJECT_CID = "project-cid";
	public static String SELECT_SESSION_CID = "session-cid";
	static final public String documentAgentDirectory = "./DocumentsAgent/";
	static final public String clientAgentDirectory = "./DocumentsClient/";
}
