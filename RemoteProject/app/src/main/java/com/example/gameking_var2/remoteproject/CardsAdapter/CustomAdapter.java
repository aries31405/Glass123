package com.example.gameking_var2.remoteproject.CardsAdapter;


//CustomXml Adapter

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;

import java.util.List;

public class CustomAdapter extends CardScrollAdapter
{
    final List<View> mCards;

    public CustomAdapter(List<View> cards)
    {
        mCards = cards;
    }

    @Override
    public int getCount()
    {
        return mCards.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mCards.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return mCards.get(position);
    }

    @Override
    public int getViewTypeCount()
    {
        return CardBuilder.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    @Override
    public int getPosition(Object item)
    {
        for (int i = 0; i < mCards.size(); i++)
        {
            if (getItem(i).equals(item))
            {
                return i;
            }
        }
        return AdapterView.INVALID_POSITION;
    }

    //新增卡片
    public void insertCard(int position, View card)
    {
        mCards.add(position, card);
    }

    //刪除卡片
    public void deleteCard(int position)
    {
        mCards.remove(position);
    }

    //清除
    public void clearCard()
    {
        mCards.clear();
    }
}
