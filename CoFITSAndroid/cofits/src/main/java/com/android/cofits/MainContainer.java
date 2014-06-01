package com.android.cofits;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

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
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
/**
 * Created by Sanaa on 5/29/14.
 */
public class MainContainer extends Activity{

    private ServiceConnection serviceConnection;
    private MicroRuntimeServiceBinder microRuntimeServiceBinder;

    private String nickname = "";

    @Override
    public void onCreate(Bundle pSavedInstanceState) {

        super.onCreate(pSavedInstanceState);
        startJade(nickname, "172.29.38.140", "2000" );

    }

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
                    startContainer(nickname, profile);
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
            startContainer(nickname, profile);
        }
    }

    private void startContainer(final String nickname, final Properties profile) {}/*{

        if (!MicroRuntime.isRunning()) {
            final RuntimeService runtimeService = new RuntimeService();
            runtimeService.createMainAgentContainer(new RuntimeCallback<AgentContainerHandler>() {

                @Override
                public void onSuccess(AgentContainerHandler arg0) {
                    System.out.println("Successfully start of the container...");
                    runtimeService.createNewAgent(arg0, "CreatorAgent", CreatorAgent.class.getName(), null, new RuntimeCallback<AgentHandler>() {
                        @Override
                        public void onSuccess(AgentHandler agentHandler) {
                            runtimeService.startAgent(agentHandler, new RuntimeCallback<Void>() {

                                @Override
                                public void onFailure(Throwable arg0) {
                                    System.err.println( "Failed to start the CreatorAgent...");
                                }

                                @Override
                                public void onSuccess(Void arg0) {
                                    System.out.println("Successfully start of the CreatorAgent...");
                                }
                            });

                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            System.err.println( "Failed to start the CreatorAgent...");
                        } });



                    microRuntimeServiceBinder.startAgentContainer(profile,
                            new RuntimeCallback<Void>() {
                                @Override
                                public void onSuccess(Void thisIsNull) {
                                    System.out.println("Successfully start of the container...");

                                    startAgent(nickname);
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    System.err.println( "Failed to start the container...");
                                }
                            });

                }

                @Override
                public void onFailure(Throwable arg0) {
                }
            });

        } else {
            startAgent(nickname);
        }
    }*/

    private void startAgent(final String nickname) {
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
                                Toast.makeText(getApplicationContext(), "Connexion réussie", Toast.LENGTH_SHORT).show();


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
                                Toast.makeText(getApplicationContext(), "Connexion échouée", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }
}
