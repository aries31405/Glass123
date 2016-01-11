package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.ChooseDevice;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.GetServerMessage;
import com.example.glass123.glasslogin.R;
import com.example.glass123.glasslogin.SetProfile;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class QuestionInfo extends Activity {

    Button startans_btn;
    Button close_btn;
    int titleId = 0;

    TextView titleId_txt;
    TextView author_txt;
    TextView floor_txt;

    String answer;
    String hint1;
    String hint2;
    String hint3;
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

        //接titleId
        Bundle bundle = this.getIntent().getExtras();
        titleId = bundle.getInt("titleId");

        getquestioninfo();

        titleId_txt = (TextView)findViewById(R.id.titleId_txt);
        author_txt = (TextView)findViewById(R.id.author_txt);
        floor_txt = (TextView)findViewById(R.id.floor_txt);

    }

    private void getquestioninfo(){

        //撈題目
        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/glass/getquestioninfo.php";

        Map<String,Object> params = new HashMap<String, Object>();

        //測試用
        params.put("titleId", titleId);

        aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                //連線成功
                if (status.getCode() == 200) {
                    try
                    {
                        //set info
                        titleId_txt.setText("NO."+String.valueOf(titleId));
                        floor_txt.setText(json.getString("floor")+"樓");
//                        author_txt.setText(json.getString("author"));
                        answer = json.getString("answer");
                        hint1 = json.getString("hint1");
                        hint2 = json.getString("hint2");
                        hint3 = json.getString("hint3");
                    }
                    catch (Exception e)
                    {

                    }
                }
                //失敗傳回HTTP狀態碼
                else {
                    Toast.makeText(getApplicationContext(), String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //跳到提示的畫面
    private void startans(){
        Bundle bundle = new Bundle();
        bundle.putString("answer",answer);
        bundle.putString("hint1",hint1);
        bundle.putString("hint2",hint2);
        bundle.putString("hint3",hint3);
        Intent intent = new Intent(QuestionInfo.this,Hints.class);
        intent.putExtras(bundle);
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
