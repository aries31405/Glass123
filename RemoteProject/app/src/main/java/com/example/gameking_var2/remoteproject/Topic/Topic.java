package com.example.gameking_var2.remoteproject.Topic;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.CameraProfile;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.CaptioningManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameking_var2.remoteproject.CardsAdapter.CardAdapter;
import com.example.gameking_var2.remoteproject.CardsAdapter.CustomAdapter;
import com.example.gameking_var2.remoteproject.CardsAdapter.Imageview;
import com.example.gameking_var2.remoteproject.Http.GetServerMessage;
import com.example.gameking_var2.remoteproject.R;
import com.google.android.glass.app.Card;
import com.google.android.glass.content.Intents;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by 孔雀舞 on 2015/9/17.
 */
public class Topic  extends Activity  implements GestureDetector.BaseListener{

    private static final String NAMESPACE = "http://tts.itri.org.tw";
    private static final String URL = "http://tts.itri.org.tw/TTSService/Soap_1_3.php?wsdl";

    private static final String Account = "s1100b026";
    private static final String Password = "Ss23851339";
    private String Result_Soap;
    private String text;//TTS文字
    private String id;//轉換後ID
    private String url;//下載網址

    String[] Ttext = new String[2];

    //計算已出的卡片
    int i =1;

    protected static final int RESULT_SPEECH = 1,TAKE_PICTURE_REQUEST = 007;

    String floor,Topic=null;
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
               /* if(i < 3 )
                {
                    speech();
                }
                else
                {*/
                    startCapture();
               // }

                break;
            case "TWO_TAP":
                if(mCardScroller.getSelectedItemPosition() != 0 && mCardScroller.getSelectedItemPosition() != 3)
                {
                    deleteCard(mCardScroller.getSelectedItemPosition());
                    i =i - 1;
                }
                else if(mCardScroller.getSelectedItemPosition() != 0)
                {
                    deleteCard(mCardScroller.getSelectedItemPosition());
                }

                break;
            case "LONG_PRESS":

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
    private void insertNewCard(int position)
    {
        //新增的卡片
        //CardBuilder card = new CardBuilder(this, CardBuilder.Layout.MENU);
       /* if(i < 3)
        {
            //提示文字
            card.setText(Topic);
        }
        else
        {*/
            //提示圖片
        //}

        //進行新增  Adapter裡的變數(CardBuilder)
       /* mAdapter.insertCard(position, card);

        //將現在的卡片進行更新(新增)
        mCardScroller.animate(position, CardScrollView.Animation.INSERTION);*/

        //新增的卡片
        View addTitleCard = new Imageview(this, R.layout.topicimage, bitmap);

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

                    //新增卡片
                    insertNewCard(i);
                    Ttext[i-1] = Topic;
                    i++;
                }
                break;
            }
            case TAKE_PICTURE_REQUEST:
            {
                if (resultCode == RESULT_OK && null != data) {
                    Bundle extras = data.getExtras();

                    String pctureFilePath = extras.getString(Intents.EXTRA_PICTURE_FILE_PATH);

                    /*//照片準備好
                    File imgFile = new  File(pctureFilePath);

                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());*/

                    processPictureWhenReady(pctureFilePath);
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processPictureWhenReady(final String picturePath){
        final File pictureFile = new File(picturePath);

        if(pictureFile.exists())
        {

            //照片準備好
            File imgFile = new  File(picturePath);

            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            Bitmap bb = bitmap;

            int bmpWidth  = bb.getWidth();

            //取得圖檔高度
            int bmpHeight  = bb.getHeight();

            //設定縮圖寬度
            float scaleWidth  = (float)  2000/ bmpWidth;
            //按固定大小缩放sWidth,要多大有多大

            //設定縮圖高度
            float scaleHeight = (float) 1600/ bmpHeight;

            //轉換矩陣
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);

            //產生縮圖
            bitmap = Bitmap.createBitmap(bb, 0, 0, bmpWidth, bmpHeight,matrix, false);

            insertNewCard(i);
            i++;

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


    //----------------------------------------TTS--------------------------------

    //將文字轉換取得ID
    public void CallSoapConvertSimple() {
        String METHOD_NAME = "ConvertSimple";
        String SOAP_ACTION = "http://tts.itri.org.tw/TTSService/"+METHOD_NAME;

        try {
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("accountID", Account);
            rpc.addProperty("password", Password);
            rpc.addProperty("TTStext", text);
            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            ht.call(SOAP_ACTION, envelope);

            SoapObject result = (SoapObject) envelope.bodyIn;
            Result_Soap=result.getProperty(0).toString();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String all[] = Result_Soap.split("&");
                    id = all[2];
                }


            });
        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
            e.printStackTrace();
        }
    }

    //'提供ID取得下載網址
    public void CallSoapGetConvertStatus() {
        String METHOD_NAME = "GetConvertStatus";
        String SOAP_ACTION = "http://tts.itri.org.tw/TTSService/"+METHOD_NAME;

        try {
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("accountID", Account);
            rpc.addProperty("password", Password);
            rpc.addProperty("convertID", id);

            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            ht.call(SOAP_ACTION, envelope);

            SoapObject result = (SoapObject) envelope.bodyIn;
            final String Result=result.getProperty(0).toString();
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    String[] all2;
                    all2 =  Result.toString().split("&");
                    url = all2[all2.length-1];

                    GetServerMessage message = new GetServerMessage();
                    String msg = message.all("http://163.17.135.75/TTS/down.php", "url=" + url);
                }});

        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
            e.printStackTrace();
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
