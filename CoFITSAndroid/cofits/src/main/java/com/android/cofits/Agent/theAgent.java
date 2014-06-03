package com.android.cofits.Agent;


import com.android.cofits.tools.user;

/**
 * Created by antho on 03/06/14.
 */
public class theAgent {

    public theAgent(){
        user.serverAgent = findDocumentAgent()[0].getName();
    }

    public DFAgentDescription[] findDocumentAgent()
    {
        final DFAgentDescription template = new DFAgentDescription();
        final ServiceDescription sd = new ServiceDescription();
        sd.setType(DocumentAgent.serviceType);
        sd.setName(DocumentAgent.serviceName);
        template.addServices(sd);findDocumentAgent()[0].getName()
        try
        {
            DFAgentDescription[] result = DFService.search(this, template);
            if(result != null && result.length > 0)
            {
                return result;
            }else
            {
                System.err.println("Aucun destinataire trouvé");
            }
        }catch(FIPAException fe)
        {
            fe.printStackTrace();
        }
        return null;
    }
}
