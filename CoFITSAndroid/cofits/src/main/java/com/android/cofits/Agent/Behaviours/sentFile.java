package com.android.cofits.Agent.Behaviours;


import com.android.cofits.tools.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by antho on 03/06/14.
 */

public class sentFile extends OneShotBehaviour {

    String filepath = new String();
    File toUse;

    public SentFile( String fp){

        URI uri = null;
        try {
            uri = new URI(fp);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        toUse = new File(uri);

        try
        {
            FileOutputStream fileOut = new FileOutputStream("/tmp/toSend.tmp");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(toUse);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/toSend.tmp");
        }catch(IOException i)
        {
            i.printStackTrace();
        }

        uri = null;
        try {
            uri = new URI("/tmp/toSend.tmp");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        toUse = new File(uri);

        filepath = fp;
    }

    public void action(){
        /* file need to be serialized*/

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(toUse);
            byte fileContent[] = new byte[(int)toUse.length()];
            fin.read(fileContent);

            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            String content ="{ \"action\": \" ADD \" , \"login\": \""+ User.name +"\", \"project_name\": \"" + User.project + "\", \"file_name\": \""+ toUse.getName() +"\", \"file\": \"" +/*theSerializedFile*/ toUse+ "\", \"checksum\":\""+ /*theCheckSum*/toUse+"\" \"session\": \""+User.session+"\" }";
            message.setContent(content);
            message.setByteSequenceContent(fileContent);
            message.addUserDefinedParameter("file-name", toUse.getName());
            message.addReceiver(user.serverAgent);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        }
        finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
    }

}