package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.glass123.glasslogin.R;

/**
 * Created by seahorse on 2016/3/5.
 */
public class Hint2 extends Fragment{
    TextView hint2_txt;

    public interface Listener{
        String getHint2();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_hint2,container,false);

        //init
        hint2_txt= (TextView)v.findViewById(R.id.hint2_txt);
        hint2_txt.setText(((Listener) getActivity()).getHint2());

        return v;
    }
}
