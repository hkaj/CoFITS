package ClientAgent;

import jade.core.behaviours.FSMBehaviour;

public class DemoBehaviour extends FSMBehaviour {
	public void start(String state) {
		System.out.println("Schedule starts ...");
		forceTransitionTo(state);
		scheduleNext(false, 0);
	}
}
