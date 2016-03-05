package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateHint1;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateHint2;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateHint3;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateQuestionAnswer;

/**
 * Created by user on 2016/2/29.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumberOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumberOfTabs) {
        super(fm);

        this.Titles = mTitles;
        this.NumberOfTabs = mNumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            Hint1 h1 = new Hint1();
            return h1;
        }
        else if(position == 1)
        {
            Hint2 h2 = new Hint2();
            return h2;
        }
        else if(position == 2)
        {
            Hint3 h3 = new Hint3();
            return h3;
        }
        else if(position == 3)
        {
            Answer ans = new Answer();
            return ans;
        }
        else
        {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumberOfTabs;
    }

}
