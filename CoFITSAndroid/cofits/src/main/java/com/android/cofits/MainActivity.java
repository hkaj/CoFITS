package com.android.cofits;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.logging.Level;

import jade.android.AndroidHelper;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.MicroRuntime;
import jade.core.NotFoundException;
import jade.core.Profile;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class MainActivity extends ActionBarActivity {

    private EditText emailText, passwordText;
    private Button loginButton;
    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    private ServiceConnection serviceConnection;
    private MicroRuntimeServiceBinder microRuntimeServiceBinder;

    private String email = "";
    private String pwd = "";

    static final int CHAT_REQUEST = 0;
    static final int SETTINGS_REQUEST = 1;

    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        myReceiver = new MyReceiver();

        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction("cofits.LOGIN_SUCCESS");
        registerReceiver(myReceiver, loginFilter);

        IntentFilter loginFailFilter = new IntentFilter();
        loginFailFilter.addAction("cofits.LOGIN_FAIL");
        registerReceiver(myReceiver, loginFailFilter);

        setContentView(R.layout.activity_main);

        emailText = (EditText) findViewById(R.id.login_email_field);
        passwordText = (EditText) findViewById(R.id.login_password_field);
        loginButton = (Button) findViewById(R.id.login_login_button);

        loginButton.setOnClickListener(loginListener);


        Button goToProjects = (Button) findViewById(R.id.button2);
        goToProjects.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProjectsActivity.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    private View.OnClickListener loginListener = new View.OnClickListener() {
        public void onClick(View v) {

            email = emailText.getText().toString();
            pwd = passwordText.getText().toString();

            if (checkLogin(email, pwd)) {
                Toast.makeText(getApplicationContext(), "utilisateur ", Toast.LENGTH_SHORT).show();

                try {
                    SharedPreferences settings = getSharedPreferences(
                            "jadeMainPrefsFile", 0);
                    String host = settings.getString("defaultHost", "");
                    String port = settings.getString("defaultPort", "");
                    Log.d("gg", host + ":" + port + "...");
                    startJade(emailText.getText().toString(), host, port, agentStartupCallback);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Unexpected exception connecting to the server!");
                }
            } else
                Toast.makeText(getApplicationContext(), "erreur utilisateur", Toast.LENGTH_SHORT).show();
        }

    };

    private static boolean checkLogin(String email, String pwd) {
        return email.equals("sanaa") && pwd.equals("sanaa");
    }

    private RuntimeCallback<AgentController> agentStartupCallback = new RuntimeCallback<AgentController>() {
        @Override
        public void onSuccess(AgentController agent) {
        }

        @Override
        public void onFailure(Throwable throwable) {
            logger.log(Level.INFO, "Email already in use!");
        }
    };

    public void startJade(final String nickname, final String host,
                          final String port,
                          final RuntimeCallback<AgentController> agentStartupCallback) {

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
        profile.setProperty(Profile.LOCAL_PORT, "2000");

        Log.d("profile", profile.getProperty(Profile.LOCAL_HOST));
        if (microRuntimeServiceBinder == null) {
            serviceConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
                    logger.log(Level.INFO, "Gateway successfully bound to MicroRuntimeService");
                    startContainer(nickname, profile,agentStartupCallback);
                };

                public void onServiceDisconnected(ComponentName className) {
                    microRuntimeServiceBinder = null;
                    logger.log(Level.INFO, "Gateway unbound from MicroRuntimeService");
                }
            };
            logger.log(Level.INFO, "Binding Gateway to MicroRuntimeService...");
            bindService(new Intent(getApplicationContext(),
                    MicroRuntimeService.class), serviceConnection,
                    Context.BIND_AUTO_CREATE);
        } else {
            logger.log(Level.INFO, "MicroRumtimeGateway already binded to service");
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
                new Object[] { getApplicationContext() },
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void thisIsNull) {
                        logger.log(Level.INFO, "Successfully start of the "
                                + ClientAgent.class.getName() + "...");
                        try {
                            agentStartupCallback.onSuccess(MicroRuntime.getAgent(nickname));
                        } catch (ControllerException e) {
                            // Should never happen
                            agentStartupCallback.onFailure(e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        logger.log(Level.SEVERE, "Failed to start the "
                                + ClientAgent.class.getName() + "...");
                        agentStartupCallback.onFailure(throwable);
                    }
                });
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logger.log(Level.INFO, "Received intent " + action);
            if (action.equalsIgnoreCase("cofits.LOGIN_SUCCESS")) {
                ShowDialog("Login succeeded!");
                //login();
                Log.d("MainActivity", "Login succeeded!");
            }
            if (action.equalsIgnoreCase("cofits.LOGIN_FAIL")) {
                ShowDialog("Login failed");
                if (MicroRuntime.isRunning()) {
                    try {
                        MicroRuntime.killAgent("Conn-" + email);
                        //MicroRuntime.detach();
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void ShowDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickLogin(View view) {
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
