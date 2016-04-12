package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.SlidingTab.SlidingTabLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AnswerQuestion extends FragmentActivity implements Hint1.Listener,Hint2.Listener,Hint3.Listener,Answer.Listener{

    //tab用
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"提示一","提示二","提示三","作答"};
    int NumOfTabs = 4;

    //題目資訊
    String answer="";
    String hint1="";
    String hint2="";
    String hint3="";
    String memberId="";

    int QuestionNo=0;

    //作答紀錄用
    int AnswerType;
    String AnswerContent="";
    int AnswerTimer=0;
    int AnswerRevolution = 0;

    //UI其他元件
    FloatingActionButton abort_fab,revolution_fab;
    TextView timer_txt;
    Timer timer;

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
        QuestionNo = bundle.getInt("titleId");
        memberId = bundle.getString("memberId");


        //產生tab
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),Titles,NumOfTabs,"AnswerQuestion");

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


        //放棄按鈕
        abort_fab=(FloatingActionButton)findViewById(R.id.abort_fab);
        abort_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abort();
            }
        });

        revolution_fab=(FloatingActionButton)findViewById(R.id.revolution_fab);
        revolution_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AnswerRevolution == 0){
                    revolution_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    AnswerRevolution =1;
                    Toast.makeText(getApplicationContext(),"革命模式開啟",Toast.LENGTH_SHORT).show();
                }
                else if(AnswerRevolution == 1){
                    revolution_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.new_gray)));
                    AnswerRevolution =0;
                    Toast.makeText(getApplicationContext(),"革命模式關閉",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //timer
        timer = new Timer();
        timer.schedule(task,0,1000);

//        timer_txt = (TextView)findViewById(R.id.timer_txt);

    }

//    //負責處理計時器UI的handler
//    private Handler handler = new Handler(){
//        public void handleMessage(Message msg){
//            super.handleMessage(msg);
//            String txt = "";
//            switch (msg.what) {
//                case 1:
//                    int min = AnswerTimer/60;
//                    int sec = AnswerTimer%60;
//                    if(min<10)
//                    {
//                        txt = "0"+min;
//                    }
//                    else
//                    {
//                        txt = " "+min;
//                    }
//                    if(sec<10)
//                    {
//                        txt = txt + " : 0"+sec;
//                    }
//                    else
//                    {
//                        txt = txt + " : " +sec;
//                    }
//
//                    break;
//            }
//            timer_txt.setText(txt);
//        }
//    };

    //計時器的任務
    private TimerTask task = new TimerTask(){
        @Override
        public void run() {
            AnswerTimer++;
//            Message message = new Message();
//            message.what = 1;
//            handler.sendMessage(message);
        }
    };

    //在提示一頁面時，回傳提示一
    @Override
    public String getHint1() {
        return hint1;
    }

    //在提示二頁面時，回傳提示二
    @Override
    public String getHint2() {
        return hint2;
    }

    //在提示三頁面時，回傳提示三的圖片url
    @Override
    public String getHint3() {
        return hint3;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //在此返回上一頁視同放棄作答
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            abort();
        }
        return super.onKeyDown(keyCode, event);
    }

    //放棄作答，還需寫回資料庫
    private void abort(){
        new AlertDialog.Builder(AnswerQuestion.this)
                .setCancelable(false)
                .setTitle("放棄")
                .setMessage("要放棄解答此題目嗎?")
                .setPositiveButton("放棄", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        //放棄，寫回資料庫
                        AnswerType = 2;
                        AnswerTimer = 0;
                        AnswerContent="放棄";
                        toscore();
                        AnswerQuestion.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

//    //上傳作答紀錄，在此只有放棄會用到
//    private void uploaduseranswer(){
//        AQuery aq = new AQuery(this);
//        String url = "http://163.17.135.76/glass/uploaduseranswer.php";
//
//        Map<String,Object> params = new HashMap<String, Object>();
//
//        params.put("QuestionNo",QuestionNo);
//        params.put("MemberId", MemberId);
//        params.put("UserAnswer", AnswerContent);
//        params.put("AnswerType", AnswerType);
//
//        aq.ajax(url, params, String.class, new AjaxCallback<String>() {
//            @Override
//            public void callback(String url, String object, AjaxStatus status) {
//                //成功
//                if (status.getCode() == 200) {
//                    Toast.makeText(AnswerQuestion.this, "上傳成功", Toast.LENGTH_SHORT).show();
//                }
//                //失敗
//                else {
//                    Toast.makeText(AnswerQuestion.this, String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    @Override
    public void sendAns(String userans) {
        timer.cancel();
        AnswerContent = userans;
        if(userans.equals(answer))
        {
            AnswerType = 1;
        }
        else
        {
            AnswerType = 0;
        }
        toscore();
    }

    private void toscore(){
        //到看答案正確與否的頁面
        Bundle bundle = new Bundle();
        bundle.putInt("QuestionNo", QuestionNo);
        bundle.putString("AnswerContent", AnswerContent);
        bundle.putInt("AnswerType", AnswerType);
        bundle.putInt("AnswerTimer",AnswerTimer);
        bundle.putInt("AnswerRevolution", AnswerRevolution);
        bundle.putString("memberId",memberId);
        Intent intent = new Intent(AnswerQuestion.this,Score.class);
        intent.putExtras(bundle);
        startActivity(intent);
        AnswerQuestion.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer, menu);
        return true;
    }
}
