package mcb.mcbandroid.utils;

import jade.android.AndroidHelper;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.AID;
import jade.core.MicroRuntime;
import jade.core.Profile;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.beans.PropertyChangeListener;
import java.util.UUID;
import java.util.logging.Level;

import mcb.guimessage.GUIMessage;
import mcb.guimessage.ObjectManipulation.AffiliateObject;
import mcb.guimessage.ObjectManipulation.DeleteObject;
import mcb.guimessage.ObjectManipulation.SelectObject;
import mcb.guimessage.ObjectManipulation.TransferObject;
import mcb.guimessage.ObjectManipulation.UnselectObject;
import mcb.guimessage.ObjectManipulation.UpdateObject;
import mcb.mcbandroid.Application;
import mcb.mcbandroid.agents.GuiInterface;
import mcb.mcbandroid.agents.PersonalAgent;
import mcb.model.BaseObject;
import mcb.model.Brainstorming;
import mcb.model.Cluster;
import mcb.model.PostIt;
import mcb.model.Session;
import mcb.model.User;
import mcb.util.AgentNamingConvention;
import mcb.util.i18nString;
import mcb.util.i18nString.Language;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Handler for working with a meeting
 * @TODO put message a waiting list during the establishment of the connection
 */
public class MeetingHandler implements HandlerInterface {
	
	protected Application app;      

	protected Logger logger = Logger.getJADELogger(this.getClass().getName());
	protected MicroRuntimeServiceBinder microRuntimeServiceBinder = null;
	protected ServiceConnection serviceConnection = null;
	protected String localName = null;
	
	protected Session session = null;
	
	public MeetingHandler(Application app, String localName, String host, int port) {
		this.session = new Session();
		this.app = app;
		this.localName = AgentNamingConvention.getPersonnalName(localName);

		/**
		 *  @FIXME this is a hack
		 *  should not do so, but when quitting the application, the container is kept alive..
		 */
		this.localName = AgentNamingConvention.getPersonnalName(UUID.randomUUID().toString());

		session.addUser(app.getUser());
		doConnect(host,port);
	}
	
	//
	//
	// agent handling
	//
	//

	public void doDisconnect() {
		if ( microRuntimeServiceBinder == null ) return;
		
		microRuntimeServiceBinder.stopAgentContainer(new RuntimeCallback<Void>() {
			@Override
			public void onSuccess(Void nothing) {
	            sendSuccessToUI(PersonalAgent.EVENT_DISCONNECTED);				
	            microRuntimeServiceBinder = null;
			}
			@Override
			public void onFailure(Throwable e) {
	            sendSuccessToUI(PersonalAgent.EVENT_DISCONNECTED);				
				//sendErrorToUI(new Exception("unable to disconnect : " + e.getMessage()));			
                microRuntimeServiceBinder = null;
            }
		});
	}
	
	private RuntimeCallback<AgentController> agentStartupCallback = new RuntimeCallback<AgentController>() {
        @Override
        public void onSuccess(AgentController agent) {
            sendSuccessToUI(PersonalAgent.EVENT_CONNECTED);				
        }

        @Override
        public void onFailure(Throwable throwable) {
	        sendErrorToUI(throwable);
        }
    };

