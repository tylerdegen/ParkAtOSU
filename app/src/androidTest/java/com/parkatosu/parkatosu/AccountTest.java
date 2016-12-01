package com.parkatosu.parkatosu;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Debug;
import android.test.ActivityInstrumentationTestCase2;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;
import android.util.Log;

import com.parkatosu.parkatosu.DatabaseHelper;
/**
 * Created by ajmcs on 11/30/2016.
 */
public class AccountTest extends ActivityInstrumentationTestCase2<AccountActivity>{
    private AccountActivity mAccountActivity;
    private Context mContext;
    private DatabaseHelper mDatabaseHelper;
    public AccountTest(){

        super("com.parkatosu.parkatosu.AccountActivity", AccountActivity.class);
    }

    protected void setUp() throws Exception	{
        super.setUp();
        mAccountActivity = getActivity();
        mContext = mAccountActivity.getApplicationContext();
        setActivityInitialTouchMode(false);
    }

    public void testPreconditions(){
        assertNotNull(mAccountActivity);
    }

    public void testDropDB(){
        assertTrue(mContext.deleteDatabase("ACCOUNTS"));
        Log.d("testDropDB", "passed.");

    }

    public void testCreateDB(){
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        //assertTrue(db.isOpen());
        //db.close();
        Log.d("testCreateDB", "passed.");
    }


    protected void tearDown() throws Exception{
        mAccountActivity.finish();
    }

}
