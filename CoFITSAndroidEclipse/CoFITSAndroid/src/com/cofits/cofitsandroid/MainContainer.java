package com.cofits.cofitsandroid;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import java.util.logging.Level;

import jade.android.AgentContainerHandler;
import jade.android.AgentHandler;
import jade.android.AndroidHelper;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.android.RuntimeService;
import jade.core.MicroRuntime;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
/**
 * Created by Sanaa on 5/29/14.
 */
public class MainContainer extends Activity{

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    private ServiceConnection serviceConnection;
    private MicroRuntimeServiceBinder microRuntimeServiceBinder;

    private String nickname = "";

    @Override
    public void onCreate(Bundle pSavedInstanceState) {

        super.onCreate(pSavedInstanceState);
        startJade(nickname, "172.29.38.140", "2000" );

    }
    private RuntimeCallback<AgentController> agentStartupCallback = new RuntimeCallback<AgentController>() {
        @Override
        public void onSuccess(AgentController agent) {
        }

        @Override
        public void onFailure(Throwable throwable) {
            logger.log(Level.INFO, "Nickname already in use!");
        }
    };

    public void startJade(final String nickname, final String host,
                          final String port) {

        final Properties profile = new Properties();
        profile.setProperty(Profile.MAIN_HOST, host);
        profile.setProperty(Profile.MAIN_PORT, port);
        profile.setProperty(Profile.MAIN, Boolean.TRUE.toString());
        profile.setProperty(Profile.JVM, Profile.ANDROID);

        if (AndroidHelper.isEmulator()) {
            // Emulator: this is needed to work with emulated devices
            profile.setProperty(Profile.LOCAL_HOST, AndroidHelper.LOOPBACK);
        } else {
            profile.setProperty(Profile.LOCAL_HOST,
                    AndroidHelper.getLocalIPAddress());
        }
        // Emulator: this is not really needed on a real device
        profile.setProperty(Profile.LOCAL_PORT, "1099");

        if (microRuntimeServiceBinder == null) {
            serviceConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
                    System.out.println("Gateway successfully bound to MicroRuntimeService");
                    startContainer(nickname, profile,agentStartupCallback);
                };

                public void onServiceDisconnected(ComponentName className) {
                    microRuntimeServiceBinder = null;
                    System.out.println("Gateway unbound from MicroRuntimeService");
                }
            };
            System.out.println("Binding Gateway to MicroRuntimeService...");
            bindService(new Intent(getApplicationContext(),
                    MicroRuntimeService.class), serviceConnection,
                    Context.BIND_AUTO_CREATE);
        } else {
            System.out.println( "MicroRumtimeGateway already binded to service");
            startContainer(nickname, profile,agentStartupCallback);
        }
    }

    private void startContainer(final String nickname, Properties profile,
                                final RuntimeCallback<AgentController> agentStartupCallback) {
        if (!MicroRuntime.isRunning()) {
            microRuntimeServiceBinder.startAgentContainer(profile,
                    new RuntimeCallback<Void>() {
                        @Override
                        public void onSuccess(Void thisIsNull) {
                            logger.log(Level.INFO, "Successfully start of the container...");
                            startAgent(nickname, agentStartupCallback);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            logger.log(Level.SEVERE, "Failed to start the container...");
                        }
                    });
        } else {
            startAgent(nickname, agentStartupCallback);
        }
    }

    private void startAgent(final String nickname,
                            final RuntimeCallback<AgentController> agentStartupCallback) {
        microRuntimeServiceBinder.startAgent(nickname,
                ClientAgent.class.getName(),
                new Object[] { this },
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void thisIsNull) {
                        System.out.println("Successfully start of the "
                                + ClientAgent.class.getName() + "...");
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Connexion r��ussie", Toast.LENGTH_SHORT).show();


                            }
                        });

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println("Failed to start the "
                                + ClientAgent.class.getName() + "...");
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Connexion ��chou��e", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }
}