    private synchronized void doConnect(final String host, final int port){
        logger.log(Level.INFO, "doConnect called");
      
        final Properties profile = new Properties();
        profile.setProperty(Profile.MAIN_HOST, host);
        profile.setProperty(Profile.MAIN_PORT, String.valueOf(port));
        profile.setProperty(Profile.MAIN, Boolean.FALSE.toString());
        profile.setProperty(Profile.JVM, Profile.ANDROID);
        if (AndroidHelper.isEmulator()) {
            profile.setProperty(Profile.LOCAL_HOST, AndroidHelper.LOOPBACK);
	    } else {
	        profile.setProperty(Profile.LOCAL_HOST, AndroidHelper.getLocalIPAddress());
	    }
        profile.setProperty(Profile.LOCAL_PORT, "2000");

        if (microRuntimeServiceBinder == null) {
            serviceConnection = new ServiceConnection() {            	
		        public void onServiceConnected(ComponentName className, IBinder service) {
		            microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
					logger.log(Level.INFO, "Gateway successfully bound to MicroRuntimeService");
		            startContainer(localName, profile, agentStartupCallback);
		        };
	
	            public void onServiceDisconnected(ComponentName className) {
					logger.log(Level.INFO, "Gateway unbound from MicroRuntimeService");
	                microRuntimeServiceBinder = null;
	            }
            };
			logger.log(Level.INFO, "Binding Gateway to MicroRuntimeService...");
			if ( app.bindService(new Intent(app.getApplicationContext(),MicroRuntimeService.class), serviceConnection, Context.BIND_AUTO_CREATE) == false ) {
    			logger.log(Level.SEVERE, "Binding Gateway to MicroRuntimeService failed !");
            }
	    } else {
			logger.log(Level.INFO, "MicroRumtimeGateway already binded to service");
            startContainer(localName, profile, agentStartupCallback);
	    }
    }
    
