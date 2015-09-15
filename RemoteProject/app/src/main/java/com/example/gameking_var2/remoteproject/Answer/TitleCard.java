package com.example.gameking_var2.remoteproject.Answer;

import com.example.gameking_var2.remoteproject.CardsAdapter.CardAdapter;
import com.example.gameking_var2.remoteproject.Http.GetServerMessage;
import com.example.gameking_var2.remoteproject.Mplayer.Player;
import com.example.gameking_var2.remoteproject.R;
import com.google.android.glass.app.Card;
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
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/*
進入題目顯示頁面
功能：
１．顯示兩個提示與照片
２．單指單擊跳到選項頁面 Option
３．三指單擊語音輸入答案 跳到Answer
*/

public class TitleCard extends Activity implements GestureDetector.BaseListener
{
    private Player player;
    String[] promptName,promptStore;
    String msg;


    //不知道
    private static final String TAG = TitleCard.class.getSimpleName();

    //定義卡片順序 方便了解
    static final int HintOne = 0;
    static final int HintTwo = 1;
    static final int HintThree = 2;

    ////上滑動佈景 下是滑動卡片
    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    //定義手勢偵測
    private GestureDetector GestureDetector;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        Intent intent = this.getIntent();
        //取得傳遞過來的資料
        msg = intent.getStringExtra("msg");

        gocreat();
    }

    private void gocreat()
    {
        String[] all;
        all=msg.split("&");

        promptName=all[0].split(",");
        promptStore=all[1].split(",");

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

        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);

    }


    //建立滑動卡片 使用List
    private List<CardBuilder> createCards(Context context)
    {
        //List的卡片創建
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();

        //逐一建造
        cards.add
        (
            HintOne, new CardBuilder(context, CardBuilder.Layout.MENU).setText(promptName[0])
        );
        cards.add
        (
            HintTwo, new CardBuilder(context, CardBuilder.Layout.MENU).setText(promptName[1])
        );
        cards.add
        (
            HintThree, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.bg01)
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
                    case HintOne:
                        player = new Player("http://163.17.135.75"+promptStore[0]);
                        player.play();
                        break;

                    case HintTwo:
                        player = new Player("http://163.17.135.75"+promptStore[1]);
                        player.play();
                        break;

                    case HintThree:

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
            case "TWO_TAP":
                //答題
                startActivity(new Intent(TitleCard.this, Answer.class));
                break;
            case "THREE_TAP":
                //進入選單
                startActivity(new Intent(TitleCard.this, Options.class));
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
