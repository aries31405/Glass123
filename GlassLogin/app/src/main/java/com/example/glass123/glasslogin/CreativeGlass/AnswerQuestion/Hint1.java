package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.glass123.glasslogin.R;

/**
 * Created by seahorse on 2016/3/5.
 */
public class Hint1 extends Fragment{
    TextView hint1_txt;

    public interface Listener{
        String getHint1();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_hint1,container,false);

        //init
        hint1_txt= (TextView)v.findViewById(R.id.hint1_txt);
        hint1_txt.setText(((Listener)getActivity()).getHint1());

        return v;
    }
}
