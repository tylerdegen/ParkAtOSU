package com.parkatosu.parkatosu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DATABASE_NAME = "ParkAtOSU";
    private static final int DATABASE_VERSION = 1;
    private static final String ACCOUNTS = "Accounts";
    private static final String LOCATIONS = "Locations";
    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;
    private static final String INSERT = "insert into " + ACCOUNTS + "(name, password, address, permits, park_lat, park_long, ss_notify, gd_notify) values (?, ?, ?, ?, ?, ?, ?, ?)";

    public DatabaseHelper(Context context) {
        this.context = context;
        ParkAtOSUOpenHelper openHelper = new ParkAtOSUOpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
    }

    public long insertAccount(String name, String password) {
        this.insertStmt.bindString(1, name);
        this.insertStmt.bindString(2, password);
        return this.insertStmt.executeInsert();
    }

    public void updateValue(ContentValues newValues, String whereClause, String[] whereArgs) {
        this.db.update("ACCOUNTS", newValues, whereClause, whereArgs);
    }

    public void deleteAll(String table) {

        this.db.delete(table, null, null);
    }

    public List<String> selectAll(String username, String password, String table) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = this.db.query(table, new String[]{"name", "password"}, "name = '" + username + "' AND password= '" + password + "'", null, null, null, "name desc");
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public List<String> selectProps(String username, String table) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = this.db.query(table, new String[]{"address", "permits", "park_lat", "park_long", "ss_notify", "gd_notify"}, "name = '" + username + "'", null, null, null,null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
                list.add(cursor.getString(1));
                list.add(String.valueOf(cursor.getFloat(2)));
                list.add(String.valueOf(cursor.getFloat(3)));
                list.add(cursor.getString(4));
                list.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public String selectPermit(String username, String table){
        Cursor cursor = this.db.query(table, new String[]{"permits"}, "name = '" + username + "'", null, null, null,null);
        String permit = null;
        if (cursor.moveToFirst()) {
            permit = cursor.getString(0);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return permit;
    }

    private static class ParkAtOSUOpenHelper extends SQLiteOpenHelper {

        ParkAtOSUOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + ACCOUNTS + "(id INTEGER PRIMARY KEY, name TEXT, password TEXT, address TEXT, permits TEXT, park_lat REAL, park_long REAL, ss_notify TEXT, gd_notify TEXT)");
            db.execSQL("CREATE TABLE " + LOCATIONS + "(id INTEGER PRIMARY KEY, name TEXT, lat FLOAT, long FLOAT, permit TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w("Example", "Upgrading database; this will drop and recreate the tables.");
            db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS);
            onCreate(db);
        }
    }

    //creates database of parking lots

}
