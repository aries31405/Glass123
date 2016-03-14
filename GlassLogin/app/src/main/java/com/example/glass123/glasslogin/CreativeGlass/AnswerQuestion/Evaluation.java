package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.*;
import com.example.glass123.glasslogin.CreativeGlass.CreativeGlassStart;
import com.example.glass123.glasslogin.MapsActivity;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.SlidingTab.SlidingTabLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Evaluation extends FragmentActivity implements Star.Listener,Commend.Listener{


    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    FloatingActionButton fab;
    CharSequence Titles[] = {"星等","評價"};
    int NumOfTabs = 2;

    FloatingActionButton uploadans_fab;

    //作答紀錄用
    int QuestionNo=0;
    String MemberId=""; //待接值
    String AnswerContent="";
    int AnswerType;
    int AnswerTimer=0;
    int AnswerRevolution = 0;
    int AnswerStar=0;
    int CommendNo=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(),Titles,NumOfTabs,"Evaluation");

        pager = (ViewPager) findViewById(R.id.pager_evaluation);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout)findViewById(R.id.tabs_evaluation);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.new_purple);
            }
        });

        tabs.setViewPager(pager);

        //接bundle的值
        Bundle bundle = this.getIntent().getExtras();
        QuestionNo = bundle.getInt("QuestionNo");
        MemberId = bundle.getString("MemberId");
        AnswerContent = bundle.getString("AnswerContent");
        AnswerType = bundle.getInt("AnswerType");
        AnswerTimer = bundle.getInt("AnswerTimer");
        AnswerRevolution = bundle.getInt("AnswerRevolution");

        uploadans_fab = (FloatingActionButton)findViewById(R.id.uploadans_fab);
        uploadans_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AnswerStar == 0)
                {
                    Toast.makeText(Evaluation.this,"請給星星",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadAns();
                }
            }
        });

    }

    private void uploadAns(){
        Toast.makeText(this,AnswerStar+"，評價 "+CommendNo,Toast.LENGTH_SHORT).show();

        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/new_glass/uploaduseranswer.php";

        Map<String,Object> params = new HashMap<>();

        params.put("QuestionNo",QuestionNo);
        params.put("MemberId",MemberId);
        params.put("AnswerContent",AnswerContent);
        params.put("AnswerType",AnswerType);
        params.put("AnswerTimer",AnswerTimer);
        params.put("AnswerRevolution",AnswerRevolution);
        params.put("AnswerStar",AnswerStar);
        params.put("CommendNo",CommendNo);

        aq.ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                //成功
                if (status.getCode() == 200) {
//                    Toast.makeText(Evaluation.this, "上傳評價成功", Toast.LENGTH_SHORT).show();
                    continueans();
                }
                //失敗
                else {
                    Toast.makeText(Evaluation.this, String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void saveStar(int star) {
        this.AnswerStar = star;
    }

    @Override
    public void saveCommend(int commend) {
        this.CommendNo = commend;
    }

    private void continueans(){
        new AlertDialog.Builder(Evaluation.this)
                .setCancelable(false)
                .setTitle("解題完成")
                .setMessage("要繼續解題嗎?")
                .setPositiveButton("繼續", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Evaluation.this, FindMap.class);
                        startActivity(intent);
                        int EXIT_CODE = 1;
                        setResult(EXIT_CODE);
                        Evaluation.this.finish();
                    }
                })
                .setNegativeButton("不繼續", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Evaluation.this, CreativeGlassStart.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        Evaluation.this.finish();
                    }
                }).show();

    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_evaluation);
