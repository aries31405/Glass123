package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.glass123.glasslogin.R;

public class Evaluation extends Activity implements View.OnTouchListener,View.OnClickListener{

    ViewFlipper viewFlipper;
    private float touchDownX;
    private float touchUpX;
    private float touchDownY;
    private float touchUpY;

    //star
    ImageButton star_imgbtn1;
    ImageButton star_imgbtn2;
    ImageButton star_imgbtn3;
    ImageButton star_imgbtn4;
    ImageButton star_imgbtn5;

    //flag
    boolean star_flag1 = false;
    boolean star_flag2 = false;
    boolean star_flag3 = false;
    boolean star_flag4 = false;
    boolean star_flag5 = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);

        int i = 0;

        for(i=0;i<5;i++){

            TextView tv1 = new TextView(this);

            switch (i){
                case 0:
                    tv1.setText("Challenge");
                    break;
                case 1:
                    tv1.setText("Good Question");
                    break;
                case 2:
                    tv1.setText("Boring");
                    break;
                case 3:
                    tv1.setText("Hard To Understand");
                    break;
                case 4:
                    tv1.setText("Not useful");
                    break;

            }

            tv1.setTextColor(this.getResources().getColor(R.color.white));
            tv1.setTextSize(30);
            LinearLayout lq1 = new LinearLayout(this);
            lq1.addView(tv1);
            viewFlipper.addView(lq1);

        }

        viewFlipper.setOnTouchListener(this);

        //star init
        star_imgbtn1 = (ImageButton)findViewById(R.id.star_imgbtn1);
        star_imgbtn2 = (ImageButton)findViewById(R.id.star_imgbtn2);
        star_imgbtn3 = (ImageButton)findViewById(R.id.star_imgbtn3);
        star_imgbtn4 = (ImageButton)findViewById(R.id.star_imgbtn4);
        star_imgbtn5 = (ImageButton)findViewById(R.id.star_imgbtn5);

    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            touchDownX = event.getX();
            touchDownY = event.getY();
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            touchUpX=event.getX();
            touchUpY=event.getY();
            if(touchUpX-touchDownX > 100){
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));

                viewFlipper.showPrevious();
            }
            else if(touchDownX-touchUpX > 100){
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_out));

                viewFlipper.showNext();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.star_imgbtn1:
//                if(star_flag1){
//                    star_imgbtn1.setBackgroundResource(R.drawable.star);
//
//                    star_imgbtn2.setBackgroundResource(R.mipmap.ic_launcher);
//                    star_imgbtn3.setBackgroundResource(R.mipmap.ic_launcher);
//                    star_imgbtn4.setBackgroundResource(R.mipmap.ic_launcher);
//                    star_imgbtn5.setBackgroundResource(R.mipmap.ic_launcher);
//
//                }
//                else if(!star_flag1){
//
//                }
                break;
            case R.id.star_imgbtn2:
                break;
            case R.id.star_imgbtn3:
                break;
            case R.id.star_imgbtn4:
                break;
            case R.id.star_imgbtn5:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_evaluation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
