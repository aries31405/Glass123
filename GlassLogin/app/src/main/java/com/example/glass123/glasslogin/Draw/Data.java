package com.example.glass123.glasslogin.Draw;

/**
 * Created by s1100b026 on 2015/12/21.
 */
public class Data {

    private double latitude,lontitude;
    private int titleId;
    public Data(double latitude,double lontitude,int titleId)
    {
        this.latitude = latitude;
        this.lontitude = lontitude;
        this.titleId = titleId;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLontitude()
    {
        return lontitude;
    }

    public int gettitleId()
    {
        return titleId;
    }
}
