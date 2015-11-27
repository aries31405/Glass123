package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import com.example.glass123.glasslogin.Sensor.Sen;

/**
 * Created by s1100b026 on 2015/11/27.
 */
public class Decide {
    private double ag;
    private int x;
    private boolean cancreat = false;
    public Decide(double ag)
    {
        this.ag = ag;
    }
    public void decide()
    {
        cancreat = false;
        if((Sen.nowpositon+30) > 359)
        {
            creat( 30-(360-Sen.nowpositon),1);
        }
        else if((Sen.nowpositon-30) < 0)
        {
            creat( (Sen.nowpositon-30)*(-1),2);
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
        int x=(int) Sen.nowpositon;
        int y=(int) ag;

        if(why == 1)
        {
            if( 360 > ag  && (Sen.nowpositon - 30) < ag)
            {
                if(Sen.nowpositon < ag)
                {
                    this.x = 600 + ((y - x) * 20);
                    cancreat = true;
                }
                else
                {
                    this.x = 600 - ((x - y) * 20);
                    cancreat = true;
                }
            }
            else  if(p > ag)
            {
                this.x = 600 + ((360 - x + y) * 20);
                cancreat = true;
            }
        }
        else if(why == 2)
        {
            if(((Sen.nowpositon + 30) > ag  && 0 < ag))
            {
                if(Sen.nowpositon < ag)
                {
                    this.x = 600 + ((y - x) * 20);
                    cancreat = true;
                }
                else
                {
                    this.x = 600 - ((x - y) * 20);
                    cancreat = true;
                }
            }
            else if ((360-p) < ag)
            {
                this.x =  600 + ((360 - y + x) * 20);
                cancreat = true;
            }
        }
        else
        {
            if((Sen.nowpositon + 30) > ag && (Sen.nowpositon - 30) < ag)
            {
                if(Sen.nowpositon < ag)
                {
                    this.x = 600 + ((y - x) * 20);
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
