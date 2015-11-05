package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.Sensor.Acceleration;
import com.example.glass123.glasslogin.Sensor.Sen;

import java.io.IOException;

public class FindQuestion extends Activity implements SurfaceHolder.Callback{

    Sen senor;
    Acceleration ac;
    TextView tv,tv2;

    Camera myCamera;
    SurfaceView previewSurfaceView;
    SurfaceHolder previewSurfaceHolder;
    boolean previewing = false;


    private SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_question);

        tv = (TextView)findViewById(R.id.textView4);
        tv2 = (TextView)findViewById(R.id.textView6);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        previewSurfaceView = (SurfaceView)findViewById(R.id.previewsurface);
        previewSurfaceHolder = previewSurfaceView.getHolder();
        previewSurfaceHolder.addCallback(this);
        previewSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        senor = new Sen(tv,sm);
        ac = new Acceleration(tv2,sm);

        RelativeLayout relativeLayout = (RelativeLayout) super.findViewById(R.id.rlId);
        //RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
        Button bt = new Button(this);
        relativeLayout.addView(bt, /*relativeParams*/150,50);
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
