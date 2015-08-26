package com.example.gameking_var2.remoteproject.Rank;

import com.example.gameking_var2.remoteproject.R;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

/*
給予題目評價
 */

public class Rank extends Activity
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
                openOptionsMenu();
            }
        });
        setContentView(mCardScroller);
    }

    //建立選單
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rank_menu, menu);
        return true;
    }

    //點擊選單
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            default:
                return true;
        }
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
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);

        card.setText("給予題目評價頁面");
        return card.getView();
    }

}
