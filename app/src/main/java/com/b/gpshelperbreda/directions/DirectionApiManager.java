package com.b.gpshelperbreda.directions;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.b.gpshelperbreda.data.Route;
import com.b.gpshelperbreda.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The class used to interact with the directions api
 */
public class DirectionApiManager {
    private Context context;
    private RequestQueue queue;
    private DirectionApiListener listener;

    private final String key = "f9455192-0b6c-4c1e-a472-8fa5a3f1eba7";
    private final String url = "http://145.48.6.80:3000/directions?origin=<latlng1>&destination=<latlng2>&mode=walking&key=";

    private final String tag = "DirectionApiManager";

    public DirectionApiManager(Context context, DirectionApiListener listener) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
        this.listener = listener;

    }

    /**
     * Calling this method will make the locationListener draw the entire route
     *
     * @param route The route to be drawn
     */
    public void generateDirections(Route route) {

        ArrayList<Waypoint> newRoute = new ArrayList<>();
        for (Waypoint waypoint : route.getWaypoints()) {
            if (!waypoint.isSeen()) {
                newRoute.add(waypoint);
            }
        }

        LatLng previous = null;
        for (Waypoint waypoint : newRoute) {
            if (previous != null) {
                this.generateDirections(previous, waypoint.getLatLng());
            }
            previous = waypoint.getLatLng();
        }
        if (newRoute.size() > 1) {
            this.generateDirections(newRoute.get(newRoute.size() - 1).getLatLng(), newRoute.get(0).getLatLng());
        }
    }

    /**
     * Calling this method will make the locationListener draw a route between the points
     *
     * @param origin      The starting point
     * @param destination The end point
     */
    public void generateDirections(LatLng origin, LatLng destination) {
        JsonObjectRequest request = new JsonObjectRequest(
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
                        Log.wtf(tag, "onErrorResponse");
                        error.printStackTrace();
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.queue.add(request);
    }

    /**
     * Creates a url to make a request to the directions api
     *
     * @param origin      The starting point of the route
     * @param destination The end point of the route
     * @return A url that can be used to make a request to the directions api
     */
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
            for (int i = 0; i < routes.length(); i++) {                     //for each route
                JSONObject route = routes.getJSONObject(i);
                JSONArray legs = route.getJSONArray("legs");
                PolylineOptions lineOptions = new PolylineOptions();
                for (int j = 0; j < legs.length(); j++) {                   //for each leg
                    JSONObject leg = legs.getJSONObject(j);
                    JSONArray steps = leg.getJSONArray("steps");
                    for (int k = 0; k < steps.length(); k++) {              //for each step
                        JSONObject step = steps.getJSONObject(k);
                        String encoded = step.getJSONObject("polyline").getString("points");
                        lineOptions.addAll(PolyUtil.decode(encoded));
                    }
                }
                this.listener.routeLineAvailable(lineOptions);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private class DirectionsTask extends AsyncTask<LatLng, Integer, PolylineOptions> {
//
//        @Override
//        protected PolylineOptions doInBackground(LatLng... latLngs) {
//            generateDirections(latLngs[0], latLngs[1]);
//            return null;
//        }
//
//        protected void onPostExecute(PolylineOptions polylineOptions) {
//
//        }
//    }

}
