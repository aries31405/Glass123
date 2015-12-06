package com.example.glass123.glasslogin.Draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Toast;

import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.Angle;
import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.Decide;
import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.FindQuestion;
import com.example.glass123.glasslogin.Sensor.Sen;

/**
 * Created by s1100b026 on 2015/11/24.
 */
public class AndroidUnit implements Runnable{
    Decide decide;
    private double ag ;
    private boolean flag = true;
    private int  y,unit_Width, unit_Height,i;//顯示物件的座標,物件圖片的寬、高
    private Bitmap unit_bmp = null;       //代表該物件的圖片
    private  boolean notcreat = true;

    //矩形框變數，與觸碰事件比對座標，看是否點在此物件圖片範圍內
    Rect unit_rect = new Rect();

    public AndroidUnit(Bitmap unit_bmp,Double ag,int i){

        //指定圖片來源
        this.unit_bmp = unit_bmp;
        this.ag = ag;
        this.i = i;
        decide = new Decide(ag);
        //此物件參數的初始設定
        UnitInitial();

    }

    //==== 此物件參數的初始設定 ====
    private void UnitInitial() {
        // TODO Auto-generated method stub

        //取得物件圖片的高、寬
        unit_Height = unit_bmp.getHeight();
        unit_Width = unit_bmp.getWidth();


        new Thread(this).start();
        y = 330;

    }

    //==== 檢查是否被碰觸到 ====
    protected void IsTouch(int touch_x, int touch_y,Canvas canvas) {

        //將觸碰點的座標 touch_x 與 touch_y 傳入到
        //矩形框類別變數 unit_rect 的 contains(x, y) 方法中去判別
        //如果觸碰點的座標位於矩形框範圍內則contains(x, y)方法會傳回 true
        //否則傳回 false
        if (unit_rect.contains(touch_x, touch_y)) {
            Paint test = new Paint(Color.YELLOW);
            test.setColor(Color.YELLOW);
            canvas.drawText(String.valueOf(i),decide.x(), y, test);
        }
    }


    //==== 將圖 PO 到 canvas(畫布)上 ====
    protected void PostUnit(Canvas canvas) {
        //設定矩形框範圍，與觸碰事件比對是否觸碰到此物件範圍內
            unit_rect.set(decide.x(), y, decide.x() + unit_Width, y + unit_Height) ;
            //在 canvas 上繪出物件本體
            canvas.drawBitmap(this.unit_bmp,decide.x(), y, null);
    }

    public boolean cancareat()
    {
        return decide.cancareat();
    }

    @Override
    public void run() {
        while(flag){

            try {
                //暫停 0.5 秒(每隔 0.5 秒更新畫面一次)
                Thread.sleep(50);

                decide.decide();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } //while
    }
}
