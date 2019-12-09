package com.b.gpshelperbreda.directions;

public interface DirectionApiListener {
    void onResponseAvailable(); //TODO decide proper parameter
    void onResponseError(Error error);
}
