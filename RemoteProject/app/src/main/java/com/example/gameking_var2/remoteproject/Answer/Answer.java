package com.example.gameking_var2.remoteproject.Answer;

import com.example.gameking_var2.remoteproject.R;
import com.example.gameking_var2.remoteproject.Rank.Rank;
import com.google.android.glass.app.Card;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

/*
答題頁面
功能：
１．利用語音出輸入答案後上傳
－－－－－－－
一指上傳
二指重新輸入
*/

public class Answer extends Activity
{


    protected static final int RESULT_SPEECH = 1;

    //定義手勢偵測
    private GestureDetector GestureDetector;

    Card card;

    private CardScrollView mCardScroller;

    private View mView;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        card.setText("請點擊並語音輸入");
        View view = card.getView();
        setContentView(view);

    }

   /* private void speech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.EXTRA_LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-us");

        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(), "Ops! Your device doesn't support Speech to Text", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    card = new Card(this);
                    card.setText(text.get(0));
                    View view = card.getView();
                    setContentView(view);
                }
                break;
            }

        }
    }*/
}
