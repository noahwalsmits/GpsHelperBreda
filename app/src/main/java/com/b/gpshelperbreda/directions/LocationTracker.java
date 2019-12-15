package com.b.gpshelperbreda.directions;


import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationTracker extends Service implements ActivityCompat.OnRequestPermissionsResultCallback{

    protected LocationManager locationManager;
    protected LocationListener listener;
    private final Context context;
    private static final int PERMISSION_REQUEST_CODE = 2000;
    private static final String PERMISSION_STRING = android.Manifest.permission.ACCESS_FINE_LOCATION;


    public LocationTracker(Context context){
        this.context = context;
    }

    private void setLocationListner(){

       String locationProvider = locationManager.GPS_PROVIDER;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    (Activity) this.context,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    0
            );
        }


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d("Debug", "Provider enabled");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("Debug", "Provider disabled");
            }
        };
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                trackLocation();
            } else {
                Toast.makeText(this.context, "LocationTracking turned off!", Toast.LENGTH_SHORT);
            }
        }
    }

    private void trackLocation() {
        if (ContextCompat.checkSelfPermission(this, PERMISSION_STRING)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this.context, new String[]{PERMISSION_STRING}, PERMISSION_REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener); //TODO configure
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
