package com.example.glass123.glasslogin.Draw;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.Angle;
import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.FindQuestion;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.Sensor.Sen;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by s1100b026 on 2015/11/24.
 */
public class DrawTest  extends SurfaceView implements SurfaceHolder.Callback, Runnable,LocationListener{
    private ArrayList<AndroidUnit> Au; //AndroidUnit 類別型態的物件陣列
    private Canvas canvas = null;
    private Thread db_thread;
    boolean flag =true,first=true,IsNotCreating = false;
    private Resources res;
    private Bitmap bmp;

    //呼叫getHolder()方法來取得 SurfaceHolder,並指給 holder
    private SurfaceHolder holder;

    private LocationManager mlocation ;
    private double latitude=0.0,longitude=0.0;

    private SensorManager sm;
    private Sensor aSensor;
    private Sensor mSensor;
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    private float positon,nowpositon = 0;

    // DrawTest 建構子
    public DrawTest(Context context,AttributeSet attrs) {
        super(context,attrs);
        // TODO Auto-generated constructor stub

        //指定圖片來源
        res = getResources();
        bmp = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);

        //初始設定
        InitialSet();

        //把這個 class 本身(extends SurfaceView)
        //透過 holder 的 Callback()方法連結起來
        //下面這行也可寫成 getHolder().addCallback(this);
        holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSPARENT); // 设置为透明
        setZOrderOnTop(true);// 设置为顶端

        //建立執行緒
        db_thread = new Thread(this);

        //取得定位
        mlocation  = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        mlocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 2, this);
        mlocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,2, this);

        //取得陀螺儀控制
        sm = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener(myListener, mSensor, SensorManager.SENSOR_DELAY_GAME);

        final ProgressDialog dialog = ProgressDialog.show(context, "讀取中", "請等待數秒...", true);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true) {
                    if((latitude != 0.0 || longitude != 0.0) && nowpositon !=0)
                    {
                        draw(nowpositon);
                        dialog.dismiss();
                        break;
                    }
                }
            }
        }).start();

    }

    //==== 初始設定 ====
    private void InitialSet() {
        //建立 AndroidUnit 物件陣列實體
        Au = new ArrayList<AndroidUnit>();
        Au.clear();  //先清除 Au 物件陣列

        for (Data a: FindQuestion.Au) {
            //產生 AndroidUnit 實體 au
            AndroidUnit au = new AndroidUnit(bmp,a.getLatitude(),a.getLontitude(),a.gettitleId());
            //陸續將 au 放入 Au 物件陣列中
            Au.add(au);
        }
    }

    //==== 加入觸碰事件方法 ====
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int)event.getX();
            int y = (int)event.getY();

            //取得並鎖住畫布(canvas)
            canvas = holder.lockCanvas();

            //巡覽 Au 物件陣列一遍，逐一比對是否碰觸到物件圖片
            for (AndroidUnit a: Au) {
                a.IsTouch(x, y,canvas);
            }

            if (canvas != null) {
                //解鎖畫布(canvas)並顯示到螢幕上
                holder.unlockCanvasAndPost(canvas);
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //db_thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(flag){
            int chang = 0;
            //將物件顯示到螢幕上
            try {
                //暫停 0.05 秒(每隔 0.05 秒更新畫面一次)
                //Thread.sleep(100);
                if(IsNotCreating)
                {
                    IsNotCreating = false;
                    if(first)
                    {
                       // draw();
                    }
                    else if((Sen.nowpositon + 5 < Sen.positon || Sen.nowpositon - 5 > Sen.positon))
                    {
                        //draw();
                    }
                    chang = 1;
                }

                /*從 Au 物件陣列中移除已經停止活動的物件
                for (AndroidUnit b: Au) {
                    if (!b.IsAlive()) Au.remove(b);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(chang == 1)
                {
                    IsNotCreating = true;
                }
            }
        } //while
    }

    //繪製畫面
   /* public void draw()
    {

        //取得並鎖住畫布(canvas)
        canvas = holder.lockCanvas();
        //清除畫面
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);

        ///巡覽 Au 物件陣列中的所有物件
        for (AndroidUnit a: Au) {
            if(a.cancareat())
            {
                //呼叫 AndroidUnit 物件的 PostUnit() 方法
                //將物件圖片繪至 canvas 上
                a.PostUnit(canvas);
            }
        }

        if(first)
        {
            first = false;
        }
        else
        {
            Sen.nowpositon = Sen.positon;
        }

        if (canvas != null) {
            //解鎖畫布(canvas)並顯示到螢幕上
            holder.unlockCanvasAndPost(canvas);
        }
    }*/
    //繪製畫面
    public void draw(float  positon)
    {
        //取得並鎖住畫布(canvas)
        canvas = holder.lockCanvas();
        //清除畫面
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);

        ///巡覽 Au 物件陣列中的所有物件
        for (AndroidUnit a: Au) {
            a.update(latitude,longitude,positon);
            if(a.cancareat()) {
                //呼叫 AndroidUnit 物件的 PostUnit() 方法
                //將物件圖片繪至 canvas 上
                a.PostUnit(canvas);
            }
        }

        if (canvas != null) {
            //解鎖畫布(canvas)並顯示到螢幕上
            holder.unlockCanvasAndPost(canvas);
        }
    }

    //--------------------------定位-----------------------------------
    @Override
    public void onLocationChanged(Location location) {

        //latitude = location.getLatitude();
        //longitude = location.getLongitude();
        latitude = 24.152214;
        longitude = 120.675439;

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


    //-------------------------------------------陀螺儀控制-----------------------------------------
    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {


            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values;

            calculateOrientation();
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private  void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        values[0] = (float) Math.toDegrees(values[0]);
        //values[1] = (float) Math.toDegrees(values[1]);
        //values[2] = (float) Math.toDegrees(values[2]);
        //Log.i(TAG, values[0]+"");

        if(values[0] != 0.0)
        {
            positon = transform(values[0]);
            if(nowpositon == 0)
            {
                nowpositon = positon;
            }
        }

    }

    public float transform(float positon)
    {
        if(positon >= -179.9 && positon <-91 )
        {
            positon = 270 + (180-(positon*-1));
        }
        else
        {
            positon = positon + 90;
        }
        return positon;
    }

}
