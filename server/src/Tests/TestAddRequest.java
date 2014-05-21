package Tests;

import ModelObjects.Document;
import Requests.AddRequest;

public class TestAddRequest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Document subject = new Document(0, "le journal du lundi", "pdf");
		AddRequest req = new AddRequest(subject);
		
		System.out.println(req.toSQL());
	}

}
