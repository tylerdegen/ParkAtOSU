package com.parkatosu.parkatosu;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

    ParkingDBHelper park = new ParkingDBHelper(this);
        try{
            park.createDataBase();
        }catch (IOException e){
                throw new Error("Unable to create database");
        }


    Thread welcome = new Thread() {
        @Override
        public void run() {
            try {
                super.run();
                sleep(100);
            }catch (Exception e){

            }finally {

                    Intent i = new Intent(loading.this, LoginActivity.class);
                    startActivity(i);
                finish();
            }

        }

    };
        welcome.start();
    }
}
