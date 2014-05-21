package Tests;

import java.io.IOException;
import java.util.ArrayList;

import Constants.RequestConstants;
import Requests.Filter;
import Requests.Predicate;
import Requests.SelectRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJackson
{

	public TestJackson()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// cf http://stackoverflow.com/questions/19765240/jackson-unexpected-token-value-string-expected-field-name-when-deserializ
		ArrayList<Predicate> p = new ArrayList<Predicate>();
		p.add(new Filter("name","la vie des moineaux"));
		SelectRequest sr = new SelectRequest(RequestConstants.objectTypes.document,p.toArray(new Predicate[0]));
		final String s = sr.toJSON();
		System.out.println(s);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		SelectRequest sr2;
		try
		{
			sr2 = mapper.readValue(s, SelectRequest.class);
			System.out.println(sr2.toString());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
