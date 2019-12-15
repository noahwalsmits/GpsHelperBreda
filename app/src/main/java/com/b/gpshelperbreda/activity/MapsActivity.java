package com.b.gpshelperbreda.activity;

import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.RouteLogicListener;
import com.b.gpshelperbreda.data.RouteFactory;
import com.b.gpshelperbreda.directions.DirectionApiListener;
import com.b.gpshelperbreda.directions.DirectionApiManager;
import com.b.gpshelperbreda.directions.LocationTrackerListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionApiListener, LocationTrackerListener, RouteLogicListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(51.526174,5.057324);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void routeLineAvailable(PolylineOptions polylineOptions) {
        //TODO have activity team customize the polyline to fit the style
        mMap.addPolyline(polylineOptions);
    }

    @Override
    public void onResponseError(Error error) {
        Toast.makeText(this, "Could not connect with Directions Api", Toast.LENGTH_SHORT);
    }

    @Override
    public void onLocationChanged(Location location) {
        //TODO update location on map and call RouteLogic.updateUserLocation()
    }

    @Override
    public void waypointAdvanced() {
        //TODO graphical implementation
    }

    @Override
    public void routeCompleted() {
        //TODO graphical implementation
    }

    @Override
    public void offTrack() {
        //TODO graphical implementation
    }
}
