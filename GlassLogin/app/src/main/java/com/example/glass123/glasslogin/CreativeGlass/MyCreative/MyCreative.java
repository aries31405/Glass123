package com.example.glass123.glasslogin.CreativeGlass.MyCreative;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyCreative extends Activity {

    String memberId="";
    int AllQuestion = 0;
    int CorrectQuestion = 0;
    int WrongQuestion = 0;
    int Percentage = 0;
    String imageUrl = "";
    String MemberName="";

    TextView all_txt;
    TextView percentage_txt;
    TextView correct_txt;
    TextView wrong_txt;
    TextView user;

    ImageView mycreative_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creative);

        Bundle bundle = this.getIntent().getExtras();
        memberId = bundle.getString("memberId");

        //init
        all_txt = (TextView)findViewById(R.id.all_txt);
        percentage_txt = (TextView)findViewById(R.id.percentage_txt);
        correct_txt = (TextView)findViewById(R.id.correct_txt);
        wrong_txt = (TextView)findViewById(R.id.wrong_txt);
        user = (TextView)findViewById(R.id.user);
        mycreative_img=(ImageView)findViewById(R.id.mycreative_img);

        getProfile();
    }

    private void getProfile(){
        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/new_glass/getprofile.php";

        Map<String,Object> params = new HashMap<>();

        params.put("userId", memberId);
        aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                //成功
                if (status.getCode() == 200) {
                    try {
                        AllQuestion = json.getInt("all");
                        Percentage = json.getInt("percentage");
                        CorrectQuestion = json.getInt("true");
                        WrongQuestion = json.getInt("false");
                        imageUrl = json.getString("picture");
                        MemberName= json.getString("name");
//                        Log.e("PETER",""+AllQuestion);
//                        Log.e("PETER",""+Percentage);
//                        Log.e("PETER",""+CorrectQuestion);
//                        Log.e("PETER",""+WrongQuestion);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("PETER", "" + "報了");

                    }
                    setProfile();
                    setProfileImage(imageUrl);
                }
                //失敗
                else {
                    Toast.makeText(getApplicationContext(), String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setProfile(){
        all_txt.setText("總題數："+AllQuestion);
        percentage_txt.setText("答對率："+Percentage+"%");
        correct_txt.setText("答對題數：" + CorrectQuestion);
        wrong_txt.setText("答錯題數：" + WrongQuestion);
        user.setText(MemberName);
    }

    // 設定 Google帳戶大頭貼
    private void setProfileImage(String imageurl){

        AQuery aq = new AQuery(this);

        // 相片很大，不用記體體快取
        boolean memCache = false;
        boolean fileCache = true;

        aq.id(R.id.mycreative_img).image(imageurl, memCache, fileCache);
    }


}
