package com.b.gpshelperbreda.data;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Arrays;

public class Waypoint implements Comparable<Waypoint>, Serializable {

    private int sequenceID;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private int routeID;
    private int[] photoIDs;
    private boolean seen;

    public Waypoint(String photoIDs, int routeID, int sequenceID, double latitude, double longitude, String name, String description, boolean seen) {
        this.photoIDs = convertStringToIntArr(photoIDs);
        this.routeID = routeID;
        this.sequenceID = sequenceID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
        this.seen = seen;
    }

    @Override
    public int compareTo(Waypoint other) {
        return (this.sequenceID - other.sequenceID);
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "sequenceID=" + sequenceID +
                ", name='" + name + '\'' +
                ", seen=" + seen +
                '}';
    }

//    @Override
//    public String toString() {
//        return "Waypoint{" +
//                "sequenceID=" + sequenceID +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", latitude='" + latitude + '\'' +
//                ", longitude='" + longitude + '\'' +
//                ", routeID=" + routeID +
//                ", photoIDs=" + Arrays.toString(photoIDs) +
//                ", seen=" + seen +
//                '}';
//    }

    private int[] convertStringToIntArr(String photoIDsString) {
        String[] stringArr = photoIDsString.split(",");
        int[] photoIDs = new int[stringArr.length];
        for (int i = 0; i < stringArr.length; i++) {
            photoIDs[i] = Integer.parseInt(stringArr[i]);
        }
        return photoIDs;
    }

    public LatLng getLatLng(){
        return new LatLng(this.latitude, this.longitude);
    }

    public String getPhotoIDsToString() {
        String result = "";
        for (int i = 0; i < this.photoIDs.length; i++) {
            result += (i != this.photoIDs.length - 1) ? photoIDs[i] + "," : photoIDs[i];
        }
        return result;
    }

    //region getters and setters
    public int[] getPhotoIDs() {
        return photoIDs;
    }

    public void setPhotoIDs(int[] photoIDs) {
        this.photoIDs = photoIDs;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public int getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(int sequenceID) {
        this.sequenceID = sequenceID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    //endregion
}