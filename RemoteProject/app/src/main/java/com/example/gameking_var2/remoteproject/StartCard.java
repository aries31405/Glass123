package com.example.gameking_var2.remoteproject;

import com.example.gameking_var2.remoteproject.CardsAdapter.CardAdapter;
import com.example.gameking_var2.remoteproject.Login.Login;
import com.example.gameking_var2.remoteproject.MainLine.MainLine;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/*
遊玩前頁面
功能：
１．連結手機
２．登入google帳號
３．顯示登入後帳戶資訊
４．選擇性別及年齡
５．完成設定，進入主畫面
－－－－－－－－－
連結至MainLine頁面
*/

public class StartCard extends Activity
{
    //不知道
    private static final String TAG = StartCard.class.getSimpleName();

    //定義卡片順序 方便了解
    static final int Connection = 0;
    static final int Login = 1;
    static final int Profile = 2;
    static final int Sex = 3;
    static final int Age = 4;
    static final int Success = 5;

    //上滑動佈景 下是滑動卡片
    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    //現在的卡片  用來判斷開啟哪個選單
    private int nowCard = -1;

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
            Connection, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.con_r)
        );
        cards.add
        (
            Login, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.signin)
        );
        cards.add
        (
            Profile, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.profile)
        );
        cards.add
        (
            Sex, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.sex)
        );
        cards.add
        (
            Age, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.age)
        );
        cards.add
        (
            Success, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.success)
        );
        return cards;
    }

    //設定卡片點擊監聽
    private void setCardScrollerListener()
    {
        //卡片的View 設定監聽
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
                    case Connection:
                        nowCard = Connection;
                        break;

                    case Login:
                        nowCard = Login;
                        //登入
                        break;

                    case Profile:
                        nowCard = Profile;
                        break;

                    case Sex:
                        nowCard = Sex;
                        openOptionsMenu();
                        break;

                    case Age:
                        nowCard = Age;
                        openOptionsMenu();
                        break;

                    case Success:
                        nowCard = Success;
                        startActivity(new Intent(StartCard.this, MainLine.class));
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

    //給當開啟選單時就會呼叫一次
    @Override
    public boolean onPrepareOptionsMenu (Menu menu)
    {
        //清除之前選單
        menu.clear();

        //宣告選單
        MenuInflater inflater = getMenuInflater();

        //判斷位置取得選單
        switch(nowCard)
        {
            case Sex:
                inflater.inflate(R.menu.sex_menu, menu);
                break;
            case Age:
                inflater.inflate(R.menu.age_menu, menu);
                break;
            default:
                Toast.makeText(StartCard.this, "選單發生錯誤！", LENGTH_LONG).show();
                break;
        }
        return true;
    }

    //點擊選單
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.question:

                return true;
            case R.id.answer:

                return true;
            default:
                return true;
        }
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
