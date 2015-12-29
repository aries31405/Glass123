package com.example.glass123.glasslogin.Sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.Angle;

/**
 * Created by s1100b026 on 2015/11/4.
 */
public class Sen {
    private SensorManager sm;
    private Sensor aSensor;
    private Sensor mSensor;

    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];

    private static final String TAG = "sensor";
    public static float postion,nowpostion = 0;

    public Sen(SensorManager sm)
    {
        this.sm = sm;

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
            postion = transform(values[0]);
            if(nowpostion == 0)
            {
                nowpostion = postion;
            }
        }

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
