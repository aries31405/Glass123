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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass123.glasslogin.Gps.G;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.Sensor.Acceleration;
import com.example.glass123.glasslogin.Sensor.Sen;

import java.io.IOException;

public class FindQuestion extends Activity implements SurfaceHolder.Callback{

    G g;
    SensorManager sm;
    Sen senor;
    Acceleration ac;
    TextView tv,tv2,tv3;

    LocationManager mlocation;

    Camera myCamera;
    SurfaceView previewSurfaceView;
    SurfaceHolder previewSurfaceHolder;
    boolean previewing = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_question);

        tv = (TextView)findViewById(R.id.textView4);
        tv2 = (TextView)findViewById(R.id.textView6);
        tv3 = (TextView)findViewById(R.id.textView7);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        previewSurfaceView = (SurfaceView)findViewById(R.id.previewsurface);
        previewSurfaceHolder = previewSurfaceView.getHolder();
        previewSurfaceHolder.addCallback(this);
        previewSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //取得陀螺儀控制
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //自定義加速度類別
        ac = new Acceleration(tv2,sm);
        //自定義方位類別
        senor = new Sen(tv,sm,ac);
        //自定義GPS類別
        mlocation  = (LocationManager)getSystemService(LOCATION_SERVICE);
        g = new G(mlocation,this,tv3);


        Toast.makeText(FindQuestion.this,String.valueOf(gps2d(24.152214, 120.675439,24.151798, 120.675637)),Toast.LENGTH_SHORT).show();


        /*RelativeLayout relativeLayout = (RelativeLayout) super.findViewById(R.id.rlId);
        //RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
        Button bt = new Button(this);
        relativeLayout.addView(bt, /*relativeParams150,50);*/
    }

    private double gps2d(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        lat_a=lat_a*Math.PI/180;
        lng_a=lng_a*Math.PI/180;
        lat_b=lat_b*Math.PI/180;
        lng_b=lng_b*Math.PI/180;

        d=Math.sin(lat_a)*Math.sin(lat_b)+Math.cos(lat_a)*Math.cos(lat_b)*Math.cos(lng_b-lng_a);
        d=Math.sqrt(1-d*d);
        d=Math.cos(lat_b)*Math.sin(lng_b - lng_a) /d;
        d=Math.asin(d)*180/Math.PI;

        //d = Math.round(d*10000);

        return d;
    }

    public void onPause(){
        senor.stop();
        ac.stop();
        super.onPause();
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
            // TODO Auto-generated catch block
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
