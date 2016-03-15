package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.content.Intent;
import android.content.res.ColorStateList;
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

    String answer="",hint1="",hint2="",imagepath="",memberId="";
    Double latitude=0.0,longitude=0.0;
    int floor;
    boolean check_flag=false;

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
                toCreateQuestionSend();
            }
        });

        setuploadfab();

        adapter.getCount();

        Bundle bundle = this.getIntent().getExtras();
        latitude = bundle.getDouble("lat");
        longitude = bundle.getDouble("lon");
        memberId = bundle.getString("memberId");

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

        //所有提示都有輸入才可以上傳
        if(!(answer.equals("")||hint1.equals("")||hint2.equals("")||imagepath.equals("")))
        {
            fab.setEnabled(true);
            check_flag = true;
        }
        else
        {
            fab.setEnabled(false);
            check_flag = false;
        }
        setuploadfab();

    }

    private void setuploadfab(){
        if(check_flag){
            fab.setEnabled(true);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
        else if(!check_flag){
            fab.setEnabled(false);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.new_white)));
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
        bundle.putInt("floor",floor);
        bundle.putString("memberId", memberId);
        intent.putExtras(bundle);
        int EXIT_CODE=1;
        startActivityForResult(intent, EXIT_CODE);
//        Toast.makeText(this,answer+hint1+hint2,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int EXIT_CODE=1;
        if(resultCode == EXIT_CODE)
        {
            Log.e("PETER","123456789");
            CreateQuestion.this.finish();
        }
    }

}
