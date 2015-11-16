package com.example.glass123.glasslogin.Sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.Angle;
import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.FindQuestion;
import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.Rl;

/**
 * Created by s1100b026 on 2015/11/4.
 */
public class Sen {
    private Rl rl;
    private Angle ag;

    private Acceleration ac;

    private SensorManager sm;
    private Sensor aSensor;
    private Sensor mSensor;

    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];

    private static final String TAG = "sensor";

    TextView tv;
    float positon,nowpositon = 0;

    public Sen(TextView tv,SensorManager sm,Acceleration ac,Angle ag,Rl rl)
    {
        this.tv = tv;
        this.sm = sm;
        this.ac = ac;
        this.ag = ag;
        this.rl = rl;

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
            positon = transform(values[0]);
            if(nowpositon == 0)
            {
                nowpositon = positon;
            }
        }


        if(ac.ok() && rl.getcreating() && nowpositon != 0)
        {
            rl.upcreating();
            if((nowpositon+30) > 359)
            {
                creat( 30-(360-nowpositon),1);
            }
            else if((nowpositon-30) < 0)
            {
                creat( (nowpositon-30)*(-1),2);
            }
            else
            {
                creat(0,0);
            }
        }
        /*else if(ac.ok())
        {

            if(nowpositon + 5 < positon || nowpositon - 5 > positon )
            {
                nowpositon = positon;
                if((nowpositon+30) > 359)
                {
                    creat( 30-(360-nowpositon),1);
                }
                else if((nowpositon-30) < 0)
                {
                    creat( (nowpositon-30)*(-1),2);
                }
                else
                {
                    creat(0,0);
                }
            }
        }
        else if(ac.ok == false)
        {

        }*/

    }

    public void creat(float p , int why)
    {

        for(int i = 0;i < ag.geti();i++)
        {
            int x=(int)nowpositon;
            int y=(int)ag.getag(i);

            if(why == 1)
            {
                if( 360 > ag.getag(i)  && (nowpositon - 30) < ag.getag(i))
                {
                    if(nowpositon < ag.getag(i))
                    {
                        rl.rightcreating(y - x,i);
                        tv.setText(nowpositon+"--"+ag.getag(i));
                    }
                    else
                    {
                        rl.leftcreating(x - y,i);
                        tv.setText(nowpositon+"--"+ag.getag(i));
                    }
                }
                else  if(p > ag.getag(i))
                {
                    rl.rightcreating(360 - x + y,i);
                    tv.setText(nowpositon+"--"+ag.getag(i));
                }
            }
            else if(why == 2)
            {
                if(((nowpositon + 30) > ag.getag(i)  && 0 < ag.getag(i)))
                {
                    if(nowpositon < ag.getag(i))
                    {
                        rl.rightcreating(y - x,i);
                        tv.setText(nowpositon+"--"+ag.getag(i));
                    }
                    else
                    {
                        rl.leftcreating(x - y,i);
                        tv.setText(nowpositon+"--"+ag.getag(i));
                    }
                }
                else if ((360-p) < ag.getag(i))
                {
                    rl.rightcreating(360 - y+x,i);
                    tv.setText(nowpositon+"--"+ag.getag(i));
                }
            }
            else
            {
                if((nowpositon + 30) > ag.getag(i) && (nowpositon - 30) < ag.getag(i))
                {
                    if(nowpositon < ag.getag(i))
                    {
                        rl.rightcreating(y - x,i);
                        tv.setText(nowpositon+"--"+ag.getag(i));
                    }
                    else
                    {
                        rl.leftcreating(x - y,i);
                        tv.setText(nowpositon+"--"+ag.getag(i));
                    }
                }
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
