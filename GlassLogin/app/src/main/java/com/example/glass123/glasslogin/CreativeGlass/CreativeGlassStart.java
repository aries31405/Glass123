package com.example.glass123.glasslogin.CreativeGlass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion.FindQuestion;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateHint1;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateQuestionAnswer;
import com.example.glass123.glasslogin.R;

public class CreativeGlassStart extends Activity implements View.OnClickListener{

    //按鈕
    private ImageButton answer_btn;
    private ImageButton create_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creative_glass_start);

        //按鈕init
        answer_btn = (ImageButton)findViewById(R.id.answer_btn);
        create_btn = (ImageButton)findViewById(R.id.create_btn);

        //按鈕設定監聽
        answer_btn.setOnClickListener(this);
        create_btn.setOnClickListener(this);

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
            Intent intent = new Intent(CreativeGlassStart.this,FindQuestion.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.create_btn)
        {
            Intent intent = new Intent(CreativeGlassStart.this,CreateQuestionAnswer.class);
            startActivity(intent);
        }
    }
}
