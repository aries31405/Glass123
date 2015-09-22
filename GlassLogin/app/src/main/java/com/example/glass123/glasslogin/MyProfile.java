package com.example.glass123.glasslogin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.Bluetooth.BluetoothChatFragment;
import com.example.glass123.glasslogin.Bluetooth.Profile;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends Activity implements View.OnClickListener{

    private Profile mProfile;

    private CircleImageView mPhoto;

    private TextView MyProfileName;
    private TextView MyProfileEmail;

    private Button ChangeUserBtn;
    private Button NextBtn;

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


    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.changeuser_btn){
            onChangeUserClick();
        }
        else if(v.getId() == R.id.next_btn){
            onNextClick();
        }
    }

    // 設定 Google帳戶大頭貼
    private void setmProfileImage(String imageurl){

        AQuery aq = new AQuery(this);

        // 相片很大，不用記體體快取
        boolean memCache = false;
        boolean fileCache = true;

        aq.id(R.id.myprofile_image).image(imageurl,memCache,fileCache);
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

        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/db/selectusers.php";

        Map<String,Object> params = new HashMap<String, Object>();

        //測試用
        params.put("email", mProfile.USER_EMAIL);

        aq.ajax(url, params, String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String result, AjaxStatus status) {
                //連線成功
                if (status.getCode() == 200) {
                    //資料庫已經有使用者資料
                    if (result.equals("2")) {
                        Intent it = new Intent(MyProfile.this, BluetoothChatFragment.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("username", mProfile.USER_NAME);
                        bundle.putString("useremail", mProfile.USER_EMAIL);
                        bundle.putString("userimage", mProfile.USER_IMAGE);

                        it.putExtras(bundle);
                        startActivity(it);
                    }
                    else if (result.equals("0")) {
                        Intent it = new Intent(MyProfile.this, SetProfile.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("username", mProfile.USER_NAME);
                        bundle.putString("useremail", mProfile.USER_EMAIL);
                        bundle.putString("userimage", mProfile.USER_IMAGE);

                        it.putExtras(bundle);
                        startActivity(it);
                    } else {
                        Toast.makeText(MyProfile.this, "Google帳戶登入失敗", Toast.LENGTH_SHORT).show();
                    }

                }
                //失敗傳回HTTP狀態碼
                else {
                    Toast.makeText(getApplicationContext(), String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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
}
