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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static android.widget.Toast.LENGTH_LONG;

/*
進入題目顯示頁面
功能：
１．顯示兩個提示與照片
２．單指單擊跳到選項頁面 Option
３．三指單擊語音輸入答案 跳到Answer
*/

public class TitleCard extends Activity implements GestureDetector.BaseListener,TextToSpeech.OnInitListener
{
    private Handler  handler  = new Handler();
    Bitmap bitmap;

    private int MY_DATA_CHECK_CODE = 0;
    TextToSpeech tts;

    protected ImageLoader imageLoader;
    DisplayImageOptions options;

    Timer timer;

    String[] promptName;
    String msg,Tid;
    int i=0, AnswerTimer=1;

    //不知道
    private static String url="http://163.17.135.76";

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

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

    }

    private void gocreat()
    {
        promptName=msg.split("&");


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

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                bitmap = getBitmapFromURL(url+promptName[2]);
                handler.post(goui);
            }

        }).start();


        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);

        timer = new Timer();
        timer.schedule(task,0,1000);

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
                timer.cancel();

                //答題
                Intent intent = new Intent();
                intent.setClass(TitleCard.this, Answer.class);
                intent .putExtra("Tid", Tid);//可放所有基本類別
                intent .putExtra("answerTime",String.valueOf(AnswerTimer));

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
                if(i < 3)
                {
                    i++;
                }
                Playmusic();
                break;
            case "SWIPE_LEFT":
                if(i != 0)
                {
                    i--;
                }
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
                    if(i < 2 )
                    {
                       // player = new Player("http://163.17.135.76"+promptStore[i]);
                       // player.play();
                        tts.speak(promptName[i], TextToSpeech.QUEUE_ADD, null);
                    }
            }

        }).start();
    }

    //從網路加載圖片
    public Bitmap getBitmapFromURL(String imageUrl){
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            //縮小圖片
            int toWidth = 640;
            int toHeight = 360;


            Bitmap bb = bitmap;

            int bmpWidth  = bb.getWidth();

            //取得圖檔高度
            int bmpHeight  = bb.getHeight();

            float scale;
            if (bmpWidth > bmpHeight) {
                scale = (float) toWidth/bmpWidth;
            }else {
                scale = (float) toHeight/bmpHeight;
            }

            //轉換矩陣
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            //產生縮圖
            bitmap = Bitmap.createBitmap(bb, 0, 0, bmpWidth, bmpHeight,matrix, true);



            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    //更新UI
    final Runnable goui = new Runnable()
    {
        @Override
        public void run()
        {
            iv1.setImageBitmap(bitmap);
        }
    };



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                tts = new TextToSpeech(this, this);
            }
            else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }


    //計算思考時間
    private TimerTask task = new TimerTask(){
        @Override
        public void run() {
            AnswerTimer++;
    //            Message message = new Message();
    //            message.what = 1;
    //            handler.sendMessage(message);
        }
    };













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

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(TitleCard.this,
                    "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
        }
        else if (status == TextToSpeech.ERROR) {
            Toast.makeText(TitleCard.this,
                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
        }

    }
}
