package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.R;

import java.util.HashMap;
import java.util.Map;

public class Answer extends Activity {

    Button sendans_btn;

    EditText answer_edittext;
    String answer="";
    String UserAnswer="";
    String UserId="20151211151346511431";
    int titleId=0;
    String right_or_wrong="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        //接bundle
        Bundle bundle = this.getIntent().getExtras();
        answer = bundle.getString("answer");
        titleId = bundle.getInt("titleId");

        sendans_btn = (Button)findViewById(R.id.sendans_btn);

        sendans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendans();
            }
        });

        answer_edittext = (EditText)findViewById(R.id.answer_edittext);

    }


    private void sendans(){

        //使用者輸入的答案
        UserAnswer=answer_edittext.getText().toString();

        //判斷答案是否正確
        if(answer.equals(UserAnswer))
        {
            right_or_wrong = "1";
            // 答對UI
            Toast.makeText(this,"答對了",Toast.LENGTH_SHORT).show();
        }
        else
        {
            right_or_wrong = "0";
            //答錯UI
            Toast.makeText(this,"答錯了",Toast.LENGTH_SHORT).show();
        }

        Log.e("niki",UserAnswer);
        Log.e("niki", UserId);
        Log.e("niki", String.valueOf(titleId));
        Log.e("niki", right_or_wrong);

        //上傳使用者解題紀錄
        uploaduseranswer();

        //到評價頁面
        Bundle bundle = new Bundle();
        bundle.putInt("titleId",titleId);
        bundle.putString("UserId",UserId);
        Intent intent = new Intent(Answer.this,Evaluation.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void uploaduseranswer(){
        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/glass/uploaduseranswer.php";

        Map<String,Object> params = new HashMap<String, Object>();

        params.put("titleId",titleId);
        params.put("UserId",UserId);
        params.put("UserAnswer",UserAnswer);
        params.put("right_or_wrong",right_or_wrong);

        aq.ajax(url,params,String.class,new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                //成功
                if(status.getCode() == 200)
                {
                    Toast.makeText(Answer.this,"上傳成功",Toast.LENGTH_SHORT).show();
                }
                //失敗
                else
                {
                    Toast.makeText(Answer.this,String.valueOf(status.getCode()),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer, menu);
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
