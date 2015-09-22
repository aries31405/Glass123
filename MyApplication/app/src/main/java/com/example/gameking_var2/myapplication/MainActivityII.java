package com.example.gameking_var2.myapplication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.example.gameking_var2.myapplication.DB_connection.DB_connection;
import com.example.gameking_var2.myapplication.GPS.GPS;
import com.example.gameking_var2.myapplication.Hello.Hello;
import com.example.gameking_var2.myapplication.RANK.RANK;
import com.example.gameking_var2.myapplication.cardAdapter.CardAdapter;
import com.example.gameking_var2.myapplication.cardAdapter.CardAdapterII;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

//IMPORT給card創建list（可以左右滑動的卡片）
//功能不明

public class MainActivityII extends Activity
{
    //不知道
    private static final String TAG = MainActivityII.class.getSimpleName();

    //定義卡片  後面的數字是卡片順序  無法顛倒（創造卡片時必須按照順序）定義變數是方便識別
    static final int One = 0;
    static final int Two = 1;
    static final int Three = 2;

    //上面是設定卡片  下面是場景 設定為上面的卡片
    private CardAdapterII mAdapter;
    private CardScrollView mCardScroller;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        //將卡片類別 傳回來  並用自定義類別"CardAdapter"（覆寫卡片類別）
        mAdapter = new CardAdapterII(createCards(this));

        //預設 抓本體
        mCardScroller = new CardScrollView(this);

        //將本體設定為用好的自定義類別
        mCardScroller.setAdapter(mAdapter);

        //設定場景
        setContentView(mCardScroller);

        //設定卡片點擊偵測
        setCardScrollerListener();
    }

    //自訂函式 建立多重卡片  使用內建List回傳
    private List<View> createCards(Context context)
    {
        //List的卡片創建／／
        ArrayList<View> cards = new ArrayList<View>();

        //逐一建造
        cards.add
        (
                One,  LayoutInflater.from(context).inflate(R.layout.glass, null)
        );
        cards.add
        (
                Two, LayoutInflater.from(context).inflate(R.layout.glass1, null)
        );
        cards.add
        (
                Three, LayoutInflater.from(context).inflate(R.layout.text, null)
        );

        return cards;
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

    //設定卡片點擊監聽  自訂函式
    private void setCardScrollerListener()
    {
        //卡片的View 設定監聽 裡面得內建函式  覆寫
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //不知道
                Log.d(TAG, "Clicked view at position " + position + ", row-id " + id);
                int soundEffect = Sounds.TAP;

                //判斷點擊哪個卡片
                switch (position)
                {

                    default:
                        soundEffect = Sounds.ERROR;
                        Log.d(TAG, "Don't show anything");

                }

                // Play sound.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);
            }
        });
    }

}
