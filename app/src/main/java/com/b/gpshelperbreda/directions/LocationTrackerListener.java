package com.b.gpshelperbreda.directions;

import android.location.Location;

public interface LocationTrackerListener { //TODO rename
    void onLocationChanged(Location location);
}
