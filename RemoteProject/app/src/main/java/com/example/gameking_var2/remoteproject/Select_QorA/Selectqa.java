package com.example.gameking_var2.remoteproject.Select_QorA;

import com.example.gameking_var2.remoteproject.CardsAdapter.CardAdapter;
import com.example.gameking_var2.remoteproject.MainLine.MainLine;
import com.example.gameking_var2.remoteproject.R;
import com.example.gameking_var2.remoteproject.SearchQuestion.Searchq;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/*
選擇單人 多人 或出題目
功能：
１．單人遊戲（故事模式）  尚未設計
２．多人遊戲 抵達地點後跳出題目 點選進入題目
３．出題目  題目會在多人模式顯現
－－－－－－－
單人尚無功能
多人連結至Answer -> TitleCard
出題尚無設計功能
*/

public class Selectqa extends Activity
{
    //不知道
    private static final String TAG = Selectqa.class.getSimpleName();

    //定義卡片順序 方便了解
    static final int Single = 0;
    static final int Multiple = 1;
    static final int Topic = 2;

    //上滑動佈景 下是滑動卡片
    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);


        //將卡片類別 傳回來  並用自定義類別"CardAdapter"（覆寫卡片類別）
        mAdapter = new CardAdapter(createCards(this));

        //預設 抓本體
        mCardScroller = new CardScrollView(this);

        //將本體設定為用好的自定義類別
        mCardScroller.setAdapter(mAdapter);

        //設定場景
        setContentView(mCardScroller);

        //設定卡片點擊事件
        setCardScrollerListener();
    }

    //建立滑動卡片 使用List
    private List<CardBuilder> createCards(Context context)
    {
        //List的卡片創建
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();

        //逐一建造
        cards.add
        (
            Single, new CardBuilder(context, CardBuilder.Layout.MENU).setText("單人")
        );
        cards.add
        (
            Multiple, new CardBuilder(context, CardBuilder.Layout.MENU).setText("多人")
        );
        cards.add
        (
            Topic, new CardBuilder(context, CardBuilder.Layout.MENU).setText("出題")
        );

        return cards;
    }

    //設定卡片點擊監聽
    private void setCardScrollerListener()
    {
        //卡片的View 設定監聽
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //不知道
                Log.d(TAG, "Clicked view at position " + position + ", row-id " + id);
                int soundEffect = Sounds.TAP;

                //判斷點擊哪個卡片
                switch (position) {
                    case Single:
                        Toast.makeText(Selectqa.this, "敬請期待", LENGTH_LONG).show();
                        break;

                    case Multiple:
                        startActivity(new Intent(Selectqa.this, Searchq.class));
                        break;

                    case Topic:
                        break;

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


}
