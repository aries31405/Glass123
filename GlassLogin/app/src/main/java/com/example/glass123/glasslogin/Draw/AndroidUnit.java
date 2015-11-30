package com.example.glass123.glasslogin.Draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

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
    private int x, y,unit_Width, unit_Height;//顯示物件的座標,物件圖片的寬、高
    private Bitmap unit_bmp = null;       //代表該物件的圖片
    private  boolean notcreat = true;

    //矩形框變數，與觸碰事件比對座標，看是否點在此物件圖片範圍內
    Rect unit_rect = new Rect();

    public AndroidUnit(Bitmap unit_bmp,Double ag){

        //指定圖片來源
        this.unit_bmp = unit_bmp;
        this.ag = ag;
        decide = new Decide(ag);
        //此物件參數的初始設定
        UnitInitial();

    }

    //==== 此物件參數的初始設定 ====
    private void UnitInitial() {
        // TODO Auto-generated method stub

        /*//取得物件圖片的高、寬
        unit_Height = unit_bmp.getHeight();
        unit_Width = unit_bmp.getWidth();

<<<<<<< HEAD

       /* if((Sen.nowpositon+30) > 359)
        {
            creat( 30-(360-Sen.nowpositon),1);
        }
        else if((Sen.nowpositon-30) < 0)
        {
            creat( (Sen.nowpositon-30)*(-1),2);
        }
        else
        {
            creat(0,0);
        }*/
        x = 600;
        y = 330;
=======
>>>>>>> e6ed9ecd4e263be05f75daeea6fee4d39f99cd8c
        //以亂數決定此物件的初始座標
        //x = (int)(Math.random() * (FindQuestion.monitor_Width - unit_Width));
        //y = (int)((Math.random() * (FindQuestion.monitor_Height - unit_Height - 5)) + 5);*/

        new Thread(this).start();
        y = 330;

    }
    //==== 將圖 PO 到 canvas(畫布)上 ====
    protected void PostUnit(Canvas canvas) {
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
