package com.parkatosu.parkatosu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mAccountButton;
    private Button mGoParkButton;
    private Button mSetParkedButton;
    private Button mLogoutButton;
    private Button mNotificationsButton;
    private DatabaseHelper dh;
    String permit;
    private int REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dh = new DatabaseHelper(this);
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(this);
        String[] usernameArg = new String[1];
        usernameArg[0] = settings.getString("name","");

        permit = dh.selectPermit(usernameArg[0], "ACCOUNTS");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountButton = (Button) findViewById(R.id.account_button);
        mGoParkButton = (Button) findViewById(R.id.park_button);
        mSetParkedButton = (Button) findViewById(R.id.set_parked_button);
        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mNotificationsButton = (Button) findViewById(R.id.notifications_button);

        mAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = AccountActivity.newIntent(MainActivity.this);
                startActivity(i);
            }
        });

        mGoParkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = WhereTo.newIntent(MainActivity.this);
                startActivity(i);
            }
        });

        mSetParkedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = SetParkedActivity.newIntent(MainActivity.this);
                startActivity(i);
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = LoginActivity.newIntent(MainActivity.this);
                i.putExtra("logout",true);
                startActivity(i);
            }
        });
        mSetParkedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = SetParkedActivity.newIntent(MainActivity.this);
                startActivity(i);
            }
        });

        mNotificationsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (permit != null) {
                    FragmentManager fm = getSupportFragmentManager();
                    NotificationsDialogFragment ndf = new NotificationsDialogFragment();
                    ndf.show(fm, "notifications");
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("No Permit Found");
                    alertDialog.setMessage("Please specify a permit for your account in order to receive notifications.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        //Intent connectivity = new Intent(this, ConnectivityReceiver.class);
        //sendBroadcast(connectivity);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setMessage("No internet connection. Please press OK to go to Settings.");
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
