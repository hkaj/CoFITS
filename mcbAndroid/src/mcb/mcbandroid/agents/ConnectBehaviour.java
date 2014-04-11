package mcb.mcbandroid.agents;


import jade.content.lang.sl.ParseException;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREInitiator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import mcb.debug.Logger;
import mcb.message.Message;
import mcb.message.ConnectionProtocol.Connect;
import mcb.message.ConnectionProtocol.ConnectReply;
import mcb.message.ConnectionProtocol.GetMeetingInformations;
import mcb.message.ConnectionProtocol.MeetingInformations;
import mcb.model.Topic;
import mcb.util.AgentNamingConvention;
import mcb.util.DFServiceV2;

/**
 * Behavior used to establish a connection with a meeting
 * 
 * It will 
 * - ask all MeetingAgent for their topic
 * - let the user select one
 * - subscribe to the selected topic
 */
public class ConnectBehaviour extends FSMBehaviour {

	private static final long serialVersionUID = -8052575027544857137L;
	
	protected PersonalAgent myAgent;
	protected AID selectedMeetingAgent = null;
	
	final protected HashMap<AID, Topic> meetingList = new HashMap<AID, Topic>();
	protected int expectedReplies = 0;
	
	public static final String STATE_FIND = "find";
	public static final String STATE_ASK_USER = "ask-user";
	public static final String STATE_WAIT_USER = "wait-user";
	public static final String STATE_SUBSCRIBE = "subscribe";
	public static final String STATE_WAIT_CONFIRMS = "wait-confirms";
	public static final String STATE_END = "end";
	
	
	public void select(AID aid){
		Logger.debug("PersonalAgent", "select meeting: " + aid);
		selectedMeetingAgent = aid;
		forceTransitionTo(STATE_SUBSCRIBE);
		scheduleNext(true, 0);
		restart();
	}
	
	public void gotReply() {
		expectedReplies--;
		Logger.debug("PersonalAgent", "ConnectBehaviour got a reply");
		
		if ( expectedReplies <= 0 ) {
			PersonalAgent myAgent = (PersonalAgent) this.myAgent;
			
			if ( meetingList.isEmpty() ) {
				myAgent.notifyGui(PersonalAgent.EVENT_NO_MEETING_FOUND, null); 
				forceTransitionTo(STATE_END);
				scheduleNext(true, 0);
				restart();
			}
			else {
				myAgent.notifyGui(PersonalAgent.EVENT_MEETING_LIST, meetingList); 
			}
		}
	}
	
	public void onStart() {
		this.myAgent = (PersonalAgent) super.myAgent;
		final String conversationId = "meeting-connection-round-"+new Date().getTime();
		
		ACLMessage listMeetingMessage = DFService.createRequestMessage(
				myAgent, 
				myAgent.getDefaultDF(), 
				FIPAManagementOntology.SEARCH,
				DFServiceV2.makeSearchDFD(AgentNamingConvention.getMeetingDFService()), 
				DFServiceV2.makeSearchConstraints(0, -1L));
		final SimpleAchieveREInitiator finder = new SimpleAchieveREInitiator(myAgent, listMeetingMessage, getDataStore());
		
		
		OneShotBehaviour askUser = new OneShotBehaviour() {
			private static final long serialVersionUID = 6633620357949407486L;
			public void action() {
				@SuppressWarnings("unchecked")
				ACLMessage result = ((Vector<ACLMessage>) getDataStore().get(finder.ALL_RESULT_NOTIFICATIONS_KEY)).get(0);
				ArrayList<AID> meetings = new ArrayList<AID>();
				try {
					meetings = DFServiceV2.decodeResult(result);
				} catch (FIPAException e) {
					// @TODO Auto-generated catch block
					Logger.error("PersonalAgent", e);
				}
				
				// ask for topics
				expectedReplies = meetings.size() + 1;
				
				for ( AID aid : meetings ) {
					ACLMessage request = new GetMeetingInformations().toACL(aid);
					myAgent.addBehaviour(new SimpleAchieveREInitiator(myAgent, request) {
						private static final long serialVersionUID = 1L;
						
						protected void handleInform(ACLMessage msg) {
							try {
								MeetingInformations mi = Message.parse(msg).as(MeetingInformations.class);
								if ( mi.getTopic() != null && mi.getTopic().getTopic() != null ) {
									meetingList.put(msg.getSender(), mi.getTopic());
								}
								gotReply();
							} catch (ClassCastException e) {
								Logger.error("PersonalAgent", e);
							} catch (ParseException e) {
								Logger.error("PersonalAgent", e);
							}
						};
						
						protected void handleAgree(ACLMessage msg) {
							gotReply();
						};
						
						protected void handleFailure(ACLMessage msg) {
							gotReply();
						};
						
						protected void handleRefuse(ACLMessage msg) {
							gotReply();
						};
					});
				}
				
				gotReply();
			}
		};
		askUser.setDataStore(getDataStore());
		
		CyclicBehaviour wait = new CyclicBehaviour() {
			private static final long serialVersionUID = 7218019388543392996L;
			public void action() { 
				Logger.debug("PersonalAgent", "waiting");
				block();
			}
		};
		
		

		OneShotBehaviour sendSubscribe = new OneShotBehaviour() {
			private static final long serialVersionUID = 5895462054764851600L;
			public void action() {
				Logger.debug("PersonalAgent", "sendSubscribe called");
				ACLMessage msg = new Connect(ConnectBehaviour.this.myAgent.getSession()).toACL(selectedMeetingAgent);
				msg.setConversationId(conversationId);
				myAgent.send(msg);
				Logger.debug("PersonalAgent", "subscribe sent");
			}
		};
		
		Behaviour receiveConfirms = new Behaviour() {
			private static final long serialVersionUID = -5841714501500818591L;
			protected boolean received = false;

			public boolean done() {
				return received;
			}
			
			@Override
			public void action() {
				PersonalAgent myAgent = (PersonalAgent) this.myAgent;
				MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
				ACLMessage msg = myAgent.receive(mt);
				if(msg == null){ 
					block(); 
					return; 
				}
				try{
					ConnectReply cr = Message.parse(msg).as(ConnectReply.class);
					myAgent.setModel(cr.getModel());
					received = true;
					Logger.debug("PersonalAgent", "model received");
					myAgent.notifyGui(PersonalAgent.EVENT_SUSCRIBED, null);
				}catch (Exception e) {
					e.printStackTrace();
					// @TODO
				}
			}
		};
		
		OneShotBehaviour end = new OneShotBehaviour() {
			private static final long serialVersionUID = 425790221265387600L;

			public void action() {
			}
		};

		
		registerFirstState(finder, STATE_FIND);
		registerState(askUser, STATE_ASK_USER);
		registerState(wait, STATE_WAIT_USER);
		registerState(sendSubscribe, STATE_SUBSCRIBE);
		registerState(receiveConfirms, STATE_WAIT_CONFIRMS);
		registerLastState(end, STATE_END);

		registerDefaultTransition(STATE_FIND, STATE_ASK_USER);
		registerDefaultTransition(STATE_ASK_USER, STATE_WAIT_USER);
		registerDefaultTransition(STATE_SUBSCRIBE, STATE_WAIT_CONFIRMS);
		registerDefaultTransition(STATE_WAIT_CONFIRMS, STATE_END);
	}
}
