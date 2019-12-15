package com.b.gpshelperbreda.data;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RouteFactory {

    private Database database;

    public RouteFactory(Context context) {
        database = new Database(context);
    }

    public Route getRouteFromId(int id){
        ArrayList<Waypoint> data = database.readValues();

        ArrayList<Waypoint> waypoints = new ArrayList<>();

        for (Waypoint wp: data) {
            if (wp.getRouteID() == id) {
                waypoints.add(wp);
            }
        }
        //Collections.sort(waypoints);

        return new Route("" + id, waypoints);
    }

}
