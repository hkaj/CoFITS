package Tests;

import java.sql.Timestamp;
import java.util.ArrayList;

import Constants.RequestConstants;
import ModelObjects.Document;
import ModelObjects.Session;
import Requests.Filter;
import Requests.Predicate;
import Requests.Relation;
import Requests.SelectRequest;
import SpecialisedRelations.FollowedBy;
import SpecialisedRelations.MobilizedIn;

public class TestSelectRequest
{

	public TestSelectRequest()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	
	public static void t1()
	{
		ArrayList<Predicate> p = new ArrayList<Predicate>();
		p.add(new Filter("name","la vie des moineaux"));
		p.add(new MobilizedIn(new Session("projet_ia04",new Timestamp(System.currentTimeMillis()).toString())));
		SelectRequest sr = new SelectRequest(RequestConstants.objectTypes.document, p.toArray(new Predicate[0]));
		System.out.println(sr.toSQL());
	}
	
	public static void t2()
	{
		ArrayList<Predicate> p = new ArrayList<Predicate>();
		p.add(new Filter("login","narichar"));
		p.add(new MobilizedIn(new Session("projet_ia04",new Timestamp(System.currentTimeMillis()).toString())));
		SelectRequest sr = new SelectRequest(RequestConstants.objectTypes.user, p.toArray(new Predicate[0]));
		System.out.println(sr.toSQL());
	}
	
//	public static void t3()
//	{
//		ArrayList<Predicate> p = new ArrayList<Predicate>();
//		p.add(new Filter("login","narichar"));
//		p.add(new Filter("firstname","Richard"));
//		p.add(new Follows(new Document(0, "pdf", "la vie des pigeons", "pdf", null)));
//		SelectRequest sr = new SelectRequest(RequestConstants.objectTypes.user, p.toArray(new Predicate[0]));
//		System.out.println(sr.toSQL());
//	}
	
	public static void main(String[] args)
	{
		t1();
		t2();
//		t3();
	}

}
