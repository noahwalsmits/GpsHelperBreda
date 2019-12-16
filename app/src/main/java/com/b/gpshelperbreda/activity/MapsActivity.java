package com.b.gpshelperbreda.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.b.gpshelperbreda.Notifications;
import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.RouteLogic;
import com.b.gpshelperbreda.RouteLogicListener;
import com.b.gpshelperbreda.data.Route;
import com.b.gpshelperbreda.data.RouteFactory;
import com.b.gpshelperbreda.data.Waypoint;
import com.b.gpshelperbreda.directions.DirectionApiListener;
import com.b.gpshelperbreda.directions.DirectionApiManager;
import com.b.gpshelperbreda.directions.LocationTracker;
import com.b.gpshelperbreda.directions.LocationTrackerListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionApiListener, LocationTrackerListener, RouteLogicListener, PopupMenu.OnMenuItemClickListener {

    private GoogleMap mMap;
    private Route route;

    private DirectionApiManager directionApiManager;
    private Marker userLocation;

    private LocationTracker locationTracker;
    private RouteLogic routeLogic;

    private ArrayList<Marker> markers;
    private Notifications notifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        notifications = new Notifications(this);

        markers = new ArrayList<>();

        route = (Route) getIntent().getExtras().getSerializable("ROUTE");

        directionApiManager = new DirectionApiManager(this,this);

        locationTracker = new LocationTracker(this,this, (LocationManager) getSystemService(Context.LOCATION_SERVICE));

        routeLogic = new RouteLogic(route,this);

    }

    private void testRouteGen() { //TODO remove after testing is completed
        DirectionApiManager directions = new DirectionApiManager(this, this);
//        LatLng start = new LatLng(51.526174,5.057324);
//        mMap.addMarker(new MarkerOptions().position(start));
//        LatLng end = new LatLng(53.097141, 6.259557);
//        mMap.addMarker(new MarkerOptions().position(end));
//        directions.generateDirections(start, end);

//        directions.generateDirections(new RouteFactory(this).getRouteFromId(1));
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

        MapStyleOptions mapStyleOptions = new MapStyleOptions(getResources().getString(R.string.map_style_resource));
        mMap.setMapStyle(mapStyleOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(route.getWaypoints().get(0).getLatLng())
                .zoom(15)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //directionApiManager.generateDirections(route);

        for (Waypoint waypoint : route.getWaypoints()) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(waypoint.getLatLng()).title(waypoint.getName()));
            marker.setTag(waypoint.getSequenceID());
            if (waypoint.isSeen()) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }

            System.out.println(marker.toString() + "TAG:" + marker.getTag());
            markers.add(marker);
            //TODO KLEURTJES ASSIE DER GWEEST BEN
        }

    }

    public void showMenu(View view) {
        Context wrapper = new ContextThemeWrapper(this, R.style.customPopupMenu); //custom style
        PopupMenu popup = new PopupMenu(wrapper, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.map_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.information:
                startActivity(new Intent(this, InformationActivity.class));
                return true;
            case R.id.waypoints:
                startActivity(new Intent(this, WaypointActivity.class).putExtra("WAYPOINTS",route.getWaypoints()));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void routeLineAvailable(PolylineOptions polylineOptions) {
        //TODO have activity team customize the polyline to fit the style
        mMap.addPolyline(polylineOptions.color(Color.CYAN));
    }

    @Override
    public void onResponseError(Error error) {
        Toast.makeText(this, "Could not connect with Directions Api", Toast.LENGTH_SHORT);
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        if (userLocation == null) {
            userLocation = mMap.addMarker(new MarkerOptions()
            .position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            .title("User"));
        }
        userLocation.setPosition(latLng);
        routeLogic.updateUserLocation(latLng);
    }

    @Override
    public void waypointAdvanced(Waypoint nextWaypoint) {

        this.notifications.sendNotification("Punt bereikt", "U moet nu naar: " + nextWaypoint.getName() + ".", RouteLogic.NOTIFICATION_PROGRESS); //TODO translations
        for (Marker marker : markers) {
            if ((int)marker.getTag() == nextWaypoint.getSequenceID() - 1) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
        }
        Snackbar.make(getWindow().getDecorView().getRootView(),getResources().getString(R.string.waypoint_reached), Snackbar.LENGTH_LONG);
    }

    @Override
    public void routeCompleted() {
        this.notifications.sendNotification("Route klaar", "U heeft het einde van de route bereikt.", RouteLogic.NOTIFICATION_PROGRESS); //TODO translations
        Snackbar.make(getWindow().getDecorView().getRootView(),getResources().getString(R.string.waypoint_reached), Snackbar.LENGTH_LONG);
    }

    @Override
    public void offTrack() {
        this.notifications.sendNotification("U dwaalt af", "U bent een grote afstand van het volgende punt.", RouteLogic.NOTIFICATION_ONTRACK); //TODO translations
        //TODO graphical implementation
    }
}
