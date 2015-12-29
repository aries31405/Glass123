package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.glass123.glasslogin.R;

public class QuestionInfo extends Activity {

    Button startans_btn;
    Button close_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //沒有標題
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_question_info);

        //init
        startans_btn = (Button)findViewById(R.id.startans_btn);
        close_btn = (Button)findViewById(R.id.close_btn);

        //開始作答監聽器
        startans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startans();
            }
        });

        //關閉按鈕監聽器
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    //跳到提示的畫面
    private void startans(){
        Intent intent = new Intent(QuestionInfo.this,Hints.class);
        startActivity(intent);
        this.finish();
    }

    //關閉題目資訊小視窗
    private void close(){
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question_info, menu);
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
