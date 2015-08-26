package com.example.gameking_var2.remoteproject.Answer;

import com.example.gameking_var2.remoteproject.R;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

/*
進入題目顯示頁面
功能：
１．顯示兩個提示與照片
２．單指單擊跳到選項頁面 Option
３．三指單擊語音輸入答案 跳到Answer
*/

public class TitleCard extends Activity implements GestureDetector.BaseListener
{

    private CardScrollView mCardScroller;

    private View mView;

    //定義手勢偵測
    private GestureDetector GestureDetector;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        mView = buildView();

        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(new CardScrollAdapter()
        {
            @Override
            public int getCount()
            {
                return 1;
            }

            @Override
            public Object getItem(int position)
            {
                return mView;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                return mView;
            }

            @Override
            public int getPosition(Object item)
            {
                if (mView.equals(item))
                {
                    return 0;
                }
                return AdapterView.INVALID_POSITION;
            }
        });

        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(TitleCard.this, Options.class));
            }
        });

        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);

        setContentView(mCardScroller);
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
                //進入選單
                startActivity(new Intent(TitleCard.this, Options.class));
                break;
            case "TWO_TAP":
                //答題
                startActivity(new Intent(TitleCard.this, Answer.class));
                break;
            case "SWIPE_DOWN":
                finish();
                break;
        }
        return false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause()
    {
        mCardScroller.deactivate();
        super.onPause();
    }

    private View buildView()
    {
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);
        card.setText("題目提示與照片頁面");
        return card.getView();
    }
}
