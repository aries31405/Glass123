package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

/**
 * Created by seahorse on 2015/11/16.
 */
public class Angle {
    double[][] laon = new double[4][2];
    double[] angle = new double[4];
    public Angle()
    {
        laon[0][0] =24.152792;
        laon[0][1] =120.675922;

        laon[1][0] =24.151607;
        laon[1][1] =120.675868;

        laon[2][0] =24.151793;
        laon[2][1] =120.674849;

        laon[3][0] =24.152870;
        laon[3][1] =120.674913;
    }

    public int geti()
    {
        return angle.length;
    }

    public double getag(int i)
    {
        return angle[i];
    }

    public void update(double latitud,double longitude)
    {

        for(int i = 0;i < laon.length;i++)
        {
            if(laon[i][0] > latitud && laon[i][1] >longitude)
            {
                angle[i] = gps2d(latitud,longitude,laon[i][0],laon[i][1]);
            }
            else if(laon[i][0] < latitud && laon[i][1] >longitude)
            {
                angle[i] = 180-gps2d(latitud,longitude,laon[i][0],laon[i][1]);
            }
            else if(laon[i][0] > latitud && laon[i][1] <longitude)
            {
                angle[i] = 360+gps2d(latitud,longitude,laon[i][0],laon[i][1]);
            }
            else if(laon[i][0] < latitud && laon[i][1] <longitude)
            {
                angle[i] = 180-gps2d(latitud,longitude,laon[i][0],laon[i][1]);
            }
        }
    }

    private double gps2d(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        lat_a=lat_a*Math.PI/180;
        lng_a=lng_a*Math.PI/180;
        lat_b=lat_b*Math.PI/180;
        lng_b=lng_b*Math.PI/180;

        d=Math.sin(lat_a)*Math.sin(lat_b)+Math.cos(lat_a)*Math.cos(lat_b)*Math.cos(lng_b-lng_a);
        d=Math.sqrt(1-d*d);
        d=Math.cos(lat_b)*Math.sin(lng_b - lng_a) /d;
        d=Math.asin(d)*180/Math.PI;

        //d = Math.round(d*10000);

        return d;
    }
}
