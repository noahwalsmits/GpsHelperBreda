package com.b.gpshelperbreda.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.provider.ContactsContract;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.data.Database;
import com.b.gpshelperbreda.data.JsonParser;
import com.b.gpshelperbreda.data.Route;
import com.b.gpshelperbreda.data.RouteFactory;
import com.b.gpshelperbreda.data.Waypoint;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the Route button
     */
    public void SelectRoute(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
