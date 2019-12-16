package com.b.gpshelperbreda;

import com.b.gpshelperbreda.data.Waypoint;

public interface RouteLogicListener {
    void waypointAdvanced(Waypoint nextWaypoint);
    void routeCompleted();
    void offTrack();
}
