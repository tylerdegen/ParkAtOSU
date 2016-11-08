package com.parkatosu.parkatosu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    private Button mSetDestButton;
    private EditText mHeadedDest;

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

        mHeadedDest = (EditText) findViewById(R.id.headed_address);

        mSetDestButton = (Button) findViewById(R.id.set_dest_button);
        mSetDestButton.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               String headed_address = mHeadedDest.getText().toString();
               //Toast.makeText(getApplication(), headed_address, Toast.LENGTH_LONG).show();
               Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
               String toast_message = "default";
               try {
                   List<Address> addresses = null;
                   addresses = geocoder.getFromLocationName(headed_address, 5);
                   double dest_lat = addresses.get(0).getLatitude();
                   double dest_long = addresses.get(0).getLongitude();
                   toast_message = "Address: " + Double.toString(dest_lat)
                           + " " + Double.toString(dest_long);
                   updateMap(dest_lat,dest_long);
               }
               catch(IOException e){
                   toast_message = e.toString();
               }
               Toast.makeText(getApplication(), toast_message, Toast.LENGTH_LONG).show();
           }
        });

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

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LatLng userCoord = new LatLng(latitude,longitude);
                        mMap.clear();

                        mMap.addMarker(new MarkerOptions().position(userCoord).title("Marker in Columbus"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userCoord));
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

        /*
        mUpdateLocButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String result ="default";

                if (checkPermission()){
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        result = "Address: " + Double.toString(location.getLatitude())
                                + " " + Double.toString(location.getLongitude());
                    } else {
                        result = "location null on updateLoc :/";
                        Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    requestPermission();
                    result = "permission requested";
                    Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
                }

                /*

                Location location = getLocation(locationManager);

                if (location != null){
                    message= "Address: " + Double.toString(location.getLatitude())
                            + " " + Double.toString(location.getLongitude());

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    updateMap(latitude, longitude);
                }
                else{
                    message="location null on updateLoc click :/";
                }

                Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
            }
        });

    }

*/

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
                result = "location null on getMapReady :/";
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

    private Location getLocation(LocationManager locationManager){
        String result = "";
        Location location = null;
        if (checkPermission()){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                //latitude = location.getLatitude();
                //longitude = location.getLongitude();

                result = "Address: " + Double.toString(location.getLatitude())
                        + " " + Double.toString(location.getLongitude());
            } else {
                result = "location null on getLocation :/";
                //Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
            }
        }
        else{
            requestPermission();
            result = "permission requested";
            //Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();


        return location;
    }

    private void updateMap(double latitude, double longitude){

        LatLng userCoord = new LatLng(latitude,longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userCoord).title("Marker in Columbus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userCoord));
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
