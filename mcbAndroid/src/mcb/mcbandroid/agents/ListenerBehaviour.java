package mcb.mcbandroid.agents;

import mcb.communication.MessageTemplate2;
import mcb.debug.Logger;
import mcb.message.Message;
import mcb.message.ConnectionProtocol.ConnectEnd;
import mcb.message.ConnectionProtocol.ConnectFinalize;
import mcb.message.LockProtocol.LockFailure;
import mcb.message.ModelChangeProtocol.AffiliateObject;
import mcb.message.ModelChangeProtocol.CreateObject;
import mcb.message.ModelChangeProtocol.DeleteObject;
import mcb.message.ModelChangeProtocol.SelectObject;
import mcb.message.ModelChangeProtocol.UnselectObject;
import mcb.message.ModelChangeProtocol.UpdateModel;
import mcb.message.ModelChangeProtocol.UpdateObject;
import mcb.util.MessageApplicator;
import jade.content.lang.sl.ParseException;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Behavior listening to message from a connected meeting
 * 
 * It listens for message matching the following templates:
 * - ListenerBehaviour.ModelUpdateMessageTemplate 
 * - ListenerBehaviour.MeetingUpdateMessageTemplate
 * - ListenerBehaviour.AMSFailureTemplate
 */
public class ListenerBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = -6288380085409240617L;
	
	/**
	 * Match all message which are update to the model
	 */
	public static final MessageTemplate ModelUpdateMessageTemplate = 
			MessageTemplate2.or(
				CreateObject.template,
				UpdateModel.template,
				AffiliateObject.template,
				SelectObject.template,
				UnselectObject.template,
				UpdateObject.template,
				DeleteObject.template
			);
	
	/**
	 * Match all message which change the meeting state
	 */
	public static final MessageTemplate MeetingUpdateMessageTemplate = 
			MessageTemplate2.or(
				ConnectFinalize.template,
				ConnectEnd.template
			);
	
	/**
	 * Match message signaling a communication failure
	 */
	public static final MessageTemplate AMSFailureTemplate = 
			MessageTemplate2.and(
					MessageTemplate.MatchSender(new AID("ams", AID.ISLOCALNAME)),
					MessageTemplate.MatchPerformative(ACLMessage.FAILURE)
			);
	
	/**
	 * Match message signaling a lock failure
	 */
	public static final MessageTemplate LockFailureTemplate = 
			LockFailure.template
		;
	
	protected PersonalAgent myAgent;

	/**
	 * Wait for message and react to them
	 */
	public void action() {
		if ( this.myAgent == null )
			this.myAgent = (PersonalAgent) super.myAgent;
		
		ACLMessage message = null;
		Message m = null;
		
		if ( (message = myAgent.receive(AMSFailureTemplate)) != null ) {
			myAgent.notifyGui(PersonalAgent.EVENT_AMSFAILURE, message.getContent());
		}
		else if ( (message = myAgent.receive(LockFailureTemplate)) != null ) {
			myAgent.notifyGui(PersonalAgent.EVENT_LOCKFAILURE, message.getContent());
		}
		else if ( (message = myAgent.receive(ModelUpdateMessageTemplate)) != null ) {
			try {
				Message command = Message.parse(message);
				MessageApplicator.ApplyMessage(myAgent.getModel(), command);
			} catch (ParseException e) {
				Logger.error("message", "parsing error", e);
			}
		}
		else if ( (message = myAgent.receive(MeetingUpdateMessageTemplate)) != null ) {
			try {
				m = Message.parse(message);
			} catch (ParseException e) {
				Logger.error("message", "parsing error", e);
			}
			
			if ( m instanceof ConnectFinalize ) {
				myAgent.setModel( ((ConnectFinalize)m).getModel() );
			}
			else if ( m instanceof ConnectEnd ) {
				myAgent.notifyGui(PersonalAgent.EVENT_UNSUBSCRIBED, "Meeting ended");
			}
		}
		else {
			block();
		}
	}
}
