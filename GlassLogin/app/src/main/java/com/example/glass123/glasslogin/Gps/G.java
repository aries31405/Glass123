package com.example.glass123.glasslogin.Gps;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.FindQuestion;

/**
 * Created by s1100b026 on 2015/11/12.
 */
public class G implements LocationListener {

    public static double latitude=0.0,longitude=0.0,nowlatitude, nowlongitude;

    public G()
    {

    }

    @Override
    public void onLocationChanged(Location location) {
        //latitude = location.getLatitude();
        //longitude = location.getLongitude();
        latitude = 24.152214;
        longitude = 120.675439;

        if(nowlatitude == 0.0)
        {
            nowlatitude=latitude;
            nowlongitude=longitude;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
