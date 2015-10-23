package com.example.glass123.glasslogin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.glass123.glasslogin.Bluetooth.BluetoothChatFragment;
import com.example.glass123.glasslogin.Bluetooth.Profile;
import com.example.glass123.glasslogin.CreativeGlass.CreativeGlassMain;

public class ChooseDevice extends Activity implements View.OnClickListener {

    //使用者基本資料
    private Profile mProfile;

    //按鈕
    private Button device;
    private Button glass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);

        // 取得MainActivity傳過來的使用者Google帳戶資料
        Bundle bundle = this.getIntent().getExtras();
        mProfile = new Profile();
        mProfile.USER_NAME = bundle.getString("username");
        mProfile.USER_EMAIL = bundle.getString("useremail");
        mProfile.USER_IMAGE = bundle.getString("userimage");
        mProfile.USER_SEX = bundle.getString("usersex");
        mProfile.USER_AGE = bundle.getString("userage");

        //按鈕init
        device = (Button)findViewById(R.id.device);
        glass = (Button)findViewById(R.id.glass);

        //按鈕設定監聽
        device.setOnClickListener(this);
        glass.setOnClickListener(this);

    }

    //按鈕監聽
    @Override
    public void onClick(View v) {

        //沒有Google Glass的話，用手機平板使用
        if(v.getId() == R.id.device)
        {
            Intent it = new Intent(ChooseDevice.this, CreativeGlassMain.class);

            Bundle bundle = new Bundle();
            bundle.putString("username", mProfile.USER_NAME);
            bundle.putString("useremail", mProfile.USER_EMAIL);
            bundle.putString("userimage", mProfile.USER_IMAGE);
            bundle.putString("usersex",mProfile.USER_SEX);
            bundle.putString("userage",mProfile.USER_AGE);

            it.putExtras(bundle);
            startActivity(it);
        }
        //有Google Glass的話，則準備與Google Glass連接
        else if(v.getId() == R.id.glass)
        {
            Intent it = new Intent(ChooseDevice.this, BluetoothChatFragment.class);

            Bundle bundle = new Bundle();
            bundle.putString("username", mProfile.USER_NAME);
            bundle.putString("useremail", mProfile.USER_EMAIL);
            bundle.putString("userimage", mProfile.USER_IMAGE);
            bundle.putString("usersex",mProfile.USER_SEX);
            bundle.putString("userage",mProfile.USER_AGE);

            it.putExtras(bundle);
            startActivity(it);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_device, menu);
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
