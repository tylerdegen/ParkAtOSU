package com.parkatosu.parkatosu;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends FragmentActivity implements View.OnClickListener {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, AccountActivity.class);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        AccountFragment accountFragment = new AccountFragment();
        // Install the Account fragment
        // For Android 3.0 and above comment out the line below
        FragmentManager fragmentManager = getSupportFragmentManager();
        // For Android 3.0 and above uncomment the line below
        // FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.accountdetails, accountFragment);
        fragmentTransaction.commit();

        // Initialize the Edit Account button
        View editAccount = (Button) findViewById(R.id.edit_account_button);
        editAccount.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_account_button:
                Intent i = AccountEditActivity.newIntent(AccountActivity.this);
                startActivity(i);
                break;
        }
    }

    // Removed because this creates a duplicate activity of MainActivity.class, which
    //allows for the MainActivity.class to be accessed after logout
//    public void onBackPressed(){
//        Intent refresh = new Intent(this, MainActivity.class);
//        startActivity(refresh);
//        this.finish();
//    }

}
