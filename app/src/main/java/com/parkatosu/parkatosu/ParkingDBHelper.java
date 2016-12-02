package com.parkatosu.parkatosu;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by costa on 11/7/16.
 */
public class ParkingDBHelper extends SQLiteOpenHelper {

        private static final String DB_PATH = "/data/data/com.parkatosu.parkatosu/databases/";
        private static final String DB_Name = "Parking.db";
        private static SQLiteDatabase myDataBase;
        private final Context myContext;

        public ParkingDBHelper(Context context) {
            super(context, DB_Name, null, 1);
            this.myContext = context;
        }



        public void createDataBase() throws IOException {
            try {
                boolean dbExist = checkDataBase();
                if (dbExist) {
                    //do nothing
                } else {
                    this.getReadableDatabase();

                    copyDataBase();

                }
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }


        private boolean checkDataBase() {
            SQLiteDatabase checkDB = null;

            try {
                String myPath = DB_PATH + DB_Name;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLiteException e) {
                //database doesnt exist
            }

            if (checkDB != null) {

                checkDB.close();
            }
            return checkDB != null ? true : false;
        }

        private void copyDataBase() throws IOException {
            try {
                InputStream myInput = myContext.getAssets().open(DB_Name);
                String outFileName = DB_PATH + DB_Name;
                OutputStream myOutput = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
            catch (Exception e){

            }
        }

        public static Cursor select(String query,String [] args) throws SQLException{
            openDataBase();
            return myDataBase.rawQuery(query,args);
        }

        public static void openDataBase() throws SQLiteException {

            String myPath = DB_PATH + DB_Name;
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        }

        @Override
        public synchronized void close() {

            if (myDataBase != null) {
                myDataBase.close();
            }

            super.close();

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        }
    }


