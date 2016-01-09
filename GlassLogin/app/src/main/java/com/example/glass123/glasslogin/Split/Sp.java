package com.example.glass123.glasslogin.Split;

/**
 * Created by s1100b026 on 2016/1/10.
 */
public class Sp {

    String[] all,titleId,X,Y,status;
    public Sp(String allmsg)
    {
        all = allmsg.split("&");

        titleId = new String[all.length];
        X =  new String[all.length];
        Y  = new String[all.length];
        status = new String[all.length];

        for (int i = 0;i < all.length;i++)
        {
            String[] msg = all[i].split("-");
            titleId[i] = msg[0];
            X[i] = msg[1];
            Y[i] = msg[2];
            status[i] = msg[3];
        }

    }

    public int gettitleId(int i)
    {
        return Integer.parseInt(titleId[i]);
    }

    public double getX(int i)
    {
        return Double.parseDouble(X[i]);
    }

    public double getY(int i)
    {
        return Double.parseDouble(Y[i]);
    }

    public int getstatus(int i)
    {
        return Integer.parseInt(status[i]);
    }

    public int geti()
    {
        return all.length;
    }
}
