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

    Rl rl ;
    Angle ag = new Angle();
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

        RelativeLayout relativeLayout = (RelativeLayout) super.findViewById(R.id.rlId);

        tv = (TextView)findViewById(R.id.textView4);
        tv2 = (TextView)findViewById(R.id.textView6);
        tv3 = (TextView)findViewById(R.id.textView7);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        previewSurfaceView = (SurfaceView)findViewById(R.id.previewsurface);
        previewSurfaceHolder = previewSurfaceView.getHolder();
        previewSurfaceHolder.addCallback(this);
        previewSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        ag.update(24.152214,120.675439);
        rl = new Rl(relativeLayout,FindQuestion.this);

        //取得陀螺儀控制
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //自定義加速度類別
        ac = new Acceleration(tv2,sm);
        //自定義方位類別
        senor = new Sen(tv,sm,ac,ag,rl);
        //自定義GPS類別
        //mlocation  = (LocationManager)getSystemService(LOCATION_SERVICE);
        //g = new G(mlocation,this,tv3);



       //RelativeLayout relativeLayout = (RelativeLayout) super.findViewById(R.id.rlId);
        //RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
        //Button bt = new Button(this);
        //relativeLayout.addView(bt, /*relativeParams150,50);
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

    /*public static void setLayout(View view,int x,int y)
    {
        MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, x+margin.width, y+margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }*/
}
