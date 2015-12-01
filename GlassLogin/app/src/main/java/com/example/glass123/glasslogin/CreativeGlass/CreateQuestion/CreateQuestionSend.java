package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.glass123.glasslogin.R;

/**
 * Created by seahorse on 2015/11/28.
 */
public class CreateQuestionSend extends AppCompatActivity{
    TextView hint1_tv,hint2_tv;
    String hint1,hint2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_send);

        hint1_tv = (TextView)findViewById(R.id.hint2_tv);
        hint2_tv = (TextView)findViewById(R.id.hint2_tv);

        Bundle bundle = this.getIntent().getExtras();
        hint1 = bundle.getString("hint1");
        hint2 = bundle.getString("hint2");
        hint1_tv.setText(hint1);
        hint2_tv.setText(hint2);

    }
}
