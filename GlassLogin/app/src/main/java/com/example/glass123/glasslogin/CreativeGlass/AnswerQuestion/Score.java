package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.glass123.glasslogin.R;

import org.w3c.dom.Text;

public class Score extends Activity {
    //作答紀錄用
    int QuestionNo=0;
    String MemberId=""; //待接值
    String AnswerContent="";
    int AnswerType;
    int AnswerTimer=0;
    int AnswerRevolution = 0;

    //UI
    Button evaluate_btn;
    ImageView score_img;
    TextView score_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Bundle bundle = this.getIntent().getExtras();
        QuestionNo = bundle.getInt("QuestionNo");
        MemberId = bundle.getString("MemberId");
        AnswerContent = bundle.getString("AnswerContent");
        AnswerType = bundle.getInt("AnswerType");
        AnswerTimer = bundle.getInt("AnswerTimer");
        AnswerRevolution = bundle.getInt("AnswerRevolution");

        evaluate_btn = (Button)findViewById(R.id.evaluate_btn);
        evaluate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toevaluate();
            }
        });

        Log.e("niki", "QuestionNo " + QuestionNo);
        Log.e("niki", "MemberId " + MemberId);
        Log.e("niki","AnswerContent "+AnswerContent);
        Log.e("niki","AnswerType "+AnswerType);
        Log.e("niki","AnswerTimer "+AnswerTimer);
        Log.e("niki", "AnswerRevolution " + AnswerRevolution);

        score_img = (ImageView)findViewById(R.id.score_img);
        score_img.setImageResource(R.drawable.profile);

        score_txt = (TextView)findViewById(R.id.score_txt);

        setScoreUI();
    }

    private void setScoreUI(){
        if(AnswerType == 0)
        {
            score_txt.setText("您答錯了，不過沒關係，之後再來挑戰！");
        }
        else if(AnswerType == 1)
        {
            score_txt.setText("您答對了，還有很多的題目等著您去挑戰呢！");
        }
        else if(AnswerType == 2)
        {
            score_txt.setText("您可能一時想不到答案，不過沒關係，之後再來挑戰！");
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        //在此返回上一頁視同放棄作答
//        if(keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    private void toevaluate(){
        //到評價頁面
        Bundle bundle = new Bundle();
        bundle.putInt("QuestionNo", QuestionNo);
        bundle.putString("MemberId", MemberId);
        bundle.putString("AnswerContent", AnswerContent);
        bundle.putInt("AnswerType", AnswerType);
        bundle.putInt("AnswerTimer",AnswerTimer);
        bundle.putInt("AnswerRevolution", AnswerRevolution);
        Intent intent = new Intent(Score.this,Evaluation.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        int EXIT_CODE=1;
//        if(resultCode == EXIT_CODE)
//        {
//            Log.e("PETER","123456789");
//            Score.this.finish();
//        }
//    }
}
