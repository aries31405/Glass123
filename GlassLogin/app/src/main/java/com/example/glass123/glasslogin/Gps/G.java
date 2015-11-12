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

    LocationManager mlocation;
    TextView tv;
    private double latitude=0.0,longitude=0.0;

    public G(LocationManager mlocation,Activity a,TextView tv)
    {
        this.mlocation = mlocation;
        this.tv = tv;
        if (mlocation.isProviderEnabled(LocationManager.GPS_PROVIDER) || mlocation.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            mlocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 0,this);
        } else {
            Toast.makeText(a, "請開啟定位服務", Toast.LENGTH_LONG).show();
        }

    }
    public double getlatitude()
    {

        return latitude;
    }

    public double getlongitude()
    {

        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        tv.setText(String.valueOf(latitude)+"---"+String.valueOf(longitude));

        mlocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0,this);
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
