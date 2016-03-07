package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.R;

import java.util.HashMap;
import java.util.Map;

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

    int nowstar=0;

    Button sendevaluation_btn;

    //作答紀錄用
    int QuestionNo=0;
    String MemberId=""; //待接值
    String AnswerContent="";
    int AnswerType;
    int AnswerTimer=0;
    int AnswerRevolution = 0;
    int AnswerStar;
    int CommendNo;

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

        star_imgbtn1.setOnClickListener(this);
        star_imgbtn2.setOnClickListener(this);
        star_imgbtn3.setOnClickListener(this);
        star_imgbtn4.setOnClickListener(this);
        star_imgbtn5.setOnClickListener(this);

        //送出按鈕
        sendevaluation_btn = (Button)findViewById(R.id.sendevaluation_btn);
        sendevaluation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowstar == 0)
                {
                    Toast.makeText(Evaluation.this,"請給星星",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),nowstar+"，第"+String.valueOf(viewFlipper.getDisplayedChild()+1),Toast.LENGTH_SHORT).show();
//                    uploadevaluation();
                }
            }
        });

        //接bundle的值
        Bundle bundle = this.getIntent().getExtras();
        QuestionNo = bundle.getInt("QuestionNo");
        MemberId = bundle.getString("MemberId");
        AnswerContent = bundle.getString("AnswerContent");
        AnswerType = bundle.getInt("AnswerType");
        AnswerTimer = bundle.getInt("AnswerTimer");
        AnswerRevolution = bundle.getInt("AnswerRevolution");



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
            if(touchUpY-touchDownY > 100){
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));

                viewFlipper.showPrevious();
            }
            else if(touchDownY-touchUpY > 100){
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
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.notstar);
                star_imgbtn3.setImageResource(R.drawable.notstar);
                star_imgbtn4.setImageResource(R.drawable.notstar);
                star_imgbtn5.setImageResource(R.drawable.notstar);
                nowstar = 1;

                break;
            case R.id.star_imgbtn2:
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.star);
                star_imgbtn3.setImageResource(R.drawable.notstar);
                star_imgbtn4.setImageResource(R.drawable.notstar);
                star_imgbtn5.setImageResource(R.drawable.notstar);
                nowstar = 2;

                break;
            case R.id.star_imgbtn3:
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.star);
                star_imgbtn3.setImageResource(R.drawable.star);
                star_imgbtn4.setImageResource(R.drawable.notstar);
                star_imgbtn5.setImageResource(R.drawable.notstar);
                nowstar = 3;

                break;
            case R.id.star_imgbtn4:
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.star);
                star_imgbtn3.setImageResource(R.drawable.star);
                star_imgbtn4.setImageResource(R.drawable.star);
                star_imgbtn5.setImageResource(R.drawable.notstar);
                nowstar = 4;

                break;
            case R.id.star_imgbtn5:
                star_imgbtn1.setImageResource(R.drawable.star);
                star_imgbtn2.setImageResource(R.drawable.star);
                star_imgbtn3.setImageResource(R.drawable.star);
                star_imgbtn4.setImageResource(R.drawable.star);
                star_imgbtn5.setImageResource(R.drawable.star);
                nowstar = 5;

                break;

        }
    }

//    private void uploadevaluation(){
//        AQuery aq = new AQuery(this);
//        String url = "http://163.17.135.76/glass/uploaduserevaluation.php";
//
//        Map<String,Object> params = new HashMap<String, Object>();
//
//        params.put("titleId",titleId);
//        params.put("UserId",UserId);
//        params.put("evaluationStar",nowstar);
//
//        aq.ajax(url,params,String.class,new AjaxCallback<String>(){
//            @Override
//            public void callback(String url, String object, AjaxStatus status) {
//                //成功
//                if(status.getCode() == 200)
//                {
//                    Toast.makeText(Evaluation.this, "上傳評價成功", Toast.LENGTH_SHORT).show();
//                }
//                //失敗
//                else
//                {
//                    Toast.makeText(Evaluation.this,String.valueOf(status.getCode()),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

}
