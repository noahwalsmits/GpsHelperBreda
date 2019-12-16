package com.b.gpshelperbreda.data;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {

    private String name;
    private ArrayList<Waypoint> waypoints;

    public Route(String name, ArrayList<Waypoint> waypoints) {
        this.name = name;
        this.waypoints = waypoints;
    }

    @NonNull
    @Override
    public String toString() {
        String result = "Route naam: " + name + "\n";

        for (Waypoint wp: waypoints) {
            result += wp.toString() + "\n";
        }

        return result;
    }

    //region getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    //endregion
}
