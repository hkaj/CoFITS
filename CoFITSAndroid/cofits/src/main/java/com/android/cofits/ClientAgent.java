package com.android.cofits;

import android.content.Context;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

/**
 * Created by Sanaa on 6/1/14.
 */
public class ClientAgent extends Agent{

    private ACLMessage message;

    private Context context;

    protected void setup() {

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            if (args[0] instanceof Context) {
                context = (Context) args[0];
            }
        }
        message = new ACLMessage(ACLMessage.SUBSCRIBE);


    }

}

