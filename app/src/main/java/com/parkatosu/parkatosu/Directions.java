package com.parkatosu.parkatosu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class Directions extends FragmentActivity implements OnMapReadyCallback {
/*
    private static class DbHelper extends SQLiteOpenHelper{

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " TEXT NOT NULL, " +
                    KEY_HOTNESS + " TEXT NOT NULL);");
            // how do i exec the sql file and get the data into this DB table?
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS" + RECORD_TABLE);
            onCreate(db);
        }
    }
*/
    private GoogleMap mMap;
    private Button mDoneButton;

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, WhereTo.class);
        return i;
    }

    double longitude = 0;
    double latitude = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





        mDoneButton = (Button) findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Directions.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            double longitude = extras.getDouble("longitude");
            double latitude = extras.getDouble("latitude");

            // Add a marker from intent and move the camera
            LatLng current_loc = new LatLng(latitude, longitude);

            //have a method to query database given a latlng and return closest latlng
            LatLng destination = closestLoc(current_loc);

            mMap.addMarker(new MarkerOptions().position(destination).title("destination"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination,16));

        }
        else {
            //THIS IS A FAILURE CASE, SHOULD NOT OCCUR
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

    //use latlng to better couple for developers
    public static double LatLngDist(LatLng latlng1, LatLng latlng2){
        double x1 = latlng1.latitude;
        double x2 = latlng2.latitude;
        double y1 = latlng1.longitude;
        double y2 = latlng2.longitude;

        double distance_squared = (x1 - x2)*(x1 - x2) + (y1-y2)*(y1-y2);
        double distance = Math.sqrt(distance_squared);

        return distance;
    }

    //returns closest location given current location
    public static LatLng closestLoc(LatLng latlng_current){
        String [] permitLevel = AccountFragment.getPermitLevel();
        String query = null;
        if (permitLevel[0].equals("C")) {
            query = "SELECT lat, long FROM lots WHERE permit_level = 'C' ";
        }
        if (permitLevel[0].equals("B")) {
            query = "SELECT lat, long FROM lots WHERE permit_level = 'C' UNION SELECT lat, long FROM lots WHERE permit_level = 'B'";
        }
        if (permitLevel[0].equals("A")) {
            query = "SELECT lat, long FROM lots WHERE permit_level = 'C' UNION SELECT lat, long FROM lots WHERE permit_level = 'B' UNION SELECT lat, long FROM lots WHERE permit_level = 'A'";
        }

        Cursor cursor = ParkingDBHelper.select(query,null);
        LatLng[]  coordinates = new LatLng [cursor.getCount()];
        int j = 0;
        while(cursor.moveToNext()){
            coordinates[j] = new LatLng(cursor.getFloat(0),cursor.getFloat(1));
            j++;
        }
//      used for testing
//        //qatar
//        LatLng test = new LatLng(25.3548, 51.1839);
//        //buffalo
//        LatLng test2 = new LatLng (42.8864, -78.8784);
//        //cleveland
        LatLng test3 = new LatLng (41.4993, -81.6944);
            LatLng closest = test3;
            double closest_dist = LatLngDist(test3, latlng_current);
//
//
//        //this is where you'd have an array of latlngs from the database
//
//        LatLng[] coordinates;
//        int i = 0;
//        coordinates = new LatLng[3];
//        coordinates[0] = test;
//        coordinates[1] = test2;
//        coordinates[2] = test3;

        double test_dist;

        for (int i = 0; i < coordinates.length; i++){
            test_dist = LatLngDist(coordinates[i], latlng_current);
            if (LatLngDist(coordinates[i], latlng_current) < closest_dist ){
                closest = coordinates[i];
                closest_dist = test_dist;
            }
        }
        
        return closest;

    }
}
