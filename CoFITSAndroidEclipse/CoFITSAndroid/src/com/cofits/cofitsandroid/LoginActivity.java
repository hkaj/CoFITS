package com.cofits.cofitsandroid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;

public class LoginActivity extends ActionBarActivity {

    private EditText emailText, passwordText;
    private Button loginButton;
    private Logger logger = Logger.getJADELogger(((Object)this).getClass().getName());

    private ServiceConnection serviceConnection;
    private MicroRuntimeServiceBinder microRuntimeServiceBinder;

    private String email = "";
    private String pwd = "";

    private String agentName;
    private Properties profile;

    private static ClientAgent clientAgent;
    public static String USER_LOGIN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.login_email_field);
        passwordText = (EditText) findViewById(R.id.login_password_field);
        loginButton = (Button) findViewById(R.id.login_login_button);

        loginButton.setOnClickListener(loginListener);


       /* Button goToProjects = (Button) findViewById(R.id.button2);
        goToProjects.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/

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
                    clientAgent = null;
                    agentName = email;
                    profile = new Properties();

                    //Home
                    profile.setProperty("host", "172.29.38.140");

                    //UTC
                    //profile.setProperty("host", "172.25.25.0");

                    profile.setProperty("port", "2000");
                    start();

                    USER_LOGIN = email;
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

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
    private void start() {
        serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
                Log.v("jade_android", "Gateway successfully bound to MicroRuntimeService");
                startContainer();
            };

            public void onServiceDisconnected(ComponentName className) {
                microRuntimeServiceBinder = null;
                Log.v("jade_android", "Gateway unbound from MicroRuntimeService");
            }
        };
        Log.v("jade_android", "Binding Gateway to MicroRuntimeService...");
        bindService(new Intent(getApplicationContext(),
                MicroRuntimeService.class), serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    private void startContainer() {
        Log.v("jade_android", "startContainer()");
        if (!MicroRuntime.isRunning()) {
            microRuntimeServiceBinder.startAgentContainer(profile,
                    new RuntimeCallback<Void>() {
                        @Override
                        public void onSuccess(Void thisIsNull) {
                            Log.v("jade_android", "Successfully start of the container...");
                            startAgent();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.v("jade_android", "Failed to start the container..." + throwable.getMessage());
                        }
                    });
        } else {
            startAgent();
        }
    }

    private void startAgent() {
        Log.v("jade_android", "startAgent()");

        microRuntimeServiceBinder.startAgent(agentName,
                ClientAgent.class.getName(),
                new Object[] { getApplicationContext() },
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void thisIsNull) {
                        Log.v("jade_android", "Successfully start of the "
                                + ClientAgent.class.getName() + "...");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.v("jade_android", "Failed to start the "
                                + ClientAgent.class.getName() + "...");
                    }
                });
    }

    public void stop() {
        if (clientAgent != null) {
            //clientAgent.logOut();
        }
        Log.v("jade_android", "stopping jade runtime manager");
        if (microRuntimeServiceBinder == null) {
            Log.v("jade_android", "no runtime");
        } else {
            microRuntimeServiceBinder.stopAgentContainer(new RuntimeCallback<Void>() {
                @Override
                public void onSuccess(Void thisIsNull) {
                    Log.v("jade_android", "jade runtime manager now stopped");
                    clientAgent = null;
                }

                @Override
                public void onFailure(Throwable throwable) {

                    Log.v("jade_android","Failed to stop the "
                            + ClientAgent.class.getName()
                            + "..." + throwable.getMessage());
                }
            });
        }
        if (serviceConnection == null) {
            Log.v("jade_android", "no service connection");
        }else {
            serviceConnection = null;
        }
    }

    public ClientAgent getAndroidAgent() {
        if (clientAgent == null && microRuntimeServiceBinder != null){
            start();
        }
        return clientAgent;
    }

    public void setAndroidAgent(ClientAgent clientAgent) {
        LoginActivity.clientAgent = clientAgent;

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
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }

}
