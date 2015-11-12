package com.example.glass123.glasslogin.Sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by 孔雀舞 on 2015/11/5.
 */
public class Acceleration {

    private SensorManager sm;
    private Sensor Sensor;

    TextView tv;
    boolean ok ;

    public Acceleration(TextView tv,SensorManager sm)
    {
        this.tv = tv;
        this.sm = sm;

        Sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(myListener, Sensor, SensorManager.SENSOR_DELAY_GAME);

    }

    public void stop()
    {
        sm.unregisterListener(myListener);
    }

    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
             float g[] = sensorEvent.values;
            if(g[0] < 11 && g[0]> 9.3)
            {
                tv.setText(g[0]+"__"+g[1]+"__"+g[2]);
                ok = true;
            }
            else
            {
                ok = false;
            }

        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    public boolean ok()
    {
        return  ok;
    }
}
