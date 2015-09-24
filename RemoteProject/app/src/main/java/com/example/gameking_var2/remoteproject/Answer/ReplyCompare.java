package com.example.gameking_var2.remoteproject.Answer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.gameking_var2.remoteproject.Http.GetServerMessage;
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

    String msg,Tid;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        Intent intent = this.getIntent();
        //取得傳遞過來的資料
        msg = intent.getStringExtra("msg");


        //判斷對錯給版面
        if( msg.equals("true") )
            setContentView(R.layout.answer_right);
        else if( msg.equals("false") )
            setContentView(R.layout.answer_wrong);

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
    public boolean onGesture(Gesture gesture) {
        //會傳入手勢  gesture.name()會取得手勢名稱 或是另一種 gesture ＝ Gesture.SWIPE_UP
        switch( gesture.name() )
        {
            case "TAP":
                startActivity(new Intent(ReplyCompare.this, Rank.class));
                break;
        }
        return false;
    }
}
