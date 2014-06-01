package DocumentAgent;

import Requests.UploadRequest;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public final class UploadBehaviour extends OneShotBehaviour
{
	final UploadRequest request;
	public UploadBehaviour(UploadRequest request)
	{
		this.request = request;
	}

	public UploadBehaviour(Agent a, UploadRequest request)
	{
		super(a);
		this.request = request;
	}

	@Override
	public void action()
	{
		this.request.exec();
	}

}
