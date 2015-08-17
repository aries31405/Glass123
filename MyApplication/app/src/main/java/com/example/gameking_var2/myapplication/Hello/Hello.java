package com.example.gameking_var2.myapplication.Hello;

import com.example.gameking_var2.myapplication.cardAdapter.CardAdapter;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Activity} showing a tuggable "Hello World!" card.
 * <p/>
 * The main content view is composed of a one-card {@link CardScrollView} that provides tugging
 * feedback to the user when swipe gestures are detected.
 * If your Glassware intends to intercept swipe gestures, you should set the content view directly
 * and use a {@link com.google.android.glass.touchpad.GestureDetector}.
 *
 * @see <a href="https://developers.google.com/glass/develop/gdk/touch">GDK Developer Guide</a>
 */
public class Hello extends Activity
{

    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    //定義卡片  後面的數字是卡片順序  無法顛倒（創造卡片時必須按照順序）
    static final int About = 0;
    static final int TowDotZero = 1;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        mAdapter = new CardAdapter(createCards(this));

        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(mAdapter);

        setContentView(mCardScroller);
    }

    private List<CardBuilder> createCards(Context context)
    {
        //List的卡片創建／／
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();

        //逐一建造
        cards.add
        (
                About, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("關於我們")
        );
        cards.add
        (
                TowDotZero, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("成之內２．０")
        );

        return cards;
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
