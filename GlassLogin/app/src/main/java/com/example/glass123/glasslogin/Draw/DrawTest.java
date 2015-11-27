package com.example.glass123.glasslogin.Draw;

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
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.Angle;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.Sensor.Sen;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by s1100b026 on 2015/11/24.
 */
public class DrawTest  extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private ArrayList<AndroidUnit> Au; //AndroidUnit 類別型態的物件陣列
    private Canvas canvas = null;
    private Thread db_thread;
    boolean flag =true,first=true,IsNotCreating = true;
    private Resources res;
    private Bitmap bmp;

    //呼叫getHolder()方法來取得 SurfaceHolder,並指給 holder
    SurfaceHolder holder;

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

    }

    //==== 初始設定 ====
    private void InitialSet() {
        //建立 AndroidUnit 物件陣列實體
        Au = new ArrayList<AndroidUnit>();
        Au.clear();  //先清除 Au 物件陣列

        //建立 AndroidUnit 物件 10 隻
        for(int i=0; i< Angle.angle.length; i++) {
            //產生 AndroidUnit 實體 au
            AndroidUnit au = new AndroidUnit(bmp,Angle.angle[i]);
            //陸續將 au 放入 Au 物件陣列中
            Au.add(au);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        db_thread.start();
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
                        draw();
                    }
                    else if(Sen.nowpositon + 5 < Sen.positon || Sen.nowpositon - 5 > Sen.positon)
                    {
                        draw();
                    }
                    chang =1;
                }

                /*//從 Au 物件陣列中移除已經停止活動的物件
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
            Sen.nowpositon = Sen.positon;
        }

        if (canvas != null) {
            //解鎖畫布(canvas)並顯示到螢幕上
            holder.unlockCanvasAndPost(canvas);
        }
    }


}
