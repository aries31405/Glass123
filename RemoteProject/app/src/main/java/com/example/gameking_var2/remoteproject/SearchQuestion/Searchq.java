package com.example.gameking_var2.remoteproject.SearchQuestion;

import com.example.gameking_var2.remoteproject.Answer.TitleCard;
import com.example.gameking_var2.remoteproject.R;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

/*
尋找題目頁面  一個題目動態新增一張卡片
在該卡片雙指跳出題目評價
三指選擇樓層
*/


public class Searchq extends Activity implements GestureDetector.BaseListener
{

    private CardScrollView mCardScroller;

    private View mView;

    //定義手勢偵測
    private GestureDetector GestureDetector;

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
        // Handle the TAP event.
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Searchq.this, TitleCard.class));
            }
        });

        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);

        setContentView(mCardScroller);
    }

    //建立選單
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floor_menu, menu);
        return true;
    }

    //點擊選單
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

        }
        return true;
    }

    private View buildView()
    {
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.CAPTION);
        card.setText("尋找題目頁面");
        card.addImage(R.drawable.notable);
        return card.getView();
    }

    //偵測手勢動作，回傳事件
    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        return GestureDetector.onMotionEvent(event);
    }

    @Override
    public boolean onGesture(Gesture gesture)
    {
        //會傳入手勢  gesture.name()會取得手勢名稱 或是另一種 gesture ＝ Gesture.SWIPE_UP
        switch( gesture.name() )
        {
            case "SWIPE_DOWN":
                finish();
                break;
            case "TWO_TAP":
                //跳出題目評價
                Toast.makeText(Searchq.this, "跳出題目評價", Toast.LENGTH_SHORT).show();
                break;
            case "THREE_TAP":
                //樓層選單
                openOptionsMenu();
                break;
        }
        return false;
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
}
