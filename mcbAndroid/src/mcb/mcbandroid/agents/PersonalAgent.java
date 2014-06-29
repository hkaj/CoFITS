package mcb.mcbandroid.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.UUID;

import mcb.debug.Logger;
import mcb.guimessage.GUIMessage;
import mcb.message.LockProtocol.Unlock;

import android.content.Context;
import android.content.Intent;
import mcb.model.BaseObject;
import mcb.model.Brainstorming;
import mcb.model.Session;
import mcb.util.AgentNamingConvention;
import mcb.util.CleanerBehaviour;

public class PersonalAgent extends Agent implements GuiInterface, PropertyChangeListener {

	private static final long serialVersionUID = -3506643594298053540L;

	public final static String INTENT 					= "mcb.pa";
	public final static String INTENT_MODEL_UPDATE 		= "mcb.pa.model";
	public final static String EVENT 					= "e";
	public final static String PARAM 					= "p";
    public static final String INTENT_EXTRA 			= "extra";
	
	/**
	 * Event fired when the meeting list is ready.
	 * The new event is a HashMap of AID to Topic.
	 */
	public final static String EVENT_MEETING_LIST 		= "meeting-list-available";
	
	/**
	 * Event fired when no meeting were found in the JADE container.
	 */
	public final static String EVENT_NO_MEETING_FOUND 	= "meeting-list-unavailable";
	
	/**
	 * Event fired when the connection to the JADE container is closed.
	 */
	public final static String EVENT_DISCONNECTED 		= "disconnected";
	
	/**
	 * Event fired when the connection to the JADE container is ready.
	 */
	public final static String EVENT_CONNECTED 			= "connected";
	
	/**
	 * Event fired when the meeting agent cannot be contacted.
	 */
	public final static String EVENT_AMSFAILURE 		= "amserror";
	
	/**
	 * Event fired when there is an error in the JADE system.
	 */
	public final static String EVENT_ERROR 				= "error";
	
	/**
	 * Event fired when the connection to the meeting is ready.
	 */
	public final static String EVENT_SUSCRIBED 			= "suscribed";
	
	/**
	 * Event fired when the meeting end.
	 */
	public final static String EVENT_UNSUBSCRIBED 		= "unsuscribed";
	
	/**
	 * Event fired when the meeting refused an action because of a lock.
	 */
	public final static String EVENT_LOCKFAILURE 		= "lockfailure";

	
	protected Brainstorming model = null;
	protected Session session = null;
	protected AID meetingAgentAID = null;

	protected Context context = null;
	
	protected ConnectBehaviour connectBehaviour;
	
	protected void setup() {
		context = (Context) getArguments()[0];
		model = (Brainstorming) getArguments()[1];
		session = (Session) getArguments()[2];
		
		registerO2AInterface(GuiInterface.class, this);
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentNamingConvention.getPersonnalDFService());
		sd.setName(getLocalName());

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) { 
			fe.printStackTrace();
		}
		
		connectBehaviour = new ConnectBehaviour();
		addBehaviour(connectBehaviour);
		addBehaviour(new ListenerBehaviour());
		addBehaviour(new CleanerBehaviour(this, 1000, 2000));
		
		if ( model != null )
			model.addListener(this);
	}
	
	//
	// getters and setters
	//
	
	public AID getMeetingAgentAID() {
		return meetingAgentAID;
	}
	
	public Brainstorming getModel() {
		return model;
	}
	
	public void setModel(Brainstorming model) {
		this.model = model;
		this.model.addListener(this);
	}

	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
			
	//
	// notify gui (handle [Agent >> Gui] communication)
	//
	
	public void notifyGui(String event, Serializable parameter) {
		Intent broadcast = new Intent();
		broadcast.setAction(PersonalAgent.INTENT);
		broadcast.putExtra(EVENT, event);
		broadcast.putExtra(PARAM, parameter);
		context.sendBroadcast(broadcast);
	}
	public void notifyGui(PropertyChangeEvent event) {
		Intent broadcast = new Intent();
		broadcast.setAction(PersonalAgent.INTENT_MODEL_UPDATE);
		broadcast.putExtra(EVENT, event);
		context.sendBroadcast(broadcast);
	}

	//
	// GuiInterface (handle [Gui >> Agent] communication)
	//
	
	public void handleEvent(GUIMessage command) {
		if ( command.isConvertible() ) {
			ACLMessage message = command.toAgentMessage(model).toACL(meetingAgentAID);
			send(message);
		}
	}

	public void selectMeeting(AID aid) {
		connectBehaviour.select(aid);
		this.meetingAgentAID = aid;
	}

	public void propertyChange(PropertyChangeEvent event) {
		Logger.debug("pa", "got a PropertyChangeEvent: " + event.getPropertyName());
		
		if ( event.getPropertyName().equals(BaseObject.EVENT_ADD_CHILD) ) {
			UUID cid = (UUID) event.getNewValue();
			BaseObject bo = model.get(cid);
			if ( bo != null ) {
				bo.addListener(this);
				Logger.debug("pa", "added listener to the new child: " + cid);
			}
		}
		
		notifyGui(event);
	}

	public void lock(UUID modelId) {
		addBehaviour(new LockerBehaviour(this, modelId));
	}

	public void unlock(UUID modelId) {
		send(new Unlock(modelId).toACL(meetingAgentAID));
	}
}
