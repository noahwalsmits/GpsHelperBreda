package com.b.gpshelperbreda.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.b.gpshelperbreda.Notifications;
import com.b.gpshelperbreda.R;
import com.b.gpshelperbreda.RouteLogic;
import com.b.gpshelperbreda.RouteLogicListener;
import com.b.gpshelperbreda.data.Route;
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
import java.util.Calendar;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionApiListener, LocationTrackerListener, RouteLogicListener, PopupMenu.OnMenuItemClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Route route;

    private DirectionApiManager directionApiManager;
    private Marker userLocation;

    private LocationTracker locationTracker;
    private RouteLogic routeLogic;

    private ArrayList<Marker> markers;
    private Notifications notifications;

    private int currentMinute = -1;


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
        directionApiManager = new DirectionApiManager(this, this);
        locationTracker = new LocationTracker(this, this, (LocationManager) getSystemService(Context.LOCATION_SERVICE), this.notifications);
        routeLogic = new RouteLogic(route, this, this);
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMarkerClickListener(this);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(route.getWaypoints().get(0).getLatLng())
                .zoom(15)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        directionApiManager.generateDirections(route);

        for (Waypoint waypoint : route.getWaypoints()) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(waypoint.getLatLng()).title(waypoint.getName()));
            marker.setTag(waypoint.getSequenceID());
            if (waypoint.isSeen()) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            } else {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            }
            markers.add(marker);
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
    public boolean onMarkerClick(Marker marker) {
        for (Waypoint waypoint : route.getWaypoints()) {
            if (waypoint.getSequenceID() == (int) marker.getTag()) {
                Intent intent = new Intent(this, WaypointDetailedActivity.class);
                intent.putExtra("WAYPOINT", waypoint);
                startActivity(intent);
            }
        }

        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.information:
                startActivity(new Intent(this, InformationActivity.class));
                return true;
            case R.id.waypoints:
                startActivity(new Intent(this, WaypointActivity.class).putExtra("WAYPOINTS", route.getWaypoints()));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void routeLineAvailable(PolylineOptions polylineOptions) {
        mMap.addPolyline(polylineOptions);
    }

    @Override
    public void onResponseError(Error error) {
        Toast.makeText(this, "Could not connect with Directions Api", Toast.LENGTH_SHORT);
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (userLocation == null) {
            userLocation = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user2))
                    .title("User"));
        }
        userLocation.setPosition(latLng);
        userLocation.setTag(-1);
        routeLogic.updateUserLocation(latLng);
    }

    @Override
    public void waypointAdvanced(Waypoint nextWaypoint) {

        this.notifications.sendNotification(getResources().getString(R.string.waypoint_reached_title), getResources().getString(R.string.waypoint_reached) + nextWaypoint.getName() + ".", RouteLogic.NOTIFICATION_PROGRESS);
        for (Marker marker : markers) {
            if ((int) marker.getTag() == nextWaypoint.getSequenceID() - 1) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
        }
        Snackbar.make(getWindow().getDecorView().getRootView(), getResources().getString(R.string.waypoint_reached) + nextWaypoint.getName() + ".", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void routeCompleted() {
        this.notifications.sendNotification(getResources().getString(R.string.route_finished_title), getResources().getString(R.string.route_finished), RouteLogic.NOTIFICATION_PROGRESS);
        markers.get(markers.size() - 1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Snackbar.make(getWindow().getDecorView().getRootView(), getResources().getString(R.string.route_finished), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void offTrack() {
        if (Calendar.getInstance().getTime().getMinutes() == currentMinute) {
            return;
        }
        currentMinute = Calendar.getInstance().getTime().getMinutes();
        this.notifications.sendNotification(getResources().getString(R.string.route_offroute_title), getResources().getString(R.string.route_offroute), RouteLogic.NOTIFICATION_ONTRACK);
        Snackbar.make(getWindow().getDecorView().getRootView(), getResources().getString(R.string.route_offroute), Snackbar.LENGTH_LONG).show();
    }

    public void centerCamera(View view) {
        if (userLocation == null)
            return;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLocation.getPosition())
                .zoom(15)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
