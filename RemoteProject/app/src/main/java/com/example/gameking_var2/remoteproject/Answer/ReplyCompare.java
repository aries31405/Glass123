package com.example.gameking_var2.remoteproject.Answer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.gameking_var2.remoteproject.Http.GetServerMessage;
import com.example.gameking_var2.remoteproject.Rank.Rank;
import com.example.gameking_var2.remoteproject.Topic.Topic;
import com.google.android.glass.app.Card;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by 孔雀舞 on 2015/9/19.
 */
public class ReplyCompare extends Activity implements GestureDetector.BaseListener{

    Card card;

    //定義手勢偵測
    private GestureDetector GestureDetector;

    String msg;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        Intent intent = this.getIntent();
        //取得傳遞過來的資料
        msg = intent.getStringExtra("msg");

        card = new Card(this);
        card.setText("您的回答:"+msg);
        View view = card.getView();
        setContentView(view);

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
