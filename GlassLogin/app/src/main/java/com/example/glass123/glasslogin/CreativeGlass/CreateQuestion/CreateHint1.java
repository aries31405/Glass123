package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.glass123.glasslogin.R;

public class CreateHint1 extends Fragment {
    ImageButton createqnext_btn;
    EditText hint1;
    String answer;
    private double latitude=0.0,longitude=0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_create_hint1,container,false);
        return v;
    }
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_hint1);
//
//        //button init
//        createqnext_btn = (ImageButton)findViewById(R.id.createqnext_btn);
//        createqnext_btn.setOnClickListener(this);
//
//        //edittext init
//        hint1 = (EditText)findViewById(R.id.hint1_txt);
//
//        //取得前一Activity的東西
//        Bundle bundle = this.getIntent().getExtras();
//        answer = bundle.getString("answer");
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
//        Bundle bundle = new Bundle();
//        bundle.putString("answer",answer);
//        bundle.putString("hint1",hint1.getText().toString());
//        bundle.putDouble("lat",latitude);
//        bundle.putDouble("lon",longitude);
//        Intent intent = new Intent(CreateHint1.this,CreateHint2.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_create_hint1, menu);
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