    private void startContainer(final String localName, Properties profile, final RuntimeCallback<AgentController> agentStartupCallback) {
       logger.log(Level.INFO, "startContainer called");
       if (!MicroRuntime.isRunning()) {
            microRuntimeServiceBinder.startAgentContainer(profile,
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void thisIsNull) {
                    	logger.log(Level.INFO, "Successfully start of the container...");
                    	startAgent(localName, agentStartupCallback);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
						logger.log(Level.SEVERE, "Failed to start the container...");
                        sendErrorToUI(throwable);
                    }
                });
        } else {
            startAgent(localName, agentStartupCallback);
        }
    }
    
    private void startAgent(final String nickname, final RuntimeCallback<AgentController> agentStartupCallback) {
        logger.log(Level.INFO, "startAgent called");
    	microRuntimeServiceBinder.startAgent(nickname, PersonalAgent.class.getName(), new Object[] { app.getApplicationContext(), new Brainstorming(), session },
    		new RuntimeCallback<Void>() {
                @Override
                public void onSuccess(Void thisIsNull) {
                    try {
						logger.log(Level.INFO, "Successfully start of the PA ...");
                    	agentStartupCallback.onSuccess(MicroRuntime.getAgent(nickname));
                    } catch (ControllerException e) {
                        agentStartupCallback.onFailure(e);
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
					logger.log(Level.SEVERE, "Failed to start the PA ...");
                    agentStartupCallback.onFailure(throwable);
                }
            });
    }
	
    public synchronized void sendErrorToUI(Throwable e){
        Intent broadcast = new Intent(PersonalAgent.INTENT);
        broadcast.putExtra(PersonalAgent.EVENT, PersonalAgent.EVENT_ERROR);
        broadcast.putExtra(PersonalAgent.PARAM, e.getMessage());
        app.getApplicationContext().sendBroadcast(broadcast);
    }

    public synchronized void sendSuccessToUI(String event){
        Intent broadcast = new Intent(PersonalAgent.INTENT);
		broadcast.putExtra(PersonalAgent.EVENT, event);
		app.getApplicationContext().sendBroadcast(broadcast);
    }
    
	//
	//
	// interface
	//
	//
    

	public void addListener(PropertyChangeListener pcl) {
		// do nothing
		// PersonalAgent listen to the model change and notify the GUI though Intent
	}
		
	public void addObject(BaseObject object) {
		TransferObject msg = new TransferObject(object);
		handleEvent(msg);
	}

	public UUID createPostIt(User author, i18nString title, i18nString content, String tags) {
		PostIt postit = new PostIt(author);
		postit.setTitle(title);
		postit.setContent(content);
		postit.setColor(author.getColor());
		TransferObject msg = new TransferObject(postit);
		
		if ( handleEvent(msg) )
			return postit.getId();
		
		return null;
	}
	
	public void updatePostIt(User user, UUID id, Language language, String title, String content, String tags) {
		PostIt postit = (PostIt) getModel().get(id);
		postit.getTitle().set(language, title);
		postit.getContent().set(language, content);
		UpdateObject msg = new UpdateObject(postit);
		handleEvent(msg);
	}

	public UUID createCluster(User author, i18nString title) {
		Cluster cluster = new Cluster(author);
		cluster.setContent(title);
		cluster.setColor(author.getColor());
		TransferObject msg = new TransferObject(cluster);
		handleEvent(msg);
		return cluster.getId();
	}

	public void updateCluster(User user, UUID id, Language language, String content) {
		Cluster cluster = (Cluster) getModel().get(id);
		cluster.getContent().set(language, content);
		UpdateObject msg = new UpdateObject(cluster);
		handleEvent(msg);
	}

	public void removeObject(UUID id) {
		DeleteObject msg = new DeleteObject(id);
		handleEvent(msg);
	}

	public void unselect(User user, UUID id) {
		UnselectObject msg = new UnselectObject(id,user);
		handleEvent(msg);
	}

	public void select(User user, UUID id) {
		SelectObject msg = new SelectObject(id,user);
		handleEvent(msg);
	}

	public void affiliate(UUID id, UUID parentId) {
		AffiliateObject msg = new AffiliateObject(id, parentId);
		handleEvent(msg);

	}

	public void disaffiliate(UUID id, UUID parentId) {
		AffiliateObject msg = new AffiliateObject(id, null);
		handleEvent(msg);
	}

	public void lock(UUID modelId) {
		try {
			GuiInterface agentIface = MicroRuntime.getAgent(localName).getO2AInterface(GuiInterface.class);
			agentIface.lock(modelId);
		} catch (StaleProxyException e) {
			mcb.debug.Logger.error("Meeting", "StaleProxyException", e);
			sendErrorToUI(e);
		} catch (ControllerException e) {
			mcb.debug.Logger.error("Meeting", "ControllerException", e);
			sendErrorToUI(e);
		}
	}

	public void unlock(UUID modelId) {
		try {
			GuiInterface agentIface = MicroRuntime.getAgent(localName).getO2AInterface(GuiInterface.class);
			agentIface.unlock(modelId);
		} catch (StaleProxyException e) {
			mcb.debug.Logger.error("Meeting", "StaleProxyException", e);
			sendErrorToUI(e);
		} catch (ControllerException e) {
			mcb.debug.Logger.error("Meeting", "ControllerException", e);
			sendErrorToUI(e);
		}
	}

	// @TODO remove
	public final Brainstorming getModel() {
		try {
			GuiInterface agentIface = MicroRuntime.getAgent(localName).getO2AInterface(GuiInterface.class);
			return agentIface.getModel();
		} catch (StaleProxyException e) {
			mcb.debug.Logger.error("Meeting", "StaleProxyException", e);
			sendErrorToUI(e);
		} catch (ControllerException e) {
			mcb.debug.Logger.error("Meeting", "ControllerException", e);
			sendErrorToUI(e);
		}
		
		return new Brainstorming();
	}
	
	public void selectMeeting(AID aid) {
		try {
			GuiInterface agentIface = MicroRuntime.getAgent(localName).getO2AInterface(GuiInterface.class);
			agentIface.selectMeeting(aid);
		} catch (StaleProxyException e) {
			mcb.debug.Logger.error("Meeting", "StaleProxyException", e);
			sendErrorToUI(e);
		} catch (ControllerException e) {
			mcb.debug.Logger.error("Meeting", "ControllerException", e);
			sendErrorToUI(e);
		}
	}
	
	private boolean handleEvent(GUIMessage msg) {
		try {
			GuiInterface agentIface = MicroRuntime.getAgent(localName).getO2AInterface(GuiInterface.class);
			agentIface.handleEvent(msg);
			return true;
		} catch (StaleProxyException e) {
			mcb.debug.Logger.error("MeetingHandler", "StaleProxyException", e);
			sendErrorToUI(e);
		} catch (ControllerException e) {
			mcb.debug.Logger.error("MeetingHandler", "ControllerException", e);
			sendErrorToUI(e);
		}
		
		return false;
	}
}
