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

import com.example.glass123.glasslogin.Draw.DrawTest;
import com.example.glass123.glasslogin.Gps.G;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.Sensor.Acceleration;
import com.example.glass123.glasslogin.Sensor.Sen;

import java.io.IOException;

public class FindQuestion extends Activity  implements SurfaceHolder.Callback {
    public static int monitor_Width ;
    public static int monitor_Height ;

    private Button bt;
    private DrawTest drawTest = null;
    private SurfaceView svCamera = null;
    protected SurfaceHolder mSurfaceHolder;


    boolean previewing = false;
    Camera myCamera;

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

        ag.update(24.152214,120.675439);

        //取得陀螺儀控制
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //自定義加速度類別
        ac = new Acceleration(sm);
        //自定義方位類別
        senor = new Sen(sm,ac,ag);


        setContentView(R.layout.activity_find_question);

        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        monitor_Width = metrics.widthPixels;      //取得螢幕的寬度
        monitor_Height = metrics.heightPixels;    //取得螢幕的高度*/

        drawTest = (com.example.glass123.glasslogin.Draw.DrawTest) findViewById(R.id.svDraw);
        svCamera = (SurfaceView) findViewById(R.id.svCamera);

        mSurfaceHolder = svCamera.getHolder();
        mSurfaceHolder.addCallback(this);
        // 当设置为SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS后就不能绘图了
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


/*

        //自定義GPS類別
        //mlocation  = (LocationManager)getSystemService(LOCATION_SERVICE);
        //g = new G(mlocation,this,tv3);*/

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
