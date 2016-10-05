package com.parkatosu.parkatosu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mAccountButton;
    private Button mGoParkButton;
    private Button mSetParkedButton;
    private Button mLogoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountButton = (Button) findViewById(R.id.account_button);
        mGoParkButton = (Button) findViewById(R.id.park_button);
        mSetParkedButton = (Button) findViewById(R.id.set_parked_button);
        mLogoutButton = (Button) findViewById(R.id.logout_button);

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
                Intent i = LogoutActivity.newIntent(MainActivity.this);
                startActivity(i);
            }
        });

    }
}
