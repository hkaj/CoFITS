package Tests;

import java.util.ArrayList;

import Requests.Filter;
import Requests.Predicate;

public class TestDocument
{

	public TestDocument()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ArrayList<Predicate> ap = new ArrayList<Predicate>();
		ap.add(new Filter("name","toto"));
		ap.add(new Filter("description","un joli document"));
		ap.add(new Filter("Date","aujourd'hui"));
		
		for(Predicate p : ap)
		{
			System.out.println(p.toString());
		}
	}

}
