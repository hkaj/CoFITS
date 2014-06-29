package mcb.mcbandroid.agents;

import java.util.UUID;

import mcb.guimessage.GUIMessage;
import mcb.model.Brainstorming;

import jade.core.AID;

public interface GuiInterface {
	public void handleEvent(GUIMessage command);
	public void selectMeeting(AID aid);
	public Brainstorming getModel();
	public void lock(UUID modelId);
	public void unlock(UUID modelId);
}