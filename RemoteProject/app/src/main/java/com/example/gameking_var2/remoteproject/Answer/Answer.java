package com.example.gameking_var2.remoteproject.Answer;

import com.example.gameking_var2.remoteproject.Http.GetServerMessage;
import com.example.gameking_var2.remoteproject.Profile.Profile;
import com.example.gameking_var2.remoteproject.R;
import com.google.android.glass.app.Card;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/*
答題頁面
功能：
１．利用語音出輸入答案後上傳
－－－－－－－
一指上傳
二指重新輸入
*/

public class Answer extends Activity  implements GestureDetector.BaseListener
{
    private Thread thread;
    private Handler handler  = new Handler();
    String id = null,Answer=null,msg=null,Tid;

    protected static final int RESULT_SPEECH = 1;

    //定義手勢偵測
    private GestureDetector GestureDetector;

    //回答文字欄
    TextView reply_tv;

    Card card;

    //Activity
    Profile act;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        //抓本Activity
        act = new Profile();
        act.Answers = Answer.this;

        Intent intent = this.getIntent();

        //取得傳遞過來的資料
        Tid = intent.getStringExtra("Tid");

        try//取得ID
        {
            FileInputStream in = openFileInput("Id.txt");
            byte[] data = new byte[128];
            in.read(data);
            in.close();
            id = new String(data);
        }
        catch(IOException e)
        {

        }

        card = new Card(this);
        card.setText("Tap to speak your answer");
        View view = card.getView();
        setContentView(R.layout.answer_reply);

        reply_tv = (TextView) findViewById(R.id.reply_tv);

        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);
    }

    //偵測手勢動作，回傳事件
    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        return GestureDetector.onMotionEvent(event);
    }

    @Override
    public boolean onGesture(Gesture gesture)
    {
        //會傳入手勢  gesture.name()會取得手勢名稱 或是另一種 gesture ＝ Gesture.SWIPE_UP
        switch( gesture.name() )
        {
            case "TAP":
                //語音輸入
                speech();
                break;
            case "LONG_PRESS":
                //設置執行續使用資料庫
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        GetServerMessage message = new GetServerMessage();
                        msg = message.all("http://163.17.135.76/glass/UserAnswer.php","titleId="+Tid+"&Id="+ id+"&Answer="+Answer);
                        handler.post(updata);
                    }

                }).start();
                break;
            case  "SWIPE_DOWN":
                break;
        }
        return false;
    }

    //執行續
    final Runnable updata = new Runnable()
    {
        @Override
        public void run()
        {
            Intent intent = new Intent();
            intent.setClass(Answer.this,ReplyCompare.class);
            intent .putExtra("msg", msg);//可放所有基本類別
            intent.putExtra("Tid",Tid);

            // 切換Activity
            startActivity(intent);
        }
    };

    //啟用語音輸入
   private void speech()
   {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.EXTRA_LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-us");

        try
        {
            startActivityForResult(intent, RESULT_SPEECH);
        }
        catch (ActivityNotFoundException a)
        {
            Toast t = Toast.makeText(getApplicationContext(), "Ops! Your device doesn't support Speech to Text", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case RESULT_SPEECH:
            {
                if (resultCode == RESULT_OK && null != data)
                {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    Answer=text.get(0);
                    reply_tv.setText(text.get(0));
                    /*card = new Card(this);
                    card.setText(text.get(0));
                    View view = card.getView();
                    setContentView(view);*/
                }
                break;
            }

        }
    }
    }
