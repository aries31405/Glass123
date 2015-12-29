package com.example.glass123.glasslogin.Draw;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.QuestionInfo;
import com.example.glass123.glasslogin.Gps.G;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.Sensor.Sen;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by s1100b026 on 2015/11/24.
 */
public class DrawTest  extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    private ArrayList<AndroidUnit> Au; //AndroidUnit 類別型態的物件陣列
    private Canvas canvas = null;
    private Thread db_thread;
    boolean flag =true,first=true,IsNotCreating = true;
    private Resources res;
    private Bitmap bmp;

    //呼叫getHolder()方法來取得 SurfaceHolder,並指給 holder
    private SurfaceHolder holder;

    private LocationManager mlocation ;

    Context context;

    // DrawTest 建構子
    public DrawTest(Context context,AttributeSet attrs) {
        super(context,attrs);
        // TODO Auto-generated constructor stub

        this.context = context;

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

        final ProgressDialog dialog = ProgressDialog.show(context, "讀取中", "請等待數秒...", true);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true) {
                    if((G.latitude != 0.0 || G.longitude != 0.0) && Sen.postion !=0)
                    {;
                        for (AndroidUnit a: Au) {
                            a.start();
                        }
                        db_thread.start();
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
                Intent intent = new Intent(context, QuestionInfo.class);
                context.startActivity(intent);
            }

        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

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
                Thread.sleep(100);
                if(IsNotCreating)
                {
                    IsNotCreating = false;
                    if(first)
                    {
                       draw();
                    }
                    else if((Sen.nowpostion + 5 < Sen.postion) || (Sen.nowpostion - 5) > Sen.postion || G.latitude > (G.nowlatitude + 0.000008) || G.latitude < (G.nowlatitude-0.000008) || G.longitude > (G.nowlongitude + 0.000008) || G.longitude < (G.nowlongitude-0.000008))
                    {
                        draw();
                    }
                    chang = 1;
                }

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
    public void draw()
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
            Sen.nowpostion = Sen.postion;
            G.nowlatitude = G.latitude;
            G.nowlongitude =G.longitude;
        }

        if (canvas != null) {
            //解鎖畫布(canvas)並顯示到螢幕上
            holder.unlockCanvasAndPost(canvas);
        }
    }


}
