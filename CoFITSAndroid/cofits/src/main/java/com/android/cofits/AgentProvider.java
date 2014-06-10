package com.android.cofits;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by Sanaa on 6/3/14.
 */
public class AgentProvider {
        //implements PropertyChangeListener{

    private ClientAgent myAgent;
    private static AgentProvider agentProvider;
/*    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        myAgent = (ClientAgent) propertyChangeEvent.getNewValue();
    }*/

    public static AgentProvider getInstance(){
        if(agentProvider == null){
            agentProvider = new AgentProvider();
        }
        return agentProvider;
    }

    public ClientAgent getMyAgent() {
        return myAgent;
    }

    public void setMyAgent(ClientAgent myAgent) {
        this.myAgent = myAgent;
    }
}
