package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.SlidingTab.SlidingTabLayout;

public class CreateQuestion extends FragmentActivity implements CreateQuestionAnswer.Listener,CreateHint1.Listener,CreateHint2.Listener,CreateHint3.Listener{

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    FloatingActionButton fab;
    CharSequence Titles[] = {"答案","提示一","提示二","提示三"};
    int NumOfTabs = 4;
    String answer="",hint1="",hint2="",imagepath="";
    Double latitude=0.0,longitude=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

//        //建立toolbar
//        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(),Titles,NumOfTabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout)findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.new_purple);
            }
        });

        tabs.setViewPager(pager);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                toCreateQuestionSend();
            }
        });
        fab.setEnabled(false);

        adapter.getCount();

        Bundle bundle = this.getIntent().getExtras();
        latitude = bundle.getDouble("lat");
        longitude = bundle.getDouble("lon");

    }

    @Override
    public void saveImagepath(String im){
        imagepath = im;
        checkupload();
        Toast.makeText(this,imagepath,Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getImagepath(){
        return imagepath;
    }

    @Override
    public void saveAnswer(String ans){
        answer = ans;
        checkupload();
    }

    @Override
    public void saveHint1(String ht1){
        hint1 = ht1;
        checkupload();
    }

    @Override
    public void saveHint2(String ht2){
        hint2 = ht2;
        checkupload();
    }

    private void checkupload(){

        Log.e("niki",answer);
        Log.e("niki", hint1);
        Log.e("niki",hint2);
        Log.e("niki",imagepath);
        if(!(answer.equals("")||hint1.equals("")||hint2.equals("")||imagepath.equals("")))
        {
            fab.setEnabled(true);
        }
        else
        {
            fab.setEnabled(false);
        }
    }

    private void toCreateQuestionSend(){
        Intent intent = new Intent(CreateQuestion.this,CreateQuestionSend.class);
        Bundle bundle = new Bundle();
        bundle.putString("answer",answer);
        bundle.putString("hint1",hint1);
        bundle.putString("hint2",hint2);
        bundle.putString("imagepath",imagepath);
        bundle.putDouble("lat",latitude);
        bundle.putDouble("lon",longitude);
        intent.putExtras(bundle);
        startActivity(intent);
        Toast.makeText(this,answer+hint1+hint2,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
