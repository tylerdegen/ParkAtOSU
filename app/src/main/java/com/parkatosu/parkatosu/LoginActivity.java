package com.parkatosu.parkatosu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static com.parkatosu.parkatosu.DatabaseHelper.*;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity implements android.view.View.OnClickListener {
    private DatabaseHelper dh;
    private EditText user;
    private EditText pass;
    private final static String OPT_NAME="name";
    private boolean showLogOutMessage;
    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, LoginActivity.class);

    }
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        FragmentManager.enableDebugLogging(true);
        setContentView(R.layout.activity_login);
        showLogOutMessage = false;
        Intent i = getIntent();
        if (i != null) {
            showLogOutMessage = i.getBooleanExtra("logout", false);
            if (showLogOutMessage) {
                Toast.makeText(this, "Logged Out.", Toast.LENGTH_SHORT).show();
            }
        }
        user=(EditText)findViewById(R.id.username);
        pass=(EditText)findViewById(R.id.password);
        android.view.View btnLogin=(Button)findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(this);
        android.view.View btnNewUser=(Button)findViewById(R.id.email_sign_in_button_register);
        if (btnNewUser!=null) btnNewUser.setOnClickListener(this);

        checkDatabase();

    }

    private void checkDatabase(){

        ParkingDBHelper park = new ParkingDBHelper(this);
        try{
            park.createDataBase();
        }catch (IOException e){
                throw new Error("Unable to create database");
        }
        try{

        }catch(SQLException sqle) {
            throw sqle;
        }
    }

    private void checkLogin(){
        String username=this.user.getText().toString();
        String password=this.pass.getText().toString();
        this.dh=new DatabaseHelper(this);
        List<String> names=this.dh.selectAll(username,password, "ACCOUNTS");
        if (names.size() > 0){
            SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor=settings.edit();
            editor.putString(OPT_NAME, username);
            editor.commit();

            startActivity(new Intent(this, MainActivity.class));
            finish();

        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("Login Failed")
                    .setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .show();

        }

    }

    public void onClick(android.view.View v) {
        switch (v.getId()){
            case R.id.email_sign_in_button:
                checkLogin();
                break;
            case R.id.email_sign_in_button_register:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }


}

