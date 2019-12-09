package com.b.gpshelperbreda.data;

import java.util.ArrayList;

public class Route {

    private String name;
    private ArrayList<Waypoint> waypoints;



    public Route(String name, ArrayList<Waypoint> waypoints) {
        this.name = name;
        this.waypoints = waypoints;
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
