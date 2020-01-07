package com.b.gpshelperbreda.directions;


import android.Manifest;
import android.annotation.SuppressLint;
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

import com.b.gpshelperbreda.Notifications;
import com.b.gpshelperbreda.R;
import com.google.android.material.snackbar.Snackbar;

public class LocationTracker extends Service implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private LocationTrackerListener listener;
    private final Context context;
    private Notifications notifications;
    private static final int PERMISSION_REQUEST_CODE = 2000;
    private static final String PERMISSION_STRING = android.Manifest.permission.ACCESS_FINE_LOCATION;


    public LocationTracker(Context context, LocationTrackerListener listener, LocationManager locationManager, Notifications notifications) {
        this.context = context;
        this.listener = listener;
        this.locationManager = locationManager;
        this.notifications = notifications;
        setLocationListener();
        trackLocation();
    }

    private void setLocationListener() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    (Activity) this.context,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    0
//            );
//        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                listener.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d("Debug", "Provider enabled");
                notifications.sendNotification(context.getResources().getString(R.string.gps_on), context.getResources().getString(R.string.gps_enabled), Notifications.NOTIFICATION_GPS);
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("Debug", "Provider disabled");
                notifications.sendNotification(context.getResources().getString(R.string.gps_off), context.getResources().getString(R.string.gps_disabled), Notifications.NOTIFICATION_GPS);
            }
        };
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
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, locationListener);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
