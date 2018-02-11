package com.example.android.quakereport;

/**
 * Created by agent47 on 10/2/18.
 */

public class Earthquake {

    private double magnitude;
    private String place;
    private long date;
    private String url;

    public Earthquake(double magnitude,String place,long date,String url) {
        this.date = date;
        this.magnitude = magnitude;
        this.place = place;
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public String getPlace() {
        return this.place;
    }

    public long getDate() {
        return this.date;
    }

}
