package com.android.cofits.Agent.Behaviours;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by antho on 03/06/14.
 */

public class ackFile extends CyclicBehaviour {

    Activity CurrentActivity = new Activity();

    public ackFile(Activity act) {
        CurrentActivity = act;
    }


    public void action() {
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REFUSE),
                MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
        ACLMessage message = myAgent.receive(mt);

        if (message != null) {

            int performative = message.getPerformative();

            if (performative == ACLMessage.REFUSE) {
                Toast.makeText(CurrentActivity.getApplicationContext(), "Impossible", Toast.LENGTH_SHORT).show();
            } else if (performative == ACLMessage.CONFIRM){
                Toast.makeText(CurrentActivity.getApplicationContext(), "C'est fait", Toast.LENGTH_SHORT).show();
            }


        }

    }
}