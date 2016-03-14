package com.example.glass123.glasslogin.Draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.widget.CheckBox;

import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.Decide;
import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.FindQuestion;
import com.example.glass123.glasslogin.Sensor.Sen;

/**
 * Created by s1100b026 on 2015/11/24.
 */
public class AndroidUnit implements Runnable{
    Decide decide;
    public static boolean flag =true;
    private double ag , latitude, lontitud;
    private boolean ch_draw;
    private int  y,unit_Width, unit_Height,titleId,ch;//顯示物件的座標,物件圖片的寬、高
    private Bitmap unit_bmp = null;       //代表該物件的圖片
    private  boolean notcreat = true;
    //矩形框變數，與觸碰事件比對座標，看是否點在此物件圖片範圍內
    Rect unit_rect = new Rect();

    public AndroidUnit(Bitmap unit_bmp,double latitude,double lontitude,int titleId,int ch){

        //指定圖片來源
        this.unit_bmp = unit_bmp;
        this.latitude = latitude;
        this.lontitud = lontitude;
        this.titleId = titleId;
        this.ch = ch;

        //此物件參數的初始設定
        UnitInitial();

    }

    //==== 此物件參數的初始設定 ====
    private void UnitInitial() {
        // TODO Auto-generated method stub

        //取得物件圖片的高、寬
        unit_Height = unit_bmp.getHeight();
        unit_Width = unit_bmp.getWidth();

        y = (int) (Math.random() *500+100);

    }

    //==== 檢查是否被碰觸到 ====
    protected void IsTouch(int touch_x, int touch_y) {

        //將觸碰點的座標 touch_x 與 touch_y 傳入到
        //矩形框類別變數 unit_rect 的 contains(x, y) 方法中去判別
        //如果觸碰點的座標位於矩形框範圍內則contains(x, y)方法會傳回 true
        //否則傳回 false
        if (unit_rect.contains(touch_x, touch_y)) {
            DrawTest.titleId = this.titleId;
        }
    }


    //==== 將圖 PO 到 canvas(畫布)上 ====
    protected void PostUnit(Canvas canvas) {
        //設定矩形框範圍，與觸碰事件比對是否觸碰到此物件範圍內
        unit_rect.set(decide.x(), y, decide.x() + unit_Width, y + unit_Height) ;
        //在 canvas 上繪出物件本體
        canvas.drawBitmap(this.unit_bmp, decide.x(), y, null);
    }

    public boolean cancareat()
    {
        return decide.cancareat();
    }

    public void start()
    {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(flag){

            try {
                //暫停 0.5 秒(每隔 0.5 秒更新畫面一次)
                Thread.sleep(10);

                switch (ch)
                {
                    case 1:
                        ch_draw = FindQuestion.ch_undo;
                        break;
                    case 2:
                        ch_draw = FindQuestion.ch_correct;
                        break;
                    case 3:
                        ch_draw = FindQuestion.ch_wrong;
                        break;
                    case 4:
                        ch_draw = FindQuestion.ch_custom;
                        break;
                }

               if(ch_draw)
               {
                   update(FindQuestion.latitude, FindQuestion.longitude, Sen.postion);
//                   Log.e("GGG","isChecked");
               }
                else if(decide.cancareat())
               {
                   decide.cantcreate();
                   Log.e("GGG", "no");
               }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } //while
    }

    public void update(double latitud,double longitude,float positon)
    {

            if(this.latitude > latitud && this.lontitud >longitude)
            {
                ag = gps2d(latitud,longitude,this.latitude,this.lontitud);
            }
            else if(this.latitude < latitud && this.lontitud >longitude)
            {
                ag = 180-gps2d(latitud,longitude,this.latitude,this.lontitud);
            }
            else if(this.latitude > latitud && this.lontitud <longitude)
            {
                ag = 360+gps2d(latitud,longitude,this.latitude,this.lontitud);
            }
            else if(this.latitude < latitud && this.lontitud <longitude)
            {
                ag = 180-gps2d(latitud,longitude,this.latitude,this.lontitud);
            }

            decide = new Decide(ag,positon);
            decide.decide();
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



}
