package Tests;

import java.sql.Timestamp;

import ModelObjects.Document;
import ModelObjects.Session;
import Requests.LinkRequest;
import Requests.Relation;
import SpecialisedRelations.MobilizedIn;

public abstract class TestLinkRequest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		final Document subject = new Document(0, "le journal du lundi", "pdf");
		final Relation r = new MobilizedIn(new Session("projet_ia04",new Timestamp(System.currentTimeMillis()).toString()));
		LinkRequest req = new LinkRequest(subject,r);
		System.out.println(req.toSQL());
	}

}
