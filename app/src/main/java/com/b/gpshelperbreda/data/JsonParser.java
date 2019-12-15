package com.b.gpshelperbreda.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonParser {

    private Context context;
    private Database database;

    public JsonParser(Context context) {
        this.context = context;
        this.database = new Database(context);
    }

    public void parseJson(String path) {

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(loadJSONFromAsset(path));
        } catch (JSONException e) {
            System.out.println("Ja:");
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int sequenceID = jsonObject.getInt("sequenceID");
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                double latitude = jsonObject.getDouble("latitude");
                double longitude = jsonObject.getDouble("longitude");
                int routeID = jsonObject.getInt("routeID");
                String photoIDs = jsonObject.getString("photoIDs");
                boolean seen = jsonObject.getBoolean("seen");

                database.insertValue(new Waypoint(photoIDs, routeID, sequenceID, latitude, longitude, name, description, seen));
            }
        } catch (Exception e) {
            Log.d("Erro23452345r", e.toString());
        }
    }

    private String loadJSONFromAsset(String path) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            System.out.println(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
