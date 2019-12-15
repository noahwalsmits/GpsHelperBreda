package com.b.gpshelperbreda.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this);

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void buttonOnClick(View view) {
        database.insertValue(new Waypoint("1", 2, 1, 10, 10, "Eerste", "Dit is de eerste", true));
    }

    public void button2OnClick(View view) {
        String tekst = "";
        ArrayList<Waypoint> waypoints = database.readValues();


        for (Waypoint w : waypoints) {
            tekst += w.toString();
            tekst += "\n";
        }

        TextView textView = findViewById(R.id.textView);
        textView.setText(tekst);

    }

    public void button3onClick(View view) {
        database.dropTable();
    }

    public void button4onClick(View view) {
        RouteFactory routeFactory = new RouteFactory(this);
        Route route = routeFactory.getRouteFromId(1);

        TextView textView = findViewById(R.id.textView);
        textView.setText(route.toString());
    }

    public void button5onClick(View view) {
        new JsonParser(this).parseJson("waypoints.json");
    }
}
