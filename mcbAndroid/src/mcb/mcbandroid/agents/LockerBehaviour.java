package mcb.mcbandroid.agents;

import java.util.UUID;

import mcb.message.LockProtocol.Lock;

import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior to request a lock and react to replies
 */
public class LockerBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;
	private final UUID objectId;
	
	/**
	 * Request a lock
	 * @param agent					The PersonalAgent
	 * @param objectId				Id of the object to lock
	 * @param failureCallback		Callback to call in case of failure
	 */
	public LockerBehaviour(PersonalAgent agent, UUID objectId) {
		super(agent);
		this.objectId = objectId;
	}
	
	@Override
	public void action() {
		myAgent.send( new Lock(objectId).toACL(((PersonalAgent) myAgent).getMeetingAgentAID()));
	}
}
