package com.b.gpshelperbreda;

import android.content.Context;
import android.location.Location;

import com.b.gpshelperbreda.data.Database;
import com.b.gpshelperbreda.data.Route;
import com.b.gpshelperbreda.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;

public class RouteLogic {
    private LatLng userLocation;
    private Route route;
    private Waypoint nextWaypoint;
    private float distanceToNext;
    private Context context;
    RouteLogicListener listener;

    private static float MIN_DISTANCE = 20.0f;
    public static int NOTIFICATION_ONTRACK = -1;
    public static int NOTIFICATION_PROGRESS = -2;

    public RouteLogic(Route route, RouteLogicListener listener, Context context) {
        this.userLocation = userLocation;
        this.context = context; //replace with database
        this.route = route;
        this.listener = listener;
        this.nextWaypoint = route.getWaypoints().get(0);
        //this.firstWaypoint();
    }

    /**
     * Helper method only used in the constructor to select the first waypoint
     */
    private void firstWaypoint() {
        for (Waypoint waypoint : this.route.getWaypoints()) {
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
     *
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

        if (results[0] < MIN_DISTANCE) {
            this.advanceWaypoint();
        }
        if (results[0] > this.distanceToNext * 1.5) {
            this.listener.offTrack();
        }
    }

    /**
     * Helper method to advance to the next waypoint, this method does not check if it SHOULD advance or not
     */
    private void advanceWaypoint() {
        this.nextWaypoint.setSeen(true);
        new Database(this.context).insertValue(nextWaypoint);
        int index = this.route.getWaypoints().indexOf(nextWaypoint);
        for (int i = index; i < this.route.getWaypoints().size(); i++) {
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
                this.listener.waypointAdvanced(nextWaypoint);
                return;
            }
        }
        this.listener.routeCompleted();
    }

}
