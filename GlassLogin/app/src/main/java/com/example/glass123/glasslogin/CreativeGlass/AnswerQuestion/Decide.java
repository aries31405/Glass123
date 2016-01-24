package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.glass123.glasslogin.Sensor.Sen;

/**
 * Created by s1100b026 on 2015/11/27.
 */
public class Decide {
    private double ag;
    private int x;
    private boolean cancreat = false;
    private float positon;
    public Decide(double ag,float positon)
    {
        this.ag = ag;
        this.positon = positon;
    }
    public void decide()
    {
        cancreat = false;
        if((positon+30) > 359)
        {
            creat( 30-(360- positon),1);
        }
        else if((positon-30) < 0)
        {
            creat( (positon-30)*(-1),2);
        }
        else
        {
            creat(0,0);
        }
    }

    public boolean cancareat()
    {
        return cancreat;
    }

    public int x()
    {
        return x;
    }

    public void creat(float p , int why)
    {
        int x=(int) positon;
        int y=(int) ag;

        if(why == 1)
        {
            if( 360 > ag  && (positon - 30) < ag)
            {
                if(positon < ag)
                {
                    this.x = 600 + ((y - x) * 30);
                    cancreat = true;
                }
                else
                {
                    this.x = 600 - ((x - y) * 30);
                    cancreat = true;
                }
            }
            else  if(p > ag)
            {
                this.x = 600 + ((360 - x + y) * 30);
                cancreat = true;
            }
        }
        else if(why == 2)
        {
            if(((positon + 30) > ag  && 0 < ag))
            {
                if(positon < ag)
                {
                    this.x = 600 + ((y - x) * 30);
                    cancreat = true;
                }
                else
                {
                    this.x = 600 - ((x - y) * 30);
                    cancreat = true;
                }
            }
            else if ((360-p) < ag)
            {
                this.x =  600 + ((360 - y + x) * 30);
                cancreat = true;
            }
        }
        else
        {
            if((positon + 30) > ag && (positon - 30) < ag)
            {
                if(positon < ag)
                {
                    this.x = 600 + ((y - x) * 30);
                    cancreat = true;
                }
                else
                {
                    this.x =  600 - ((x - y) * 20);
                    cancreat = true;
                }
            }
        }


    }
}
