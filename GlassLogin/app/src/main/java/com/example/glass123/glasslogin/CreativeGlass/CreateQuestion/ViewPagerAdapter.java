package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by user on 2016/2/29.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumberOfTabs;

    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[],int mNumberOfTabs) {
        super(fm);

        this.Titles = mTitles;
        this.NumberOfTabs = mNumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            CreateQuestionAnswer cqa = new CreateQuestionAnswer();
            return cqa;
        }
        else if(position == 1)
        {
            CreateHint1 ch1 = new CreateHint1();
            return ch1;
        }
        else if(position == 2)
        {
            CreateHint2 ch2 = new CreateHint2();
            return ch2;
        }
        else if(position == 3)
        {
            CreateHint3 ch3 = new CreateHint3();
            return ch3;
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
