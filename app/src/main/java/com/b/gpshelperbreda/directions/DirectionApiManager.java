package com.b.gpshelperbreda.directions;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectionApiManager {
    private Context context;
    private RequestQueue queue;
    private DirectionApiListener listener;

    private final String key = "f9455192-0b6c-4c1e-a472-8fa5a3f1eba7";
    private final String url = "http://145.48.6.80:3000/directions?origin=<latlng1>&destination=<latlng2>&mode=walking&key=";

    public DirectionApiManager(Context context, DirectionApiListener listener) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
        this.listener = listener;

    }

    public void doThing(LatLng origin, LatLng destination) {
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                this.generateUrl(origin, destination),
                null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        this.queue.add(request);
    }

    private String generateUrl(LatLng origin, LatLng destination) {
        String originText = origin.latitude + "," + origin.longitude;
        String destinationText = destination.latitude + "," + destination.longitude;
        String fullUrl = this.url + this.key;
        fullUrl = fullUrl.replace("<latlng1>", originText);
        fullUrl = fullUrl.replace("<latlng2>", destinationText);
        return fullUrl;
    }

    private void handleResponse(JSONObject response) {
        try {
            JSONArray routes = response.getJSONArray("routes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
