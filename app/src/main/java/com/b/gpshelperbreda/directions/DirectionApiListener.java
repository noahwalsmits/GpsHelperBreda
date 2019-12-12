package com.b.gpshelperbreda.directions;

import com.google.android.gms.maps.model.PolylineOptions;

public interface DirectionApiListener {
    void onResponseAvailable(); //TODO decide proper parameter
    void routeLine(PolylineOptions polylineOptions); //TODO give proper name
    void onResponseError(Error error);
}
