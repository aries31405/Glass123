package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass123.glasslogin.Draw.Data;
import com.example.glass123.glasslogin.Draw.DrawTest;
import com.example.glass123.glasslogin.Gps.G;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.Sensor.Acceleration;
import com.example.glass123.glasslogin.Sensor.Sen;

import java.io.IOException;
import java.util.ArrayList;

public class FindQuestion extends Activity  implements SurfaceHolder.Callback {
    public static int monitor_Width ;
    public static int monitor_Height ;

    private Button bt;
    private DrawTest drawTest = null;
    private SurfaceView svCamera = null;
    protected SurfaceHolder mSurfaceHolder;

    TextView tv3;

    boolean previewing = false;
    Camera myCamera;

    public static ArrayList<Data> Au;

    Angle ag = new Angle();
    G g;
    SensorManager sm;
    Sen senor;
    Acceleration ac;

    LocationManager mlocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bt = (Button)findViewById(R.id.button2);

        double[][] laon = new double[4][2];

        laon[0][0] =24.152792;
        laon[0][1] =120.675922;

        laon[1][0] =24.151607;
        laon[1][1] =120.675868;

        laon[2][0] =24.151793;
        laon[2][1] =120.674849;

        laon[3][0] =24.152870;
        laon[3][1] =120.674913;


        Au = new ArrayList<Data>();
        Au.clear();  //先清除 Au 物件陣列

        //建立 AndroidUnit 物件 10 隻
        for(int i=0; i< laon.length; i++) {
            //產生 AndroidUnit 實體 au
            Data au = new Data(laon[i][0],laon[i][1],i);
            //陸續將 au 放入 Au 物件陣列中
            Au.add(au);
        }


        //取得陀螺儀控制
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //自定義加速度類別
        //ac = new Acceleration(sm);
        //自定義方位類別
        senor = new Sen(sm);


        setContentView(R.layout.activity_find_question);

        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        monitor_Width = metrics.widthPixels;      //取得螢幕的寬度
        monitor_Height = metrics.heightPixels;    //取得螢幕的高度*/


        //自定義GPS類別
        mlocation  = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        g = new G();

        if (mlocation.isProviderEnabled(LocationManager.GPS_PROVIDER) || mlocation.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            mlocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0,g);
        } else {
            Toast.makeText(FindQuestion.this, "請開啟定位服務", Toast.LENGTH_LONG).show();
        }

        drawTest = (com.example.glass123.glasslogin.Draw.DrawTest) findViewById(R.id.svDraw);
        svCamera = (SurfaceView) findViewById(R.id.svCamera);

        mSurfaceHolder = svCamera.getHolder();
        mSurfaceHolder.addCallback(this);
        // 当设置为SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS后就不能绘图了
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);




    }

    public void onPause(){
        //senor.stop();
        //ac.stop();
        super.onPause();
    }


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
}
