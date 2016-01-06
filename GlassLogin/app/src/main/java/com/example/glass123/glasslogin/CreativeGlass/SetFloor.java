package com.example.glass123.glasslogin.CreativeGlass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateQuestionAnswer;
import com.example.glass123.glasslogin.R;

public class SetFloor extends Activity implements View.OnTouchListener{

    ViewFlipper floor_viewflipper;
    private float touchDownX;
    private float touchUpX;
    private float touchDownY;
    private float touchUpY;

    Button setfloor_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_floor);

        floor_viewflipper = (ViewFlipper)findViewById(R.id.floor_viewflipper);

        int i = 0;

        for(i=0;i<11;i++){

            TextView tv1 = new TextView(this);

            if(i==0)
            {
                tv1.setText("戶外");
            }
            else
            {
                tv1.setText(String.valueOf(i)+"樓");
            }

            tv1.setTextColor(this.getResources().getColor(R.color.white));
            tv1.setTextSize(50);
            LinearLayout lq1 = new LinearLayout(this);
            lq1.addView(tv1);
            floor_viewflipper.addView(lq1);

        }

        floor_viewflipper.setOnTouchListener(this);

        setfloor_btn = (Button)findViewById(R.id.setfloor_btn);

        setfloor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetFloor.this, CreateQuestionAnswer.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            touchDownX = event.getX();
            touchDownY = event.getY();
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            touchUpX=event.getX();
            touchUpY=event.getY();
            if(touchUpY-touchDownY > 100){
                floor_viewflipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
                floor_viewflipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));

                floor_viewflipper.showPrevious();
            }
            else if(touchDownY-touchUpY > 100){
                floor_viewflipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_in));
                floor_viewflipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_out));

                floor_viewflipper.showNext();
            }
            return true;
        }
        return false;
    }

    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_floor, menu);
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
