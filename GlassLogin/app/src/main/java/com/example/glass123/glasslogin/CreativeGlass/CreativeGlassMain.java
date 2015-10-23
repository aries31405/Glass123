package com.example.glass123.glasslogin.CreativeGlass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.glass123.glasslogin.Bluetooth.Profile;
import com.example.glass123.glasslogin.ChooseDevice;
import com.example.glass123.glasslogin.R;

public class CreativeGlassMain extends Activity implements View.OnClickListener{

    //使用者基本資料
    private Profile mProfile;

    //按鈕
    private Button start_btn;
    private Button help_btn;
    private Button about_btn;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creative_glass_main);

        // 取得MainActivity傳過來的使用者Google帳戶資料
        Bundle bundle = this.getIntent().getExtras();
        mProfile = new Profile();
        mProfile.USER_NAME = bundle.getString("username");
        mProfile.USER_EMAIL = bundle.getString("useremail");
        mProfile.USER_IMAGE = bundle.getString("userimage");
        mProfile.USER_SEX = bundle.getString("usersex");
        mProfile.USER_AGE = bundle.getString("userage");

        //按鈕init
        start_btn = (Button)findViewById(R.id.start_btn);
        help_btn = (Button)findViewById(R.id.help_btn);
        about_btn = (Button)findViewById(R.id.about_btn);

        //按鈕設定監聽
        start_btn.setOnClickListener(this);
        help_btn.setOnClickListener(this);
        about_btn.setOnClickListener(this);

        //Toast
        toast = new Toast(getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.start_btn)
        {
            Intent intent = new Intent(CreativeGlassMain.this,CreativeGlassStart.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.help_btn)
        {

        }
        else if(v.getId() == R.id.about_btn)
        {
            toast.cancel();
            toast.makeText(getApplicationContext(),"by 城之內2.0",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_creative_glass_main, menu);
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
