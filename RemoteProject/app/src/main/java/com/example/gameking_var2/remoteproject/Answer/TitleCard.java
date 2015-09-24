package com.example.gameking_var2.remoteproject.Answer;

import com.androidquery.AQuery;
import com.example.gameking_var2.remoteproject.CardsAdapter.CardAdapter;
import com.example.gameking_var2.remoteproject.CardsAdapter.CustomAdapter;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    //用來串流撥放音檔
    private Player player;

    protected ImageLoader imageLoader;
    DisplayImageOptions options;

    String[] promptName,promptStore;
    String msg,Tid;
    int i=0;

    //不知道
    private static final String TAG = TitleCard.class.getSimpleName(),url="http://163.17.135.75";

    //定義卡片順序 方便了解
    static final int HintOne = 0;
    static final int HintTwo = 1;
    static final int HintThree = 2;

    ////上滑動佈景 下是滑動卡片
    private CustomAdapter mAdapter;
    private CardScrollView mCardScroller;

    //定義手勢偵測
    private GestureDetector GestureDetector;

    //提示1、2
    TextView tv1, tv2;

    //提示3
    ImageView iv1;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        Intent intent = this.getIntent();

        //取得傳遞過來的資料
        msg = intent.getStringExtra("msg");
        Tid = intent.getStringExtra("Tid");

        gocreat();
    }

    private void gocreat()
    {
        String[] all;
        all=msg.split("&");

        promptName=all[0].split(",");
        promptStore=all[1].split(",");

        //將卡片類別 傳回來  並用自定義類別"CardAdapter"（覆寫卡片類別）
        mAdapter = new CustomAdapter(createCards(this));

        //預設 抓本體
        mCardScroller = new CardScrollView(this);

        //將本體設定為用好的自定義類別
        mCardScroller.setAdapter(mAdapter);

        //設定場景
        setContentView(mCardScroller);

        //設定提示文字
        tv1.setText(promptName[0]);
        tv2.setText(promptName[1]);

        //設定提示圖片
        iv1.setImageDrawable(loadImageFromURL(url + promptStore[2]));

        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);

    }

    //建立滑動卡片 使用List
    private List<View> createCards(Context context)
    {
        //List的卡片創建
        ArrayList<View> cards = new ArrayList<View>();

        //抓XML的View
        View view_one = View.inflate(context, R.layout.prompt_one, null);
        View view_two = View.inflate(context, R.layout.prompt_two, null);
        View view_three = View.inflate(context, R.layout.prompt_three, null);

        //逐一建造
        cards.add
        (
            HintOne, view_one
        );
        cards.add
        (
            HintTwo, view_two
        );
        cards.add
        (
            HintThree, view_three
        );

        //建立完之後抓元件
        //抓提示1、2 文字欄    Textview
        tv1 = (TextView) view_one.findViewById(R.id.prom_one);
        tv2 = (TextView) view_two.findViewById(R.id.prom_two);

        //抓提示三 ImageView
        iv1 = (ImageView) view_three.findViewById(R.id.prom_three);

        return cards;
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
                //答題
                Intent intent = new Intent();
                intent.setClass(TitleCard.this, Answer.class);
                intent .putExtra("Tid",Tid);//可放所有基本類別

                // 切換Activity
                startActivity(intent);
                finish();
                break;
            case "TWO_TAP":
                //進入選單
                startActivity(new Intent(TitleCard.this, Options.class));
                break;
            case "SWIPE_DOWN":
                finish();
                break;
            case  "SWIPE_RIGHT":
                Playmusic();
                break;
            case "SWIPE_LEFT":
                Playmusic();
                break;
        }
        return true;
    }

    //播放影音檔
    public  void Playmusic()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(1000);
                    if(mCardScroller.getSelectedItemPosition() < 2 )
                    {
                        //設定音檔位置
                        player = new Player("http://163.17.135.75"+promptStore[mCardScroller.getSelectedItemPosition()]);
                        player.play();
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    //從網路加載圖片
    private Drawable loadImageFromURL(String url)
    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable draw = Drawable.createFromStream(is, "src");
            is.close();
            return draw;
        }
        catch (Exception e)
        {
            //TODO handle error
            Log.i("loadingImg", e.toString());
            return null;
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

    private View buildView()
    {
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);
        card.setText("題目提示與照片頁面");
        return card.getView();
    }
}
