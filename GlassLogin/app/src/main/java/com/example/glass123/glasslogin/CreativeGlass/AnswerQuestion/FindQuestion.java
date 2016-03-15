package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.GetServerMessage;
import com.example.glass123.glasslogin.Draw.AndroidUnit;
import com.example.glass123.glasslogin.Draw.Data;
import com.example.glass123.glasslogin.Draw.DrawTest;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.Sensor.Acceleration;
import com.example.glass123.glasslogin.Sensor.Sen;
import com.example.glass123.glasslogin.Split.Sp;

import java.io.IOException;
import java.util.ArrayList;

public class FindQuestion extends Activity  implements SurfaceHolder.Callback, LocationListener {

    public static FindQuestion INSTANCE;

    public static double latitude=0.0,longitude=0.0,nowlatitude=0.0,nowlongitude=0.0;
    public static String choose,memberId;

    private  int radius = 10,floor;
    private String allmsg;

    private Button bt;
    private DrawTest drawTest = null;
    private SurfaceView svCamera = null;
    protected SurfaceHolder mSurfaceHolder;
    public static Boolean ch_undo = true,ch_correct = true,ch_wrong = true,ch_custom = true;

    boolean previewing = false;
    Camera myCamera;

    public static ArrayList<Data> Au;

    SensorManager sm;
    Sen senor;
    Acceleration ac;
    Sp sp;

    Handler handler = new Handler();

    LocationManager mlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrawTest.flag = true;
        AndroidUnit.flag = true;
        INSTANCE = this;

        Bundle bundle = this.getIntent().getExtras();
        choose = bundle.getString("choose");
        memberId = bundle.getString("memberId");
        floor = bundle.getInt("floor");
        if(choose.equals("Network")){
            radius = bundle.getInt("radius");
            latitude = bundle.getDouble("latitude");
            longitude = bundle.getDouble("longitude");
            //latitude = 24.149592;
            //longitude = 120.683449;
        }
        else {
            mlocation  = (LocationManager)getSystemService(LOCATION_SERVICE);
            mlocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,FindQuestion.this);
        }


        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true) {
                    if((latitude != 0.0 || longitude != 0.0))
                    {
                        GetServerMessage message = new GetServerMessage();
                        allmsg = message.all("http://163.17.135.76/new_glass/question_search.php","UserId="+memberId+"&lat="+latitude+"&lon="+longitude+"&radius="+(radius*0.00000900900901)+"&floor="+String.valueOf(floor));
                        handler.post(split);
                        break;
                    }
                }
            }

        }).start();

        //取得陀螺儀控制
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //自定義加速度類別
        //ac = new Acceleration(sm);
        //自定義方位類別
        senor = new Sen(sm);

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
            if(allmsg.equals("No question"))
            {
                Toast.makeText(FindQuestion.this,"附近無任何題目",Toast.LENGTH_LONG).show();
            }
            else
            {
                sp = new Sp(allmsg);
                handler.post(uiupdate);
            }

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
        CheckBox ch;
        ch = (CheckBox)findViewById(v.getId());

        switch (v.getId())
        {
            case R.id.draw_undo:
                if(ch.isChecked()) {
                    ch_undo = true;
                }else
                {
                    ch_undo = false;
                }
                break;
            case R.id.draw_correct:
                if(ch.isChecked()) {
                    ch_correct = true;
                }else
                {
                    ch_correct = false;
                }
                break;
            case R.id.draw_wrong:
                if(ch.isChecked()) {
                    ch_wrong = true;
                }else
                {
                    ch_wrong = false;
                }
                break;
            case R.id.draw_custom:
                if(ch.isChecked()) {
                   ch_custom = true;
                }else
                {
                    ch_custom = false;
                }
                break;
        }
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

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //latitude = 24.149592;
        //longitude = 120.683449;

        if(nowlatitude == 0.0)
        {
            nowlatitude = latitude;
            nowlongitude = longitude;
        }

        //Toast.makeText(FindQuestion.this,"latitude="+latitude+"__longitude="+longitude,Toast.LENGTH_LONG).show();

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
