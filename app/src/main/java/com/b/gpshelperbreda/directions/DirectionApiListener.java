package com.b.gpshelperbreda.directions;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Interface used by DirectionApiManager for callbacks
 */
public interface DirectionApiListener {
    void routeLineAvailable(PolylineOptions polylineOptions);
    void onResponseError(Error error);
}
