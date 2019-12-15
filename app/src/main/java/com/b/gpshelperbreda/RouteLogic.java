package com.b.gpshelperbreda;

import android.location.Location;

import com.b.gpshelperbreda.data.Route;
import com.b.gpshelperbreda.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;

public class RouteLogic {
    private LatLng userLocation;
    private Route route;
    private Waypoint nextWaypoint;
    private Notifications notifications; //TODO move notification functions into listener method?
    RouteLogicListener listener;

    private static float MIN_DISTANCE = 10.0f; //TODO change

    public RouteLogic(LatLng userLocation, Route route, Notifications notifications, RouteLogicListener listener) {
        this.userLocation = userLocation;
        this.route = route;
        this.notifications = notifications;
        this.listener = listener;
    }

    public void updateUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
        this.checkForNextWaypoint();
        this.checkIfOnTrack();
    }

    private void checkForNextWaypoint() {
        float[] results = new float[1];
        Location.distanceBetween(
                this.userLocation.latitude,
                this.userLocation.longitude,
                this.nextWaypoint.getLatitude(),
                this.nextWaypoint.getLongitude(),
                results);

        if (results[0] < this.MIN_DISTANCE) {
            this.advanceWaypoint();
        }

    }

    /**
     * Helper method to advance to the next waypoint
     */
    private void advanceWaypoint() {
        this.nextWaypoint.setSeen(true); //TODO save this in the database
        int index = this.route.getWaypoints().indexOf(nextWaypoint);
        for (int i = index; i < this.route.getWaypoints().size(); i++) { //TODO check if waypoints are sorted in the right order
            Waypoint waypoint = this.route.getWaypoints().get(i);
            if (!waypoint.isSeen()) {
                this.nextWaypoint = waypoint;
                this.listener.waypointAdvanced();
                this.notifications.sendNotification("Punt bereikt", "U heeft " + waypoint.getName() + " bereikt."); //TODO translations
                return;
            }
        }
        this.listener.routeCompleted();
        this.notifications.sendNotification("Route klaar", "U heeft het einde van de route bereikt."); //TODO translations
        //TODO You have completed the route
    }

    private void checkIfOnTrack() {
        //TODO implement
    }
}
