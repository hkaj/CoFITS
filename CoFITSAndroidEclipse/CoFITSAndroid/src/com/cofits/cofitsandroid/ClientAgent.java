package com.cofits.cofitsandroid;

import android.util.Log;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;


/**
 * Created by Sanaa on 6/1/14.
 */
public class ClientAgent extends Agent{

    public PropertyChangeSupport pcs;
    public ClientAgent(){

    }
    protected void setup() {
        super.setup();
        Log.v("jade_android", "Agent Android is running");

/*        pcs = new PropertyChangeSupport(this);
        pcs.firePropertyChange("myAgent",null,this);
        addPropertyChangeListener(AgentProvider.getInstance());*/
        AgentProvider.getInstance().setMyAgent(this);

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @return The server AID
     * Search for the server AID in DFAgent
     */
    public AID searchServer() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        DFAgentDescription[] result = null;
        AID rec = null;

        sd.setType("ReceiverAgent");
        sd.setName("receiver");
        template.addServices(sd);

        try {
            result = DFService.search(this, template);
            if (result.length > 0){
                int index = new Random().nextInt(result.length);
                rec = result[index].getName();
            }
        }catch (FIPAException e) {
            e.printStackTrace();
        }

        return rec;
    }

}

