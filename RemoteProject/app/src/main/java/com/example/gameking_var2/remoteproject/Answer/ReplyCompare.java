package com.example.gameking_var2.remoteproject.Answer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.gameking_var2.remoteproject.Http.GetServerMessage;
import com.example.gameking_var2.remoteproject.Profile.Profile;
import com.example.gameking_var2.remoteproject.R;
import com.example.gameking_var2.remoteproject.Rank.Rank;
import com.example.gameking_var2.remoteproject.Topic.Topic;
import com.google.android.glass.app.Card;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.FileInputStream;
import java.io.IOException;

public class ReplyCompare extends Activity implements GestureDetector.BaseListener
{

    //定義手勢偵測
    private GestureDetector GestureDetector;

    String msg,Tid,id,answer,answerType,answerTime;

    //Activity
    Profile act;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        //抓本Activity
        act = new Profile();
        act.ReplyCompare = ReplyCompare.this;


        Intent intent = this.getIntent();
        //取得傳遞過來的資料*
        msg = intent.getStringExtra("msg");
        Tid = intent.getStringExtra("Tid");
        id = intent.getStringExtra("id");
        answer = intent.getStringExtra("answer");
        answerTime = intent.getStringExtra("answerTime");


        //判斷對錯給版面
        if( msg.equals("true") )
        {
            setContentView(R.layout.answer_right);
            answerType ="1";
        }
        else if( msg.equals("false") )
        {
            setContentView(R.layout.answer_wrong);
            answerType ="0";
        }

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
                Intent intent = new Intent();
                intent.setClass(ReplyCompare.this,Rank.class);
                intent.putExtra("Tid", Tid);
                intent.putExtra("answer",answer);
                intent.putExtra("id", id);
                intent.putExtra("answerType", answerType);
                intent.putExtra("answerTime", answerTime);

                // 切換Activity
                startActivity(intent);
                break;
        }
        return true;
    }
}
