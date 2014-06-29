package DocumentAgent;

import jade.core.behaviours.OneShotBehaviour;

import java.sql.Connection;
import java.util.ArrayList;

import Constants.RequestConstants;
import ModelObjects.Project;
import Requests.Predicate;
import Requests.SelectRequest;
import SpecialisedRelations.InvolvedIn;

public class TestBehaviour extends OneShotBehaviour
{
	Connection C;
	public TestBehaviour(Connection C)
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action()
	{
		ArrayList<Predicate> p = new ArrayList<Predicate>();
		p.add(new InvolvedIn(new Project("projet de test", "a project to test selectBehaviour.", "kajhaiss")));
		SelectRequest sr = new SelectRequest(RequestConstants.objectTypes.document, p.toArray(new Predicate[0]));
		this.myAgent.addBehaviour(new SelectBehaviour(sr, null));
	}

}
