package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.glass123.glasslogin.R;

/**
 * Created by seahorse on 2015/11/28.
 */
public class CreateHint3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hint3);
    }

    private void CreateQuestionNext(){
        Intent intent = new Intent(CreateHint3.this,CreateQuestionSend.class);
        startActivity(intent);
    }
}
