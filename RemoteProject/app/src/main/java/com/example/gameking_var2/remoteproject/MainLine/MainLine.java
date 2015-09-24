package com.example.gameking_var2.remoteproject.MainLine;

import com.example.gameking_var2.remoteproject.CardsAdapter.CardAdapter;
import com.example.gameking_var2.remoteproject.Profile.Profile;
import com.example.gameking_var2.remoteproject.R;
import com.example.gameking_var2.remoteproject.Select_QorA.Selectqa;
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
主畫面
功能：
１．開始遊戲
２．個人紀錄
３．關於
－－－－－－
開始連結至Select_QorA
個人紀錄會開啟選單，尚未連結
關於目前跳出Toast
*/

public class MainLine extends Activity
{
    //不知道
    private static final String TAG = MainLine.class.getSimpleName();

    //定義卡片順序 方便了解
    static final int Start = 0;
//    static final int Record = 1;
    static final int About = 1;

    //上滑動佈景 下是滑動卡片
    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    // 使用者資料
    private Profile mProfile;

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
            Start, new CardBuilder(context, CardBuilder.Layout.MENU).setText("Start").setIcon(R.drawable.ic_select_link_50)
        );
        /*
        cards.add
        (

            Record, new CardBuilder(context, CardBuilder.Layout.MENU).setText("Record").setIcon(R.drawable.ic_select_link_50)
        );
        */
        cards.add
        (
            About, new CardBuilder(context, CardBuilder.Layout.MENU).setText("About").setIcon(R.drawable.ic_select_link_50)
        );
/*
            Record, new CardBuilder(context, CardBuilder.Layout.MENU).setText("紀錄").setIcon(R.drawable.ic_select_link_50)
        );
        cards.add
        (
            1, new CardBuilder(context, CardBuilder.Layout.MENU).setText("關於").setIcon(R.drawable.ic_select_link_50)
        );
        */
        return cards;
    }

    //設定卡片點擊監聽
    private void setCardScrollerListener()
    {
        //卡片的View 設定監聽
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //不知道
                Log.d(TAG, "Clicked view at position " + position + ", row-id " + id);
                int soundEffect = Sounds.TAP;

                //判斷點擊哪個卡片
                switch (position)
                {
                    case Start:
                        startActivity(new Intent(MainLine.this, Selectqa.class));
                        break;

                    /*
                    case Record:
                        //openOptionsMenu();
                        Toast.makeText(MainLine.this, "Prowered by 成之內貳點零 Version b1.0", LENGTH_LONG).show();
                        break;
*/
                    case About:
                        Toast.makeText(MainLine.this, "Powered by 成之內貳點零 Version b1.0", LENGTH_LONG).show();
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

    //建立選單
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.record_meun, menu);
        return true;
    }

    //點擊選單
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.question:
                Toast.makeText(MainLine.this, "出題", LENGTH_LONG).show();
                return true;
            case R.id.answer:
                Toast.makeText(MainLine.this, "解題", LENGTH_LONG).show();
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
