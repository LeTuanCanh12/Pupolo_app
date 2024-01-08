package com.example.pulopo.model;

public class LocationCurrent {
    private double Lat;
    private double Long;

    public LocationCurrent(double lat, double aLong) {
        Lat = lat;
        Long = aLong;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }
}
