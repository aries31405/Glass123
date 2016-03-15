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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.ChooseDevice;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.GetServerMessage;
import com.example.glass123.glasslogin.Draw.AndroidUnit;
import com.example.glass123.glasslogin.Draw.DrawTest;
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
    int percentage = 0;

    TextView titleId_txt;
    TextView author_txt;
    TextView floor_txt;

    String answer;
    String hint1;
    String hint2;
    String hint3;
    String memberId;

    ImageView starinfo1_img;
    ImageView starinfo2_img;
    ImageView starinfo3_img;
    ImageView starinfo4_img;
    ImageView starinfo5_img;

    ProgressBar rate;

    int star=0;
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
        memberId = bundle.getString("memberId");

        getquestioninfo();


        titleId_txt = (TextView)findViewById(R.id.titleId_txt);
        author_txt = (TextView)findViewById(R.id.author_txt);
        floor_txt = (TextView)findViewById(R.id.floor_txt);
        rate = (ProgressBar)findViewById(R.id.rate);

        //starinfo_img init
        starinfo1_img = (ImageView)findViewById(R.id.starinfo1_img);
        starinfo2_img = (ImageView)findViewById(R.id.starinfo2_img);
        starinfo3_img = (ImageView)findViewById(R.id.starinfo3_img);
        starinfo4_img = (ImageView)findViewById(R.id.starinfo4_img);
        starinfo5_img = (ImageView)findViewById(R.id.starinfo5_img);

    }

    private void getquestioninfo(){

        //撈題目
        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/new_glass/getquestioninfo.php";

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
                        hint1 = json.getString("hint1");
                        hint2 = json.getString("hint2");
                        hint3 = json.getString("hint3");
                        titleId_txt.setText("NO."+String.valueOf(titleId));
                        floor_txt.setText(json.getInt("floor")+"樓");
                        answer = json.getString("answer");
                        author_txt.setText(json.getString("author"));
                        star = json.getInt("star");
                        percentage=json.getInt("percentage");
                        Log.e("PETER","star "+star);
                        Log.e("PETER","percentage "+percentage);
                        rate.setProgress(percentage);

                        setstar();
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
        DrawTest.flag = false;
        AndroidUnit.flag = false;
        FindQuestion.INSTANCE.finish();
        Bundle bundle = new Bundle();
        bundle.putInt("titleId", titleId);
        bundle.putString("answer", answer);
        bundle.putString("hint1",hint1);
        bundle.putString("hint2", hint2);
        bundle.putString("hint3", hint3);
        bundle.putString("memberId",memberId);
        Intent intent = new Intent(QuestionInfo.this,AnswerQuestion.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        this.finish();
    }

    private void setstar(){
        if(star == 0)
        {

        }
        else if(star == 1)
        {
            starinfo1_img.setVisibility(View.VISIBLE);
        }
        else if(star == 2)
        {
            starinfo1_img.setVisibility(View.VISIBLE);
            starinfo2_img.setVisibility(View.VISIBLE);
        }
        else if(star == 3)
        {
            starinfo1_img.setVisibility(View.VISIBLE);
            starinfo2_img.setVisibility(View.VISIBLE);
            starinfo3_img.setVisibility(View.VISIBLE);
        }
        else if(star == 4)
        {
            starinfo1_img.setVisibility(View.VISIBLE);
            starinfo2_img.setVisibility(View.VISIBLE);
            starinfo3_img.setVisibility(View.VISIBLE);
            starinfo4_img.setVisibility(View.VISIBLE);

        }else if(star == 5)
        {
            starinfo1_img.setVisibility(View.VISIBLE);
            starinfo2_img.setVisibility(View.VISIBLE);
            starinfo3_img.setVisibility(View.VISIBLE);
            starinfo4_img.setVisibility(View.VISIBLE);
            starinfo5_img.setVisibility(View.VISIBLE);
        }
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
