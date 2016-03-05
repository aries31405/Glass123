package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.androidquery.AQuery;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.SlidingTab.SlidingTabLayout;

public class AnswerQuestion extends FragmentActivity implements Hint1.Listener,Hint2.Listener,Hint3.Listener{

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"提示一","提示二","提示三","作答"};
    int NumOfTabs = 4;

    String answer="";
    String hint1="";
    String hint2="";
    String hint3="";

    int titleId=0;

    String serverip="http://163.17.135.76";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        //接bundle
        Bundle bundle = this.getIntent().getExtras();
        answer = bundle.getString("answer");
        hint1 = bundle.getString("hint1");
        hint2 = bundle.getString("hint2");
        hint3 = bundle.getString("hint3");
        titleId = bundle.getInt("titleId");


        //產生tab
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),Titles,NumOfTabs);

        pager = (ViewPager) findViewById(R.id.pager_answer);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout)findViewById(R.id.tabs_answer);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.new_purple);
            }
        });

        tabs.setViewPager(pager);

    }

    @Override
    public String getHint1() {
        return hint1;
    }


    @Override
    public String getHint2() {
        return hint2;
    }


    @Override
    public String getHint3() {
        return hint3;
    }
}
