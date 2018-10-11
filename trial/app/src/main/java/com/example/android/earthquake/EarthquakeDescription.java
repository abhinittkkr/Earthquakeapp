package com.example.android.earthquake;

import java.text.SimpleDateFormat;

public class EarthquakeDescription {
    private double m ;
    private String mlocation ;
    private String mtime ;
    private String murl ;

    public EarthquakeDescription( double magnitude , String location ,String time  ,String url ){
        m = magnitude ;
        mlocation = location ;
        mtime =time ;
        murl = url ;
    }
    public double getM() {
        return m;
    }
    public String getMtime() {
        return mtime;
    }
    public String getMlocation() {
        return mlocation;
    }
    public String getMurl() {
        return murl;
    }
}
