package com.example.gameking_var2.remoteproject.Answer;

import com.example.gameking_var2.remoteproject.R;
import com.example.gameking_var2.remoteproject.Rank.Rank;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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

    private CardScrollView mCardScroller;

    private View mView;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        mView = buildView();

        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(new CardScrollAdapter()
        {
            @Override
            public int getCount()
            {
                return 1;
            }

            @Override
            public Object getItem(int position)
            {
                return mView;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                return mView;
            }

            @Override
            public int getPosition(Object item)
            {
                if (mView.equals(item))
                {
                    return 0;
                }
                return AdapterView.INVALID_POSITION;
            }
        });

        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(Answer.this, Rank.class));
            }
        });
        setContentView(mCardScroller);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause()
    {
        mCardScroller.deactivate();
        super.onPause();
    }

    private View buildView()
    {
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.MENU);
        card.setText("語音輸入");
        card.setIcon(R.drawable.ic_microphone_on_50);
        return card.getView();
    }

}
