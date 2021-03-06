package com.example.glass123.glasslogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.Bluetooth.ConnectToGlass;
import com.example.glass123.glasslogin.Bluetooth.Profile;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.GetServerMessage;
import com.example.glass123.glasslogin.CreativeGlass.CreativeGlassStart;
import com.example.glass123.glasslogin.Mplayer.Player;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends Activity implements View.OnClickListener,TextToSpeech.OnInitListener{

    Handler handler = new Handler();

    private Profile mProfile;

    private CircleImageView mPhoto;

    private TextView MyProfileName;
    private TextView MyProfileEmail;

    private Button ChangeUserBtn;
    private Button NextBtn;

    private static final int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech tts;

    public String msg;

    Player player;
    String device="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // 大頭貼findViewById
        mPhoto = (CircleImageView)findViewById(R.id.myprofile_image);

        // 取得MainActivity傳過來的使用者Google帳戶資料
        Bundle bundle = this.getIntent().getExtras();
        mProfile = new Profile();
        mProfile.USER_NAME = bundle.getString("username");
        mProfile.USER_EMAIL = bundle.getString("useremail");
        mProfile.USER_IMAGE = bundle.getString("userimage");
        device=bundle.getString("device");

        // 設定 Google帳戶大頭貼
        setmProfileImage(mProfile.USER_IMAGE);

        // Google帳號、姓名 findViewById
        MyProfileName = (TextView) findViewById(R.id.myprofile_name);
        MyProfileEmail = (TextView) findViewById(R.id.myprofile_email);

        // 設定Google帳號、姓名
        MyProfileName.setText(mProfile.USER_NAME);
        MyProfileEmail.setText(mProfile.USER_EMAIL);

        // 按鈕findViewById
        ChangeUserBtn = (Button) findViewById(R.id.changeuser_btn);
        NextBtn = (Button) findViewById(R.id.next_btn);

        // 設定按鈕監聽
        ChangeUserBtn.setOnClickListener(this);
        NextBtn.setOnClickListener(this);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

        //player = new Player("http://163.17.135.75/TTS/Lelogin/profile.mp3");
       // player.play();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.changeuser_btn){
            onChangeUserClick();
        }
        else if(v.getId() == R.id.next_btn){
            onNextClick();
            Log.e("onClick","123456");
        }
    }

    // 設定 Google帳戶大頭貼
    private void setmProfileImage(String imageurl){

        AQuery aq = new AQuery(this);

        // 相片很大，不用記體體快取
        boolean memCache = false;
        boolean fileCache = true;

        aq.id(R.id.myprofile_image).image(imageurl, memCache, fileCache);
    }

    // 按下切換使用者按鈕回前頁
    public void onChangeUserClick(){
        MyProfile.this.finish();
    }

    // 按下下一步按鈕後進入連結Glass的畫面
    public void onNextClick(){

        connDb0();
    }

    // 登入，檢查是否已註冊為使用者，之後跳轉到 profile頁面
    private void connDb0(){

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                GetServerMessage message = new GetServerMessage();
                msg = message.all("http://163.17.135.76/new_glass/selectuser.php","MemberEmail="+mProfile.USER_EMAIL);
                handler.post(update);
            }

        }).start();


    }

    private void addlogin(){

        AQuery aq1 = new AQuery(this);
        String url = "http://163.17.135.76/new_glass/addlogin.php";

        Map<String,Object> params = new HashMap<String, Object>();

        //測試用
        params.put("MemberEmail", mProfile.USER_EMAIL);

        aq1.ajax(url, params, String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String result, AjaxStatus status) {
                //連線成功
                if (status.getCode() == 200) {
                    useGlassOrPhone(result);
                }
                //失敗傳回HTTP狀態碼
                else {
                    Toast.makeText(getApplicationContext(), String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void useGlassOrPhone(String result){
        if(device.equals("glass")){
            Intent it = new Intent(MyProfile.this, GlassMap.class);
            Bundle bundle = new Bundle();
            bundle.putString("memberId", result);
            it.putExtras(bundle);
            MyProfile.this.startActivity(it);
        }
        else if(device.equals("phone")){
            Intent it = new Intent(MyProfile.this, CreativeGlassStart.class);
            Bundle bundle = new Bundle();
            bundle.putString("memberId", result);
            it.putExtras(bundle);
            MyProfile.this.startActivity(it);
        }
    }

    final Runnable update = new Runnable()
    {
        @Override
        public void run()
        {

            switch (msg) {
                case "yes":
                    addlogin();
                    break;
                case "no":
                    Intent it = new Intent(MyProfile.this, SetGender.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("username", mProfile.USER_NAME);
                    bundle.putString("useremail", mProfile.USER_EMAIL);
                    bundle.putString("userimage", mProfile.USER_IMAGE);
                    bundle.putString("device",device);

                    it.putExtras(bundle);
                    MyProfile.this.startActivity(it);
                    Log.e("PETER", "---");
                    break;
                default:
                    Toast.makeText(MyProfile.this, "Google帳戶登入失敗"+msg, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        switch (requestCode) {
            case MY_DATA_CHECK_CODE:
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    // success, create the TTS instance
                    tts = new TextToSpeech(this, this);
                }
                else {
                    // missing data, install it
                    Intent installIntent = new Intent();
                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
                break;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(MyProfile.this,
                    "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
            tts.speak("請確認畫面上的資料是否為要登入的帳戶", TextToSpeech.QUEUE_ADD, null);
        }
        else if (status == TextToSpeech.ERROR) {
            Toast.makeText(MyProfile.this,
                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
        }
    }
}
