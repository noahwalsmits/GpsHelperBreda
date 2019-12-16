package com.b.gpshelperbreda;

import android.location.Location;

import com.b.gpshelperbreda.data.Route;
import com.b.gpshelperbreda.data.Waypoint;
import com.b.gpshelperbreda.directions.LocationTrackerListener;
import com.google.android.gms.maps.model.LatLng;

public class RouteLogic {
    private LatLng userLocation;
    private Route route;
    private Waypoint nextWaypoint; //TODO instantiate
    private float distanceToNext;
    private Notifications notifications; //TODO move notification functions into locationListener method?
    RouteLogicListener listener;

    private static float MIN_DISTANCE = 10.0f; //TODO change
    private static int NOTIFICATION_ONTRACK = -1;
    private static int NOTIFICATION_PROGRESS = -2;

    public RouteLogic(LatLng userLocation, Route route, Notifications notifications, RouteLogicListener listener) {
        this.userLocation = userLocation;
        this.route = route;
        this.notifications = notifications;
        this.listener = listener;
        this.firstWaypoint();
    }

    /**
     * Helper method only used in the constructor to select the first waypoint
     */
    private void firstWaypoint() {
        for (Waypoint waypoint : this.route.getWaypoints()) { //TODO check if waypoints are sorted in the right order
            if (!waypoint.isSeen()) {
                float[] results = new float[1];
                Location.distanceBetween(
                        nextWaypoint.getLatitude(),
                        nextWaypoint.getLongitude(),
                        waypoint.getLatitude(),
                        waypoint.getLongitude(),
                        results); //the distance between the last waypoint and the next
                this.distanceToNext = results[0];

                this.nextWaypoint = waypoint;
            }
        }
    }

    /**
     * Method to update the user location and check if any waypoints have been reached.
     * @param userLocation The location of the user
     */
    public void updateUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
        this.checkForNextWaypoint();
    }

    /**
     * Helper method to check if the next waypoint is reached and to check if the user isn't too far from the next waypoint
     */
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
        if (results[0] > this.distanceToNext * 1.5) { //checks if not too far
            this.listener.offTrack();
            this.notifications.sendNotification("U dwaalt af", "U bent een grote afstand van het volgende punt.", NOTIFICATION_ONTRACK); //TODO translations
        }

    }

    /**
     * Helper method to advance to the next waypoint, this method does not check if it SHOULD advance or not
     */
    private void advanceWaypoint() {
        this.nextWaypoint.setSeen(true); //TODO save this in the database
        int index = this.route.getWaypoints().indexOf(nextWaypoint);
        for (int i = index; i < this.route.getWaypoints().size(); i++) { //TODO check if waypoints are sorted in the right order
            Waypoint waypoint = this.route.getWaypoints().get(i);
            if (!waypoint.isSeen()) {
                float[] results = new float[1];
                Location.distanceBetween(
                        nextWaypoint.getLatitude(),
                        nextWaypoint.getLongitude(),
                        waypoint.getLatitude(),
                        waypoint.getLongitude(),
                        results); //the distance between the last waypoint and the next
                this.distanceToNext = results[0];

                this.nextWaypoint = waypoint;
                this.listener.waypointAdvanced();
                this.notifications.sendNotification("Punt bereikt", "U moet nu naar: " + waypoint.getName() + ".", NOTIFICATION_PROGRESS); //TODO translations
                return;
            }
        }
        this.listener.routeCompleted();
        this.notifications.sendNotification("Route klaar", "U heeft het einde van de route bereikt.", NOTIFICATION_PROGRESS); //TODO translations
    }

}
