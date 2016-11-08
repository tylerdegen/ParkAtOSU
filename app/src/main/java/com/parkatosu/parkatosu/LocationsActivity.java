package com.parkatosu.parkatosu;

import android.database.sqlite.SQLiteException;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

public class LocationsActivity extends Fragment implements View.OnClickListener {
    private ParkingDBHelper dh;

    @Override
    public void onClick(View view) {
        try{
            dh.createDataBase();
        }catch (IOException ioe){
            throw new Error ("Unable to create database");
        }

        try{
            dh.openDataBase();
        }catch (SQLiteException sqle){
            throw sqle;
        }
    }
}
