package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Handler;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.GetServerMessage;
import com.example.glass123.glasslogin.Draw.Data;
import com.example.glass123.glasslogin.Draw.DrawTest;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.Sensor.Acceleration;
import com.example.glass123.glasslogin.Sensor.Sen;
import com.example.glass123.glasslogin.Split.Sp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;

public class FindQuestion extends Activity  implements SurfaceHolder.Callback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static double latitude=0.0,longitude=0.0,nowlatitude=0.0,nowlongitude=0.0;

    private String allmsg;

    private Button bt;
    private DrawTest drawTest = null;
    private SurfaceView svCamera = null;
    protected SurfaceHolder mSurfaceHolder;

    boolean previewing = false;
    Camera myCamera;

    public static ArrayList<Data> Au;

    SensorManager sm;
    Sen senor;
    Acceleration ac;
    Sp sp;

    Handler handler = new Handler();

    // Google API用戶端物件
    private GoogleApiClient googleApiClient;

    // Location請求物件
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bt = (Button)findViewById(R.id.button2);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                GetServerMessage message = new GetServerMessage();
                allmsg = message.all("http://163.17.135.76/glass/question_search.php","UserId="+"20151211151346511431");
                handler.post(split);
            }

        }).start();

        //取得陀螺儀控制
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //自定義加速度類別
        //ac = new Acceleration(sm);
        //自定義方位類別
        senor = new Sen(sm);

        // 建立Google API用戶端物件
        configGoogleApiClient();
        // 建立Location請求物件
        configLocationRequest();
        //啟動
        googleApiClient.connect();

    }

    public void onPause(){
        //senor.stop();
        //ac.stop();
        super.onPause();
    }

    final Runnable split = new Runnable()
    {
        @Override
        public void run()
        {
            sp = new Sp(allmsg);
            handler.post(uiupdate);
        }
    };

    final Runnable uiupdate = new Runnable()
    {
        @Override
        public void run()
        {
            Au = new ArrayList<Data>();
            Au.clear();  //先清除 Au 物件陣列

            //建立 AndroidUnit 物件 10 隻
            for(int i=0; i< sp.geti(); i++) {
                //產生 AndroidUnit 實體 au
                Data au = new Data(sp.getX(i),sp.getY(i),sp.gettitleId(i),sp.getstatus(i));
                //陸續將 au 放入 Au 物件陣列中
                Au.add(au);
            }

            setContentView(R.layout.activity_find_question);
            drawTest = (com.example.glass123.glasslogin.Draw.DrawTest) findViewById(R.id.svDraw);

            //camera
            svCamera = (SurfaceView) findViewById(R.id.svCamera);

            mSurfaceHolder = svCamera.getHolder();
            mSurfaceHolder.addCallback(FindQuestion.this);
            // 置为SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS后就不能绘图了
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    };


    public void click(View v)
    {
        finish();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        myCamera = Camera.open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(previewing){
            myCamera.stopPreview();
            previewing = false;
        }

        try {
            myCamera.setPreviewDisplay(holder);
            myCamera.startPreview();
            previewing = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        myCamera.stopPreview();
        myCamera.release();
        myCamera = null;
        previewing = false;
    }

    // 建立Google API用戶端物件
    private synchronized void configGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // 建立Location請求物件
    private void configLocationRequest() {
        locationRequest = new LocationRequest();
        // 設定讀取位置資訊的間隔時間為一秒（1000ms）
        locationRequest.setInterval(1000);
        // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
        locationRequest.setFastestInterval(1000);
        // 設定優先讀取高精確度的位置資訊（GPS）
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // 已經連線到Google Services
        // 啟動位置更新服務
        // 位置資訊更新的時候，應用程式會自動呼叫LocationListener.onLocationChanged
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, FindQuestion.this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Google Services連線中斷
        // int參數是連線中斷的代號
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Services連線失敗
        // ConnectionResult參數是連線失敗的資訊
        int errorCode = connectionResult.getErrorCode();

        // 裝置沒有安裝Google Play服務
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(this, "裝置沒有安裝Google Play服務", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

       // latitude = location.getLatitude();
        //longitude = location.getLongitude();

        latitude = 24.149384;
        longitude = 120.683561;

        if(nowlatitude == 0.0)
        {
            nowlatitude = latitude;
            nowlongitude = longitude;
        }

    }
}
