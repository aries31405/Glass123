package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.glass123.glasslogin.R;

/**
 * Created by seahorse on 2016/3/7.
 */
public class Star extends Fragment implements View.OnClickListener{

    //star
    ImageButton star_imgbtn1;
    ImageButton star_imgbtn2;
    ImageButton star_imgbtn3;
    ImageButton star_imgbtn4;
    ImageButton star_imgbtn5;

    int nowstar=0;

    public interface Listener{
        void saveStar(int star);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_star,container,false);

        //star init
        star_imgbtn1 = (ImageButton)v.findViewById(R.id.star_imgbtn1);
        star_imgbtn2 = (ImageButton)v.findViewById(R.id.star_imgbtn2);
        star_imgbtn3 = (ImageButton)v.findViewById(R.id.star_imgbtn3);
        star_imgbtn4 = (ImageButton)v.findViewById(R.id.star_imgbtn4);
        star_imgbtn5 = (ImageButton)v.findViewById(R.id.star_imgbtn5);

        star_imgbtn1.setOnClickListener(this);
        star_imgbtn2.setOnClickListener(this);
        star_imgbtn3.setOnClickListener(this);
        star_imgbtn4.setOnClickListener(this);
        star_imgbtn5.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.star_imgbtn1:
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.notstar);
                star_imgbtn3.setImageResource(R.drawable.notstar);
                star_imgbtn4.setImageResource(R.drawable.notstar);
                star_imgbtn5.setImageResource(R.drawable.notstar);
                nowstar = 1;

                break;
            case R.id.star_imgbtn2:
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.star);
                star_imgbtn3.setImageResource(R.drawable.notstar);
                star_imgbtn4.setImageResource(R.drawable.notstar);
                star_imgbtn5.setImageResource(R.drawable.notstar);
                nowstar = 2;

                break;
            case R.id.star_imgbtn3:
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.star);
                star_imgbtn3.setImageResource(R.drawable.star);
                star_imgbtn4.setImageResource(R.drawable.notstar);
                star_imgbtn5.setImageResource(R.drawable.notstar);
                nowstar = 3;

                break;
            case R.id.star_imgbtn4:
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.star);
                star_imgbtn3.setImageResource(R.drawable.star);
                star_imgbtn4.setImageResource(R.drawable.star);
                star_imgbtn5.setImageResource(R.drawable.notstar);
                nowstar = 4;

                break;
            case R.id.star_imgbtn5:
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.star);
                star_imgbtn3.setImageResource(R.drawable.star);
                star_imgbtn4.setImageResource(R.drawable.star);
                star_imgbtn5.setImageResource(R.drawable.star);
                nowstar = 5;

                break;

        }
        ((Listener)getActivity()).saveStar(nowstar);

    }
}