//
//        //star init
//        star_imgbtn1 = (ImageButton)findViewById(R.id.star_imgbtn1);
//        star_imgbtn2 = (ImageButton)findViewById(R.id.star_imgbtn2);
//        star_imgbtn3 = (ImageButton)findViewById(R.id.star_imgbtn3);
//        star_imgbtn4 = (ImageButton)findViewById(R.id.star_imgbtn4);
//        star_imgbtn5 = (ImageButton)findViewById(R.id.star_imgbtn5);
//
//        star_imgbtn1.setOnClickListener(this);
//        star_imgbtn2.setOnClickListener(this);
//        star_imgbtn3.setOnClickListener(this);
//        star_imgbtn4.setOnClickListener(this);
//        star_imgbtn5.setOnClickListener(this);
//
//        //送出按鈕
//        sendevaluation_btn = (Button)findViewById(R.id.sendevaluation_btn);
//        sendevaluation_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(nowstar == 0)
//                {
//                    Toast.makeText(Evaluation.this,"請給星星",Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),nowstar+"，第"+String.valueOf(viewFlipper.getDisplayedChild()+1),Toast.LENGTH_SHORT).show();
////                    uploadevaluation();
//                }
//            }
//        });
//
//        //接bundle的值
//        Bundle bundle = this.getIntent().getExtras();
//        QuestionNo = bundle.getInt("QuestionNo");
//        MemberId = bundle.getString("MemberId");
//        AnswerContent = bundle.getString("AnswerContent");
//        AnswerType = bundle.getInt("AnswerType");
//        AnswerTimer = bundle.getInt("AnswerTimer");
//        AnswerRevolution = bundle.getInt("AnswerRevolution");
//
//
//
//    }
//
//
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if(event.getAction() == MotionEvent.ACTION_DOWN){
//            touchDownX = event.getX();
//            touchDownY = event.getY();
//            return true;
//        }
//        else if(event.getAction() == MotionEvent.ACTION_UP){
//            touchUpX=event.getX();
//            touchUpY=event.getY();
//            if(touchUpY-touchDownY > 100){
//                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
//                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
//
//                viewFlipper.showPrevious();
//            }
//            else if(touchDownY-touchUpY > 100){
//                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_in));
//                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_out));
//
//                viewFlipper.showNext();
//            }
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId())
//        {
//            case R.id.star_imgbtn1:
//                star_imgbtn1.setImageResource(R.drawable.star);
//                star_imgbtn2.setImageResource(R.drawable.notstar);
//                star_imgbtn3.setImageResource(R.drawable.notstar);
//                star_imgbtn4.setImageResource(R.drawable.notstar);
//                star_imgbtn5.setImageResource(R.drawable.notstar);
//                nowstar = 1;
//
//                break;
//            case R.id.star_imgbtn2:
//                star_imgbtn1.setImageResource(R.drawable.star);
//                star_imgbtn2.setImageResource(R.drawable.star);
//                star_imgbtn3.setImageResource(R.drawable.notstar);
//                star_imgbtn4.setImageResource(R.drawable.notstar);
//                star_imgbtn5.setImageResource(R.drawable.notstar);
//                nowstar = 2;
//
//                break;
//            case R.id.star_imgbtn3:
//                star_imgbtn1.setImageResource(R.drawable.star);
//                star_imgbtn2.setImageResource(R.drawable.star);
//                star_imgbtn3.setImageResource(R.drawable.star);
//                star_imgbtn4.setImageResource(R.drawable.notstar);
//                star_imgbtn5.setImageResource(R.drawable.notstar);
//                nowstar = 3;
//
//                break;
//            case R.id.star_imgbtn4:
//                star_imgbtn1.setImageResource(R.drawable.star);
//                star_imgbtn2.setImageResource(R.drawable.star);
//                star_imgbtn3.setImageResource(R.drawable.star);
//                star_imgbtn4.setImageResource(R.drawable.star);
//                star_imgbtn5.setImageResource(R.drawable.notstar);
//                nowstar = 4;
//
//                break;
//            case R.id.star_imgbtn5:
//                star_imgbtn1.setImageResource(R.drawable.star);
//                star_imgbtn2.setImageResource(R.drawable.star);
//                star_imgbtn3.setImageResource(R.drawable.star);
//                star_imgbtn4.setImageResource(R.drawable.star);
//                star_imgbtn5.setImageResource(R.drawable.star);
//                nowstar = 5;
//
//                break;
//
//        }
//    }
//
////    private void uploadevaluation(){
////        AQuery aq = new AQuery(this);
////        String url = "http://163.17.135.76/glass/uploaduserevaluation.php";
////
////        Map<String,Object> params = new HashMap<String, Object>();
////
////        params.put("titleId",titleId);
////        params.put("UserId",UserId);
////        params.put("evaluationStar",nowstar);
////
////        aq.ajax(url,params,String.class,new AjaxCallback<String>(){
////            @Override
////            public void callback(String url, String object, AjaxStatus status) {
////                //成功
////                if(status.getCode() == 200)
////                {
////                    Toast.makeText(Evaluation.this, "上傳評價成功", Toast.LENGTH_SHORT).show();
////                }
////                //失敗
////                else
////                {
////                    Toast.makeText(Evaluation.this,String.valueOf(status.getCode()),Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
////    }

}
