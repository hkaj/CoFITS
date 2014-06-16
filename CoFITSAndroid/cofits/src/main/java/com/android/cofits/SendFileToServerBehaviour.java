package com.android.cofits;

import org.json.JSONException;
import org.json.JSONObject;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by Sanaa on 6/3/14.
 */
public class SendFileToServerBehaviour extends OneShotBehaviour {
    ClientAgent clientAgent = AgentProvider.getInstance().getMyAgent();

    JSONObject jO;

    @Override
    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        System.out.println(" ----- SendFileToServerBehaviour started ----- "  );

        try {
            jO = new JSONObject();
            jO.put("action", "ADD");
            jO.put("user_login", LoginActivity.USER_LOGIN);
            jO.put("project_name", ProjectsFragment.PROJECT_NAME);
            jO.put("session_name", SessionsFragment.SESSION_NAME);
            jO.put("file_name",AddFileActivity.FILE_NAME);
            jO.put("file_content",AddFileActivity.FILE_TO_STRING);

            message.setContent(jO.toString());
            System.out.println(" jO messageToserver= " + jO );

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        message.addReceiver(clientAgent.searchServer());
        myAgent.send(message);
    }
}
