package com.cofits.cofitsandroid;

import org.json.JSONException;
import org.json.JSONObject;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by Sanaa on 6/4/14.
 */
public class DeleteFileBehaviour extends OneShotBehaviour {
    JSONObject jO;
    ClientAgent clientAgent = AgentProvider.getInstance().getMyAgent();


    @Override
    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        System.out.println(" ----- DeleteFileBehaviour started ----- "  );

        try {
            jO = new JSONObject();
            jO.put("action", "REMOVE");
            jO.put("user_login", LoginActivity.USER_LOGIN);
            jO.put("project_name", ProjectsFragment.PROJECT_NAME);
            jO.put("session_name", SessionsFragment.SESSION_NAME);
            jO.put("file_name", FilesFragment.FILE_NAME);

            message.setContent(jO.toString());
            System.out.println(" Delete file : jO messageToserver= " + jO );

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        message.addReceiver(clientAgent.searchServer());
        myAgent.send(message);
    }
}
