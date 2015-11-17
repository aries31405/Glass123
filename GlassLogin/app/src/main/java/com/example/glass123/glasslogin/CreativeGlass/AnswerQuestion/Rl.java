package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by s1100b026 on 2015/11/16.
 */
public class Rl {
    Activity ac;
    private boolean iscreating = true;

    RelativeLayout relativeLayout;

    public Rl(RelativeLayout relativeLayout,Activity ac)
    {
        this.relativeLayout = relativeLayout;
        this.ac = ac;
    }

    public void leftcreating(int subtraction,int i)
    {
        Button bt = new Button(ac);
        bt.setId(i);
        relativeLayout.addView(bt,150,50);
        setLayout(bt, 480 - (subtraction * 18), 330 );
    }

    public void rightcreating(int subtraction,int i)
    {
        Button bt = new Button(ac);
        bt.setId(i);
        relativeLayout.addView(bt,150,50);
        setLayout(bt,480 + (subtraction * 18),330);
    }

    public void upcreating()
    {
        iscreating = false;
    }

    public boolean getcreating()
    {
        return iscreating;
    }

    public static void setLayout(View view,int x,int y)
    {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, x+margin.width, y+margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    public void movie(int i)
    {
        Button bt = (Button)relativeLayout.findViewById(i);
        setLayout(bt,100,100);
    }

    public void remo(int i)
    {
        Button bt = (Button)relativeLayout.findViewById(i);
        relativeLayout.removeView(bt);
    }

}
