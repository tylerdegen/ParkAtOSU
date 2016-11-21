package com.parkatosu.parkatosu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SetParkedActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private final static int DISTANCE_UPDATES = 1;
    private final static int TIME_UPDATES = 5;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean LocationAvailable;

    private Button mSetParkedButton;

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SetParkedActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_parked);
        LocationAvailable = false;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
        } else {
            requestPermission();
        }

        mSetParkedButton = (Button) findViewById(R.id.set_location_button);
        mSetParkedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // instantiate the location manager, note you will need to request permissions in your manifest
                //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // get the last know location from your location manager.
                String result ="initialized but never set";
                boolean permission = true;

                if (checkPermission()){
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        result = "Address: " + Double.toString(location.getLatitude())
                                + " " + Double.toString(location.getLongitude());
                    } else {
                        result = "location null :/";
                    }
                }
                else{
                    requestPermission();
                    result = "permission requested";
                }
                /*permission = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED;
                permission = ContextCompat.checkSelfPermission(SetParkedActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;*/

                /*
                permission = ContextCompat.checkSelfPermission(SetParkedActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                if (permission) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    result = "Address: " + Double.toString(location.getLatitude())
                            + " " + Double.toString(location.getLongitude());
                } else {
                    ActivityCompat.requestPermissions(SetParkedActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    result = "GPS not enabled ):";
                }
                */
                /*
                try {
                    Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // now get the lat/lon from the location and do something with it.
                //nowDoSomethingWith(location.getLatitude(), location.getLongitude());
                    result = "Address: " + Double.toString(location.getLatitude())
                            + " " + Double.toString(location.getLongitude());

                } catch (SecurityException e){
                    result = "GPS not enabled ):";

                    //dialogGPS(this.getContext());
                }
                */
                Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();

            }
        });
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
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
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
