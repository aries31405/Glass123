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

    TextView tv;
    private double latitude=0.0,longitude=0.0;

    public G(Activity a,TextView tv)
    {
        this.tv = tv;
    }

    @Override
    public void onLocationChanged(Location location) {
        double la,lon;
        la = location.getLatitude();
        lon = location.getLongitude();

        //if(la > (latitude + 0.000008) || la < (latitude-0.000008))
        //{
            latitude = la;
        //}

        //if(lon > (longitude + 0.000008) || lon < (longitude-0.000008))
        //{
            longitude = lon;
        //}

        tv.setText(String.valueOf(latitude)+"---"+String.valueOf(longitude));
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
