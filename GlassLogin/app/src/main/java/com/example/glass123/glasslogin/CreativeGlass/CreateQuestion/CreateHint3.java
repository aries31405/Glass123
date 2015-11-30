package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.glass123.glasslogin.R;

/**
 * Created by seahorse on 2015/11/28.
 */
public class CreateHint3 extends AppCompatActivity implements View.OnClickListener {
    ImageButton createqnext_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hint3);

        createqnext_btn = (ImageButton)findViewById(R.id.createqnext_btn);
        createqnext_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.createqnext_btn){
            CreateQuestionNext();
        }
    }

    private void CreateQuestionNext(){
        Intent intent = new Intent(CreateHint3.this,CreateQuestionSend.class);
        startActivity(intent);
    }
}
