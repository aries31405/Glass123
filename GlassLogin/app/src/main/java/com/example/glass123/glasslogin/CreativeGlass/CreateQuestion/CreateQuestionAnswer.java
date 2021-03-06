package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.glass123.glasslogin.R;

public class CreateQuestionAnswer extends Fragment{
    ImageButton createqnext_btn;
    EditText answer;
    private double latitude=0.0,longitude=0.0;

    public interface Listener {
        public void saveAnswer(String ans);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_create_question_answer,container,false);

        //init
        answer= (EditText)v.findViewById(R.id.answer);
        answer.addTextChangedListener(textchangelistener);

        return v;
    }

    private TextWatcher textchangelistener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ((Listener)getActivity()).saveAnswer(answer.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //    @Override
//    public void onStop() {
//        super.onStop();
//        ((Listener) getActivity()).saveAnswer(answer.getText().toString());
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        ((Listener) getActivity()).saveAnswer(answer.getText().toString());
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        ((Listener) getActivity()).saveAnswer(answer.getText().toString());
//    }
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_question_answer);
//
////        //button init
////        createqnext_btn = (ImageButton)findViewById(R.id.createqnext_btn);
////        createqnext_btn.setOnClickListener(this);
//
//
//
//        //edittext init
//        answer= (EditText)findViewById(R.id.hint2);
//
//        Bundle bundle = this.getIntent().getExtras();
//        latitude = bundle.getDouble("lat");
//        longitude = bundle.getDouble("lon");
//
//    }
//
////    @Override
////    public void onClick(View view) {
////        if(view.getId() == R.id.createqnext_btn){
////            CreateQuestionNext();
////        }
////    }
//
//    private void CreateQuestionNext(){
//        Bundle bundle = new Bundle();
//        bundle.putString("answer",answer.getText().toString());
//        bundle.putDouble("lat",latitude);
//        bundle.putDouble("lon",longitude);
//        Intent intent = new Intent(CreateQuestionAnswer.this,CreateHint1.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_create_question_answer, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
