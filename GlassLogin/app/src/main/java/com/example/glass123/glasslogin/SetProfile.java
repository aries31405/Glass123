package com.example.glass123.glasslogin;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.glass123.glasslogin.Bluetooth.Profile;
import com.example.glass123.glasslogin.Mplayer.Player;

public class SetProfile extends Activity implements View.OnClickListener,TextToSpeech.OnInitListener{

    Button mNextBtn;
    Button mForwardBtn;

    RadioButton mMaleRBtn;
    RadioButton mFemaleRBtn;

    Profile mProfile;

    String device="";

    private static final int MY_DATA_CHECK_CODE = 0;
    TextToSpeech tts;
    Player player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        // 性別單選按鈕findViewById
        mMaleRBtn = (RadioButton) findViewById(R.id.male_radio);
        mFemaleRBtn = (RadioButton) findViewById(R.id.female_radio);

        // 取得MyProfile傳過來的使用者Google帳戶資料
        Bundle bundle = this.getIntent().getExtras();
        mProfile = new Profile();
        mProfile.USER_NAME = bundle.getString("username");
        mProfile.USER_EMAIL = bundle.getString("useremail");
        mProfile.USER_IMAGE = bundle.getString("userimage");
        device = bundle.getString("device");

        // 按鈕初始化
        mNextBtn = (Button)findViewById(R.id.set_next);
        mForwardBtn = (Button)findViewById(R.id.set_forward);

        // 設定按鈕監聽
        mNextBtn.setOnClickListener(this);
        mForwardBtn.setOnClickListener(this);

        // 預設為男性
        mProfile.USER_SEX="0";

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent,MY_DATA_CHECK_CODE);

        //player = new Player("http://163.17.135.75/TTS/Lelogin/gender.mp3");
        //player.play();
    }

    // 覆寫View.OnClickListener的onClick
    @Override
    public void onClick(View v){

        // 按下下一步按鈕
        if(v.getId() == R.id.set_next)
        {
            onNextClick();
        }
        // 按下上一步按鈕
        else if(v.getId() == R.id.set_forward)
        {
            onForwardClick();
        }

    }

    // 按下一步按鈕，跳轉到設定性別的頁面
    private void onNextClick(){
        if(mProfile.USER_SEX.equals("")){
            Toast.makeText(getApplicationContext(),"請選擇性別！",Toast.LENGTH_SHORT).show();
        }
        else{
//            if(player.pause())
//            {
//
//            }
            Intent it = new Intent(SetProfile.this, SetProfileAge.class);

            Bundle bundle = new Bundle();
            bundle.putString("username", mProfile.USER_NAME);
            bundle.putString("useremail", mProfile.USER_EMAIL);
            bundle.putString("userimage", mProfile.USER_IMAGE);
            bundle.putString("usersex",mProfile.USER_SEX);
            bundle.putString("device",device);

            it.putExtras(bundle);
            startActivity(it);
        }
    }

    // 按上一步按鈕，回到檢視使用者頁面
    private void onForwardClick(){
        this.finish();
    }

    // 在RadioButton被打勾時自動呼叫
    public void onRadioButtonClicked(View view) {

        // 檢查RadioButton有沒有被打勾
        boolean checked = ((RadioButton) view).isChecked();

        // 查看哪個RadioButton被打勾
        switch(view.getId()) {
            // 男
            case R.id.male_radio:
                if (checked)
                {
                    // 設定為男性
                    mProfile.USER_SEX = "1";
                    Log.e("onRadioButtonClicked",mProfile.USER_SEX + "!");

                    mFemaleRBtn.setChecked(false);
                }
                break;
            // 女
            case R.id.female_radio:
                if (checked)
                {
                    // 設定為女性
                    mProfile.USER_SEX="0";
                    Log.e("onRadioButtonClicked",mProfile.USER_SEX + "!");

                    mMaleRBtn.setChecked(false);
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_profile, menu);
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
            Toast.makeText(SetProfile.this,
                    "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
            tts.speak("請選擇性別", TextToSpeech.QUEUE_ADD, null);
        }
        else if (status == TextToSpeech.ERROR) {
            Toast.makeText(SetProfile.this,
                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
        }
    }
}
