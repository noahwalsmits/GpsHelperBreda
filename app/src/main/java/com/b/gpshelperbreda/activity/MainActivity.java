package com.b.gpshelperbreda.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.provider.ContactsContract;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.data.Database;
import com.b.gpshelperbreda.data.JsonParser;
import com.b.gpshelperbreda.data.Route;
import com.b.gpshelperbreda.data.RouteFactory;
import com.b.gpshelperbreda.data.Waypoint;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Route route;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        database = new Database(this);
        if (!database.isTableFilled()){
            new JsonParser(this).parseJson("waypoints.json");
        }

        RouteFactory routeFactory = new RouteFactory(this);

        route = routeFactory.getRouteFromId(1);
    }

    /**
     * Called when the user taps the Route button
     */
    public void SelectRoute(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("ROUTE",route);
        startActivity(intent);
    }

    public void askPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Location permission not accepted.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void resetDatabase(View view) {
        database.resetTable();
        new JsonParser(this).parseJson("waypoints.json");
    }
}
