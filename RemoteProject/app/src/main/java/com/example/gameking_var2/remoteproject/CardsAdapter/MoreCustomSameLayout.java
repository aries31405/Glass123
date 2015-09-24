package com.example.gameking_var2.remoteproject.CardsAdapter;


import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gameking_var2.remoteproject.R;

public class MoreCustomSameLayout extends FrameLayout
{
    public MoreCustomSameLayout (Context context, int ResLayoutId, int states, int number, int star, int persent, String createName)
    {
        super(context);

        //初始化版面
        initView(ResLayoutId, states, number, star, persent, createName);
    }

    private void initView(int ResLayoutId, int states, int number, int star, int persent, String createName)
    {
        //將定義好的加入這個View
        View view = inflate(getContext(), ResLayoutId, null);

        //答題狀態 星等數量
        ImageView statesIV, starIV;

        //題目編號  答對率  題目創立者
        TextView  numberTV, persentTV, createTV;

        //初始化 抓元件
        statesIV  = (ImageView) view.findViewById(R.id.neworclear);
        starIV    = (ImageView) view.findViewById(R.id.rank_star);

        numberTV  = (TextView)  view.findViewById(R.id.title_number);
        persentTV = (TextView)  view.findViewById(R.id.percent);
        createTV  = (TextView)  view.findViewById(R.id.create_name);

        //--根據傳值換元件狀態--//
        //判斷題目狀態
        switch (states)
        {
            //new
            case 0:
                statesIV.setImageResource(R.drawable.new_text);
                break;
            //none
            case 1:
                break;
            //clear
            case 2:
                statesIV.setImageResource(R.drawable.clear_text);
                break;
        }

        //設定題目編號
        numberTV.setText("NO." + number);

        //判斷星數
        switch (star)
        {
            case 0:
                starIV.setImageResource(R.drawable.no_star);
                break;
            case 1:
                starIV.setImageResource(R.drawable.one_star);
                break;
            case 2:
                starIV.setImageResource(R.drawable.two_star);
                break;
            case 3:
                starIV.setImageResource(R.drawable.three_star);
                break;
            case 4:
                starIV.setImageResource(R.drawable.four_star);
                break;
            case 5:
                starIV.setImageResource(R.drawable.five_star);
                break;
        }

        //設定答對率
        persentTV.setText("Correct rate: " + persent + "%");

        //設定出題者
        createTV.setText(createName);

        addView(view);
    }
}
