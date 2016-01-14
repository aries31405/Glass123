package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.glass123.glasslogin.R;

public class Hints extends Activity {

    Button answer_btn;
    Button abort_btn;

    int titleId=0;
    String answer;
    String hint1;
    String hint2;
    String hint3;

    TextView hint1_txt;
    TextView hint2_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        //init
        answer_btn = (Button)findViewById(R.id.answer_btn);
        abort_btn = (Button)findViewById(R.id.abort_btn);

        //作答按鈕監聽器
        answer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer();
            }
        });

        //放棄按鈕監聽器
        abort_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abort();
            }
        });

        //接bundle
        Bundle bundle = this.getIntent().getExtras();
        answer = bundle.getString("answer");
        hint1 = bundle.getString("hint1");
        hint2 = bundle.getString("hint2");
        hint3 = bundle.getString("hint3");
        titleId = bundle.getInt("titleId");

        //init
        hint1_txt=(TextView)findViewById(R.id.hint1_txt);
        hint2_txt=(TextView)findViewById(R.id.hint2_txt);

        hint1_txt.setText(hint1);
        hint2_txt.setText(hint2);


    }

    //進入作答頁面
    private void answer(){
        Bundle bundle = new Bundle();
        bundle.putString("answer",answer);
        bundle.putInt("titleId",titleId);
        Intent intent = new Intent(Hints.this,Answer.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //放棄作答，還需寫回資料庫
    private void abort(){
        new AlertDialog.Builder(Hints.this)
                .setTitle("放棄")
                .setMessage("要放棄解答此題目嗎?")
                .setPositiveButton("放棄", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //放棄，寫回資料庫


                        Hints.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //返回鍵
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            abort();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hints, menu);
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
