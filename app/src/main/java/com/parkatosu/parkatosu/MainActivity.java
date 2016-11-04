package com.parkatosu.parkatosu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
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
    private int REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        mNotificationsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager fm = getSupportFragmentManager();
                NotificationsDialogFragment ndf = new NotificationsDialogFragment();
                ndf.show(fm, "notifications");
            }
        });

    }

}
