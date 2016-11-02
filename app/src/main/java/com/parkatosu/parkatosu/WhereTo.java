package com.parkatosu.parkatosu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class WhereTo extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private LocationManager locationManager;
    private final static int DISTANCE_UPDATES = 1;
    private final static int TIME_UPDATES = 5;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean LocationAvailable;

    private static final String TAG = "WhereTo";

    private GoogleMap mMap;
    private Button mGoButton;
    private Button mUpdateLocButton;

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, WhereTo.class);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.activity_where_to);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoButton = (Button) findViewById(R.id.go_button);
        mGoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //start activity
                Intent i = new Intent(WhereTo.this, Directions.class);
                startActivity(i);

            }
        });

        mUpdateLocButton = (Button) findViewById(R.id.update_loc_button);
        mUpdateLocButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String message;
                if (checkPermission()){
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        message= "Address: " + Double.toString(location.getLatitude())
                                + " " + Double.toString(location.getLongitude());
                    } else {
                        message = "location null :/";
                    }
                }
                else{
                    requestPermission();
                    message = "permission requested";
                }
                Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
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
        String result = "";
        double latitude = 0;
        double longitude = 0;


        if (checkPermission()){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                result = "Address: " + Double.toString(location.getLatitude())
                        + " " + Double.toString(location.getLongitude());
            } else {
                result = "location null :/";
                Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
            }
        }
        else{
            requestPermission();
            result = "permission requested";
            Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
        }
        // Add a marker at Ohio Union
        //LatLng sydney = new LatLng(39.9977, -83.0086);
        LatLng userCoord = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(userCoord).title("Marker in Columbus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userCoord));
    }

    @Override public void onLocationChanged(Location location) {
        //monitor for location changes
        //location holds new location
    }

    //GPS tuned off
    //provider contains which provider was disabled
    @Override
    public void onProviderDisabled(String provider) {
        if (checkPermission()){
            locationManager.removeUpdates(this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onProviderEnabled(String provider){
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //autogeerated method stub
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            LocationAvailable = true;
            return true;
        } else {
            LocationAvailable = false;
            return false;
        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "This app uses location data! Please enable!", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    //monitor for permission changes
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * We are good, turn on monitoring
                     */
                    if (checkPermission()) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
                    } else {
                        requestPermission();
                    }
                } else {
                    /**
                     * No permissions, block out all activities that require a location to function
                     */
                    Toast.makeText(this, "Permission Not Granted.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
