package com.example.glass123.glasslogin.CreativeGlass;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.FindMap;
import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.FindQuestion;
import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.QuestionInfo;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateHint1;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateQuestionAnswer;
import com.example.glass123.glasslogin.CreativeGlass.MyCreative.MyCreative;
import com.example.glass123.glasslogin.MainActivity;
import com.example.glass123.glasslogin.MapsActivity;
import com.example.glass123.glasslogin.R;

import java.util.Calendar;

public class CreativeGlassStart extends Activity implements View.OnClickListener{

    //按鈕
    private ImageButton answer_btn;
    private ImageButton create_btn;
    private ImageButton profile_btn;

    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creative_glass_start);

        Bundle bundle= this.getIntent().getExtras();
        memberId = bundle.getString("memberId");
        Log.e("PETER",memberId);

        //按鈕init
        answer_btn = (ImageButton)findViewById(R.id.answer_btn);
        create_btn = (ImageButton)findViewById(R.id.create_btn);
        profile_btn = (ImageButton)findViewById(R.id.profile_btn);

        //按鈕設定監聽
        answer_btn.setOnClickListener(this);
        create_btn.setOnClickListener(this);
        profile_btn.setOnClickListener(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //在此返回上一頁視同放棄作答
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            closeapp();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeapp(){
        new AlertDialog.Builder(CreativeGlassStart.this)
                .setCancelable(false)
                .setTitle("登出")
                .setMessage("要否登出回到主畫面?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CreativeGlassStart.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        CreativeGlassStart.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_creative_glass_start, menu);
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
    public void onClick(View v) {
        if(v.getId() == R.id.answer_btn)
        {
            Intent intent = new Intent(CreativeGlassStart.this,FindMap.class);
            Bundle bundle = new Bundle();
            bundle.putString("memberId", memberId);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(v.getId() == R.id.create_btn)
        {
            Intent intent = new Intent(CreativeGlassStart.this, MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("memberId", memberId);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(v.getId() == R.id.profile_btn)
        {
            Intent intent = new Intent(CreativeGlassStart.this, MyCreative.class);
            Bundle bundle = new Bundle();
            bundle.putString("memberId", memberId);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
