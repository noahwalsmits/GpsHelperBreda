package com.b.gpshelperbreda.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.data.Database;
import com.b.gpshelperbreda.data.JsonParser;
import com.b.gpshelperbreda.data.Route;
import com.b.gpshelperbreda.data.RouteFactory;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Route route;
    private Database database;
    private RouteFactory routeFactory;

    Button NL;
    Button ENG;
    Button startRoute;
    TextView routeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        database = new Database(this);
        if (!database.isTableFilled()) {
            new JsonParser(this).parseJson("waypoints.json");
        }

        routeFactory = new RouteFactory(this);

        route = routeFactory.getRouteFromId(1);

        NL = findViewById(R.id.NLBttn);
        ENG = findViewById(R.id.ENGBttn);
        routeName = findViewById(R.id.RouteSelectorText);
        startRoute = findViewById(R.id.route);

        NL.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Locale locale2 = new Locale("default");
                Locale.setDefault(locale2);

                Configuration config2 = new Configuration();
                config2.locale = locale2;

                getBaseContext().getResources().updateConfiguration(
                        config2,getBaseContext().getResources().getDisplayMetrics());

                routeName.setText(R.string.RouteSelectorText);
                startRoute.setText(R.string.Route);
            }
        });

        ENG.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Locale locale2 = new Locale("en");
                Locale.setDefault(locale2);

                Configuration config2 = new Configuration();
                config2.locale = locale2;

                getBaseContext().getResources().updateConfiguration(
                        config2,getBaseContext().getResources().getDisplayMetrics());

                routeName.setText(R.string.RouteSelectorText);
                startRoute.setText(R.string.Route);
            }
        });

    }

    /**
     * Called when the user taps the Route button
     */
    public void SelectRoute(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("ROUTE", route);
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

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_reset_database);

        Button yes = dialog.findViewById(R.id.bttnYes);
        Button no = dialog.findViewById(R.id.bttnNo);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.resetTable();
                new JsonParser(view.getContext()).parseJson("waypoints.json");
                route = routeFactory.getRouteFromId(1);

                dialog.cancel();

                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    public void infoButtonClick(View view) {
        startActivity(new Intent(this, InformationActivity.class));
    }
}
