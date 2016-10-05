package com.parkatosu.parkatosu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LogoutActivity extends AppCompatActivity {
    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, LogoutActivity.class);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
    }
}
