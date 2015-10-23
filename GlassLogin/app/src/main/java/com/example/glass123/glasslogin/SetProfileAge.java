package com.example.glass123.glasslogin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.glass123.glasslogin.Bluetooth.BluetoothChatFragment;
import com.example.glass123.glasslogin.Bluetooth.Profile;
import com.example.glass123.glasslogin.Mplayer.Player;

import org.w3c.dom.Text;

public class SetProfileAge extends Activity implements View.OnClickListener, View.OnTouchListener,TextToSpeech.OnInitListener{

    private static final int MY_DATA_CHECK_CODE = 0;

    Button mNextBtn;
    Button mForwardBtn;

    Profile mProfile;

    private ViewFlipper mViewFlipper1;
    private ViewFlipper mViewFlipper2;
    private float touchDownX;
    private float touchUpX;
    private float touchDownY;
    private float touchUpY;

    private TextToSpeech tts;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_age);

        // 取得SetProfile傳過來的使用者Google帳戶資料
        Bundle bundle = this.getIntent().getExtras();
        mProfile = new Profile();
        mProfile.USER_NAME = bundle.getString("username");
        mProfile.USER_EMAIL = bundle.getString("useremail");
        mProfile.USER_IMAGE = bundle.getString("userimage");
        mProfile.USER_SEX = bundle.getString("usersex");

        // 按鈕初始化
        mNextBtn = (Button)findViewById(R.id.set_age_next);
        mForwardBtn = (Button)findViewById(R.id.set_age_forward);

        // 設定按鈕監聽
        mNextBtn.setOnClickListener(this);
        mForwardBtn.setOnClickListener(this);

        mViewFlipper1 = (ViewFlipper)findViewById(R.id.viewFlipper1);
        mViewFlipper2 = (ViewFlipper)findViewById(R.id.viewFlipper2);


        int i = 0;

        for(i=0;i<10;i++){

            // 十位數
            TextView tv1 = new TextView(this);

            tv1.setText(String.valueOf(i));
            tv1.setTextColor(this.getResources().getColor(R.color.white));
            tv1.setTextSize(150);

            LinearLayout lq1 = new LinearLayout(this);
            lq1.addView(tv1);
            mViewFlipper1.addView(lq1);


            // 個位數
            TextView tv2 = new TextView(this);

            tv2.setText(String.valueOf(i));
            tv2.setTextColor(this.getResources().getColor(R.color.white));
            tv2.setTextSize(150);

            LinearLayout lq2 = new LinearLayout(this);
            lq2.addView(tv2);

            mViewFlipper2.addView(lq2);
        }

        mViewFlipper1.setOnTouchListener(this);
        mViewFlipper2.setOnTouchListener(this);


        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

        //player = new Player("http://163.17.135.75/TTS/Lelogin/age.mp3");
        //player.play();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.viewFlipper1:
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //X
                    touchDownY = event.getY();
                    touchDownX = event.getX();
                    //Toast.makeText(this, "sdf" , Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    //X
                    touchUpY = event.getY();
                    touchUpX = event.getX();
                    //
                    if (touchUpY - touchDownY > 100)
                    {
                        //
                        mViewFlipper1.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
                        mViewFlipper1.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
                        //View
                        mViewFlipper1.showPrevious();
                        //View
                    }
                    else if (touchDownY - touchUpY > 100)
                    {
                        //
                        mViewFlipper1.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_in));
                        mViewFlipper1.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_out));
                        //
                        mViewFlipper1.showNext();
                    }
                    if (touchUpX - touchDownX > 100)
                    {
                        //
                        mViewFlipper1.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in_a));
                        mViewFlipper1.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out_a));
                        //View
                        mViewFlipper1.setDisplayedChild(mViewFlipper1.getDisplayedChild() + 10);
                        //View
                    }
                    else if (touchDownX - touchUpX > 100)
                    {
                        //
                        mViewFlipper1.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_in_a));
                        mViewFlipper1.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_out_a));
                        //
                        mViewFlipper1.setDisplayedChild( mViewFlipper1.getDisplayedChild() - 10 );
                    }
                    return true;
                }
                break;
            case R.id.viewFlipper2:
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //X
                    touchDownY = event.getY();
                    touchDownX = event.getX();
                    //Toast.makeText(this, "sdf" , Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    //X
                    touchUpY = event.getY();
                    touchUpX = event.getX();
                    //
                    if (touchUpY - touchDownY > 100)
                    {
                        //
                        mViewFlipper2.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
                        mViewFlipper2.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
                        //View
                        mViewFlipper2.showPrevious();
                        //View
                    }
                    else if (touchDownY - touchUpY > 100)
                    {
                        //
                        mViewFlipper2.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_in));
                        mViewFlipper2.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_out));
                        //
                        mViewFlipper2.showNext();
                    }
                    if (touchUpX - touchDownX > 100)
                    {
                        //
                        mViewFlipper2.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in_a));
                        mViewFlipper2.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out_a));
                        //View
                        mViewFlipper2.setDisplayedChild(mViewFlipper2.getDisplayedChild() + 10);
                        //View
                    }
                    else if (touchDownX - touchUpX > 100)
                    {
                        //
                        mViewFlipper2.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_in_a));
                        mViewFlipper2.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_out_a));
                        //
                        mViewFlipper2.setDisplayedChild( mViewFlipper2.getDisplayedChild() - 10 );
                    }
                    return true;
                }
                break;
        }

        return false;
    }


    // 覆寫View.OnClickListener的onClick
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.set_age_next)
        {
            onNextClick();
        }
        else if(v.getId() == R.id.set_age_forward)
        {
            onForwardClick();
        }

    }

    // 按下下一步按鈕，跳轉到藍芽連線頁面
    private void onNextClick(){

        mProfile.USER_AGE = String.valueOf(mViewFlipper1.getDisplayedChild() * 10 + mViewFlipper2.getDisplayedChild());
//        if(player.pause())
//        {
//
//        }
        Intent it = new Intent(SetProfileAge.this, ChooseDevice.class);

        Bundle bundle = new Bundle();
        bundle.putString("username", mProfile.USER_NAME);
        bundle.putString("useremail", mProfile.USER_EMAIL);
        bundle.putString("userimage", mProfile.USER_IMAGE);
        bundle.putString("usersex",mProfile.USER_SEX);
        bundle.putString("userage",mProfile.USER_AGE);

        it.putExtras(bundle);
        startActivity(it);

    }

    // 按上一步按鈕，回到檢視使用者頁面
    private void onForwardClick(){
        this.finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        //this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_profile_age, menu);
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
            Toast.makeText(SetProfileAge.this,
                    "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
            tts.speak("請選擇年齡", TextToSpeech.QUEUE_ADD, null);
        }
        else if (status == TextToSpeech.ERROR) {
            Toast.makeText(SetProfileAge.this,
                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
        }
    }
}
