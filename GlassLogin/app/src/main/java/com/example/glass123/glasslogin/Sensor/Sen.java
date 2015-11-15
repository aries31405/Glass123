package com.example.glass123.glasslogin.Sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by s1100b026 on 2015/11/4.
 */
public class Sen {
    private Acceleration ac;

    private SensorManager sm;
    private Sensor aSensor;
    private Sensor mSensor;

    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];

    private static final String TAG = "sensor";

    TextView tv;
    float positon,nowpositon = 0;

    public Sen(TextView tv,SensorManager sm,Acceleration ac)
    {
        this.tv = tv;
        this.sm = sm;
        this.ac = ac;

        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener(myListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop()
    {
        sm.unregisterListener(myListener);
    }

    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {


            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values;

            calculateOrientation();
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    public float getpositon()
    {
        return nowpositon;
    }

    private  void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        values[0] = (float) Math.toDegrees(values[0]);
        //values[1] = (float) Math.toDegrees(values[1]);
        //values[2] = (float) Math.toDegrees(values[2]);
        //Log.i(TAG, values[0]+"");

        if(values[0] != 0.0)
        {
            positon = transform(values[0]);
        }

        if(nowpositon == 0 && ac.ok() == false)
        {
            nowpositon = positon;
        }
        else if(nowpositon == 0 && ac.ok() == true)
        {
            nowpositon = positon;
            //創建
        }
        else if((nowpositon + 5 < positon || nowpositon - 5 > positon) && ac.ok() == false )
        {
            nowpositon = positon;
        }
        else if((nowpositon + 5 < positon || nowpositon - 5 > positon) && ac.ok() == true)
        {
            nowpositon = positon;
            //創建
        }
        tv.setText(String.valueOf(nowpositon));
    }
    public float transform(float positon)
    {
        if(positon >= -179.9 && positon <-91 )
        {
            positon = 270 + (180-(positon*-1));
        }
        else
        {
            positon = positon + 90;
        }
        return positon;
    }

}
