package com.example.gameking_var2.remoteproject.Rank;

import com.example.gameking_var2.remoteproject.Http.GetServerMessage;
import com.example.gameking_var2.remoteproject.MainLine.MainLine;
import com.example.gameking_var2.remoteproject.R;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import static android.widget.Toast.LENGTH_LONG;

/*
給予題目評價
 */

public class Rank extends Activity implements GestureDetector.BaseListener
{

    String msg = "";

    //星等的ViewFliper
    ViewFlipper vf_rank;

    //定義手勢偵測
    private GestureDetector GestureDetector;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        setContentView(R.layout.rank_stars);

        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);

        vf_rank = (ViewFlipper) findViewById(R.id.vf_rank);

        //定義六個View放星等圖片
        LinearLayout lq0, lq1, lq2, lq3, lq4, lq5;

        //六個星等圖
        ImageView    im0, im1, im2, im3, im4, im5;

        //放入星等圖  從零到五
        lq0 = new LinearLayout(this);
        im0 = new ImageView(this);
        im0.setImageResource(R.drawable.no_star);
        lq0.addView(im0);
        vf_rank.addView(lq0);

        lq1 = new LinearLayout(this);
        im1 = new ImageView(this);
        im1.setImageResource(R.drawable.one_star);
        lq1.addView(im1);
        vf_rank.addView(lq1);

        lq2 = new LinearLayout(this);
        im2 = new ImageView(this);
        im2.setImageResource(R.drawable.two_star);
        lq2.addView(im2);
        vf_rank.addView(lq2);

        lq3 = new LinearLayout(this);
        im3 = new ImageView(this);
        im3.setImageResource(R.drawable.three_star);
        lq3.addView(im3);
        vf_rank.addView(lq3);

        lq4 = new LinearLayout(this);
        im4 = new ImageView(this);
        im4.setImageResource(R.drawable.four_star);
        lq4.addView(im4);
        vf_rank.addView(lq4);

        lq5 = new LinearLayout(this);
        im5 = new ImageView(this);
        im5.setImageResource(R.drawable.five_star);
        lq5.addView(im5);
        vf_rank.addView(lq5);

    }

    //--------------------手勢動作------------------//

    //偵測手勢動作，回傳事件
    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        return GestureDetector.onMotionEvent(event);
    }

    @Override
    public boolean onGesture(Gesture gesture)
    {
        //會傳入手勢  gesture.name()會取得手勢名稱 或是另一種 gesture ＝ Gesture.SWIPE_UP
        switch (gesture.name())
        {
            case "SWIPE_LEFT":
                vf_rank.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_left_in));
                vf_rank.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_left_out));
                vf_rank.showPrevious();
                break;
            case "SWIPE_RIGHT":
                vf_rank.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_right_in));
                vf_rank.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_right_out));
                vf_rank.showNext();
                break;
            case "TAP":
                startActivity(new Intent( Rank.this, MainLine.class));
                Toast.makeText(this, String.valueOf(vf_rank.getDisplayedChild()) + "星", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case "LONG_PRESS":
                /*new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        GetServerMessage message = new GetServerMessage();
                        msg = message.all(allurl,alldata);
                    }

                }).start();*/
                break;
        }
        return true;
    }

    //------------------選單------------------//

    //建立選單
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        /*
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rank_menu, menu);
        */
        return true;
    }

    //點擊選單
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /*
        switch (item.getItemId())
        {
            default:
                return true;
        }*/
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

}
