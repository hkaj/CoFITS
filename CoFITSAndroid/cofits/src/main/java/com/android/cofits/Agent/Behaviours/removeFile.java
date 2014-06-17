package com.android.cofits.Agent.Behaviours;

import com.android.cofits.tools.user;

/**
 * Created by antho on 03/06/14.
 */

public class removeFile extends OneShotBehaviour {
    String FileName = new String();

    public RemoveFile(String FN){
        FileName = FN;
    }

    public void action(){
        /* file need to be serialized*/
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        String content ="{ \"action\": \" ADD \" , \"login\": \""+ User.name +"\", \"project_name\": \"" + User.project + "\", \"file_name\": \""+ FileName +"\" }";
        message.setContent(content);
        message.addReceiver(user.serverAgent);
    }
}