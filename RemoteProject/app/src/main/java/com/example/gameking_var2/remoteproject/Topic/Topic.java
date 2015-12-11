package com.example.gameking_var2.remoteproject.Topic;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.gameking_var2.remoteproject.CardsAdapter.CustomAdapter;
import com.example.gameking_var2.remoteproject.CardsAdapter.Imageview;
import com.example.gameking_var2.remoteproject.CardsAdapter.Toictext;
import com.example.gameking_var2.remoteproject.Http.GetServerMessage;
import com.example.gameking_var2.remoteproject.Http.UploadImage;
import com.example.gameking_var2.remoteproject.R;
import com.google.android.glass.app.Card;
import com.google.android.glass.content.Intents;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孔雀舞 on 2015/9/17.
 */
public class Topic  extends Activity  implements GestureDetector.BaseListener,LocationListener{
    LocationManager mlocation;
    private double latitude=0.0,longitude=0.0;
    String[] Ttext = new String[2];
    private Handler handler  = new Handler();
    //計算已出的卡片
    int ii[] = {0,0,0};

    protected static final int RESULT_SPEECH = 1,TAKE_PICTURE_REQUEST = 007;

    String floor,Topic=null,msg,id,titleId,picturePath;
    Card card;
    Bitmap bitmap;


    //上滑動佈景 下是滑動卡片
    //private CardScrollAdapter mAdapter;
    private CustomAdapter mAdapter;
    private CardScrollView mCardScroller;

    //定義手勢偵測
    private GestureDetector GestureDetector;

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        //暫定6樓
        floor ="6";

        //將卡片類別 傳回來  並用自定義類別"CardAdapter"（覆寫卡片類別）
        mAdapter = new CustomAdapter(createCards(this));

        //預設 抓本體
        mCardScroller = new CardScrollView(this);

        //將本體設定為用好的自定義類別
        mCardScroller.setAdapter(mAdapter);

        //設定場景
        setContentView(mCardScroller);


        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);


        try//取得ID
        {
            FileInputStream in = openFileInput("Id.txt");
            byte[] data = new byte[128];
            in.read(data);
            in.close();
            id = new String(data);
        }
        catch(IOException e)
        {

        }

        //設定向使用者連接的手持裝置取得位置
        mlocation  = (LocationManager)getSystemService(LOCATION_SERVICE);
        mlocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0,Topic.this);

    }

    //------------------------------建立卡片-----------------------------//

    //建立滑動卡片 使用List
    private List<View> createCards(Context context)
    {
        //List的卡片創建
        ArrayList<View> cards = new ArrayList<View>();

        //抓XML的View
        View search_view = View.inflate(context, R.layout.topic, null);

        //建立尋找頁面
        cards.add
                (
                        0, search_view
                );

        return cards;
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
            case "TAP":
                if(ii[0] == 1 &&  ii[1] == 1)
                {
                    startCapture();
                }
                else
                {
                    speech();
                }

                break;
            case "TWO_TAP":
                ii[mCardScroller.getSelectedItemPosition()-1] = 0;
                deleteCard(mCardScroller.getSelectedItemPosition());
                break;
            case "LONG_PRESS":
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        GetServerMessage message = new GetServerMessage();
                        titleId = message.all("http://163.17.135.76/glass/add_title.php","UserId="+id+"&x="+ latitude+"&y="+longitude+"&floor="+6);
                        handler.post(add_image);
                    }

                }).start();
                break;
        }
        return false;
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

    //新增卡片
    private void insertNewCard(int position,int tori)
    {
        //新增的卡片
        View addTitleCard;
        //新增的卡片
        if(tori == 0)
        {
            //提示文字
             addTitleCard = new Toictext(this, R.layout.topic_text,Topic);
        }
        else
        {
            //提示圖片
            addTitleCard = new Imageview(this, R.layout.topic_image, bitmap);
        }



        //進行新增  Adapter裡的變數(CardBuilder)
        mAdapter.insertCard(position, addTitleCard);

        //將現在的卡片進行更新(新增)
        mCardScroller.animate(position, CardScrollView.Animation.INSERTION);


    }

    //---------------------------------啟用語音輸入---------------------------//
    private void speech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.EXTRA_LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-us");

        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(), "Ops! Your device doesn't support Speech to Text", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    //-----------------------------------啟動拍照函示---------------------//

    private void startCapture()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //語音輸入文字
                    Topic=text.get(0).toString();

                    if(ii[0] == 0)
                    {
                        //新增卡片
                        insertNewCard(1,0);
                        Ttext[0] = Topic;
                        ii[0] = 1;
                    }
                    else
                    {
                        //新增卡片
                        insertNewCard(2,0);
                        Ttext[1] = Topic;
                        ii[1] = 1;
                    }
                }
                break;
            }
            case TAKE_PICTURE_REQUEST:
            {
                if (resultCode == RESULT_OK && null != data) {
                    Bundle extras = data.getExtras();

                    String pctureFilePath = extras.getString(Intents.EXTRA_PICTURE_FILE_PATH);

                    processPictureWhenReady(pctureFilePath);
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processPictureWhenReady(final String picturePath){
        this.picturePath = picturePath;
        final File pictureFile = new File(picturePath);

        if(pictureFile.exists())
        {

            int toWidth = 640;
            int toHeight = 360;

            //照片準備好
            File imgFile = new  File(picturePath);

            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

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

            insertNewCard(3,1);
            ii[2] = 1;

        }
        else
        {
            final File parentDirectory = pictureFile.getParentFile();
            FileObserver observer = new FileObserver(parentDirectory.getPath()) {
                // Protect against additional pending events after CLOSE_WRITE is
                // handled.
                private boolean isFileWritten;
                @Override
                public void onEvent(int event, String path) {
                    if (!isFileWritten) {
                        // For safety, make sure that the file that was created in
                        // the directory is actually the one that we're expecting.
                        File affectedFile = new File(parentDirectory, path);
                        isFileWritten = (event == FileObserver.CLOSE_WRITE && affectedFile.equals(pictureFile));

                        if (isFileWritten) {
                            stopWatching();
                            // Now that the file is ready, recursively call
                            // processPictureWhenReady again (on the UI thread).
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    processPictureWhenReady(picturePath);
                                }
                            });
                        }
                    }
                }};
            observer.startWatching();
        }
    }
    

    //執行緒
    final Runnable add_image = new Runnable()
    {
        @Override
        public void run()
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    UploadImage uploadImage = new UploadImage();
                    uploadImage.uploadFile(picturePath);
                    handler.post(add_prompt);
                }

            }).start();
        }
    };

    final Runnable add_prompt = new Runnable()
    {
        @Override
        public void run()
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    GetServerMessage message = new GetServerMessage();
                    msg = message.all("http://163.17.135.76/glass/add_prompt.php","titleId="+titleId.trim()+"&p1="+ Ttext[0]+"&p2="+Ttext[1]+"&p3="+"three");
                }

            }).start();
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


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        mlocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0,Topic.this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
