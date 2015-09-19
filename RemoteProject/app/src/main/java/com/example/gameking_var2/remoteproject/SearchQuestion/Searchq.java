package com.example.gameking_var2.remoteproject.SearchQuestion;

import com.example.gameking_var2.remoteproject.Answer.TitleCard;
import com.example.gameking_var2.remoteproject.CardsAdapter.CardAdapter;
import com.example.gameking_var2.remoteproject.Http.GetServerMessage;
import com.example.gameking_var2.remoteproject.R;
import com.example.gameking_var2.remoteproject.Select_QorA.Selectqa;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/*
尋找題目頁面  一個題目動態新增一張卡片
在該卡片雙指跳出題目評價
三指選擇樓層
*/


public class Searchq extends Activity implements GestureDetector.BaseListener,LocationListener
{private ViewFlipper vfSex;
    //不知道
    private static final String TAG = Searchq.class.getSimpleName();

    String[] all;
    private double latitude=0.0,longitude=0.0;
    private int i =0;

    //播放語音
    private static MediaPlayer mp = new MediaPlayer();

    //查詢資料庫 執行續
    private Handler handler  = new Handler();
    private Thread thread;
    private String allurl = "http://163.17.135.75/glass/question_localtion.php",alldata = "",msg;

    //樓層暫定6樓
    String floor = "6";

    //上滑動佈景 下是滑動卡片
    //private CardScrollAdapter mAdapter;
    private CardAdapter mAdapter;
    private CardScrollView mCardScroller;

    //定義手勢偵測
    private GestureDetector GestureDetector;

    //點點
    TextView tv1;

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


        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);

        //設定場景
        setContentView(mCardScroller);

        //取得此層題目位置
        thread = new Thread(getlocal);
        thread.start();

        tv1.setText("...");
    }

    //------------------------------建立卡片-----------------------------//

    //建立滑動卡片 使用List
    private List<CardBuilder> createCards(Context context)
    {
        //List的卡片創建
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();

        //建立卡片建立元件
        CardBuilder cb1 = new CardBuilder(context, CardBuilder.Layout.EMBED_INSIDE).setEmbeddedLayout(R.layout.search_layout);

        View view = cb1.getView();

        //抓點點文字View
        tv1 = (TextView) view.findViewById(R.id.dot_d);

        tv1.setText("......");

        //建立尋找頁面
        cards.add
        (
            0, cb1
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

                //判斷點擊哪個卡片(點擊後進入題目 跳到TitleCard)
                switch (position)
                {
                    case 0:
                        insertNewCard(1);
                        break;
                    case 1:
                        deleteCard(1);
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

    //----------------------變更卡片---------------------//

    //刪除卡片
    private void deleteCard(int position)
    {
        //刪除卡片  刪除Adapter裡的CardBuilder之一
        mAdapter.deleteCard(position);

        //將現在卡片進行刪除
        mCardScroller.animate(position, CardScrollView.Animation.DELETION);
    }

    //移動卡片
    private void navigateToCard(int position)
    {
        //將現在的卡片進行移動位置
        mCardScroller.animate(position, CardScrollView.Animation.NAVIGATION);
    }

    //新增卡片
    private void insertNewCard(int position)
    {
        //新增的卡片
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE);
        card.setEmbeddedLayout(R.layout.sex);

        //進行新增  Adapter裡的變數(CardBuilder)
        mAdapter.insertCard(position, card);

        //將現在的卡片進行更新(新增)
        mCardScroller.animate(position, CardScrollView.Animation.INSERTION);

        createMenu();
    }

    //----------------------選單------------------------//

    //建立選單 樓層
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floor_menu, menu);
        return true;
    }

    //點擊選單
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

        }
        return true;
    }

    //---------------------------手勢偵測------------------------------//

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
            case "SWIPE_DOWN":
                finish();
                break;
            case "TWO_TAP":
                //跳出題目評價
               // Toast.makeText(Searchq.this, "跳出題目評價", Toast.LENGTH_SHORT).show();
                Toast.makeText(Searchq.this,"找到題目", LENGTH_LONG).show();
                if(mp.isPlaying())
                    mp.pause();
                mp.seekTo(0);
                mp.setVolume(1000, 1000);//設置找到題目聲音
                mp.start();

                //設置執行續使用資料庫
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        GetServerMessage message = new GetServerMessage();
                        msg = message.all("http://163.17.135.75/glass/question.php","titleId=2");
                        handler.post(getprompt);
                    }

                }).start();
                break;
            case "THREE_TAP":
                //樓層選單
                openOptionsMenu();
                break;
        }
        return false;
    }

    //-----------------------GPS-------------------------//

    //執行續
    final Runnable getlocal = new Runnable()
    {
        @Override
        public void run()
        {
            alldata = "floor="+floor;
            GetServerMessage message = new GetServerMessage();
            msg = message.all(allurl,alldata);
            handler.post(updata);
        }
    };

    final Runnable updata = new Runnable()
    {
        @Override
        public void run()
        {
            if(!msg.equals("No wifi"))
            {
                //設定向使用者連接的手持裝置取得位置
                LocationManager mlocation  = (LocationManager)getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(true);
                List<String> providers = mlocation.getProviders(criteria,true);

                for(String provider : providers)
                {
                    mlocation.requestLocationUpdates(provider,1000,0,Searchq.this);
                }

                try {
                    //R.raw.error 是ogg格式的音頻 放在res/raw/下
                    AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.error);
                    mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mp.setAudioStreamType(AudioManager.STREAM_RING);
                    afd.close();
                    mp.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    final Runnable getprompt = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                Thread.sleep(1500);
                Intent intent = new Intent();
                intent.setClass(Searchq.this,TitleCard.class);
                intent .putExtra("msg", msg);//可放所有基本類別
                // 切換Activity
                startActivity(intent);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    //當位置變動時更新
    @Override
    public void onLocationChanged(Location location)
    {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if(i ==0)
        {
            Toast.makeText(Searchq.this,"找到題目", LENGTH_LONG).show();
            if(mp.isPlaying())
                mp.pause();
            mp.seekTo(0);
            mp.setVolume(1000, 1000);//設置聲音
            mp.start();

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    GetServerMessage message = new GetServerMessage();
                    msg = message.all("http://163.17.135.75/glass/question.php","titleId=2");
                    handler.post(getprompt);
                }

            }).start();
        }
        i = 1;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    //---------------------無---------------------------//

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

    public void createMenu()
    {


        vfSex = (ViewFlipper) findViewById(R.id.vf_sex);

        TextView tv = new TextView(this);
        tv.setText("男");

        LinearLayout lq = new LinearLayout(this);
        lq.addView(tv);

        TextView tv2 = new TextView(this);
        tv2.setText("女");

        LinearLayout lq2 = new LinearLayout(this);
        lq2.addView(tv2);

        vfSex.addView(lq);

        vfSex.addView(lq2);

        int i = 0;


/*
        for(i = 0; i < 30; i++)
        {

            TextView tv = new TextView(this);
            tv.setText("男");

            LinearLayout lq = new LinearLayout(this);
            lq.addView(tv);


            viewFlipper.addView(lq);
        }
*/
        //vfSex.setOnTouchListener(this);
    }

}
