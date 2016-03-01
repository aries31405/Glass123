package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.glass123.glasslogin.R;

/**
 * Created by seahorse on 2015/11/28.
 */
public class CreateHint2 extends Fragment {
    ImageButton createqnext_btn;
    EditText hint2;
    String answer,hint1;
    private double latitude=0.0,longitude=0.0;

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_create_hint2,container,false);
        return v;
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_hint2);
//
//        //button init
//        createqnext_btn = (ImageButton)findViewById(R.id.createqnext_btn);
//        createqnext_btn.setOnClickListener(this);
//
//        //edittext init
//        hint2 = (EditText)findViewById(R.id.hint2);
//
//        Bundle bundle = this.getIntent().getExtras();
//        answer = bundle.getString("answer");
//        hint1 = bundle.getString("hint1");
//        latitude = bundle.getDouble("lat");
//        longitude = bundle.getDouble("lon");
//    }
//
//    @Override
//    public void onClick(View view) {
//        if(view.getId() == R.id.createqnext_btn){
//            CreateQuestionNext();
//        }
//    }
//
//    private void CreateQuestionNext(){
//        Intent intent = new Intent(CreateHint2.this,CreateHint3.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("answer",answer);
//        bundle.putString("hint1",hint1);
//        bundle.putString("hint2", hint2.getText().toString());
//        bundle.putDouble("lat",latitude);
//        bundle.putDouble("lon",longitude);
//        intent.putExtras(bundle);
//        startActivity(intent);
//
//    }

}
