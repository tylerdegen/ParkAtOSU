package com.parkatosu.parkatosu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    Thread welcome = new Thread() {
        @Override
        public void run() {
            try {
                super.run();
                sleep(10000);
            }catch (Exception e){

            }finally {

                    Intent i = new Intent(loading.this, MainActivity.class);
                    startActivity(i);
                finish();
            }

        }

    };
        welcome.start();
    }
}
