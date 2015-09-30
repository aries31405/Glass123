package com.example.gameking_var2.remoteproject.CardsAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gameking_var2.remoteproject.R;

/**
 * Created by 孔雀舞 on 2015/9/30.
 */
public class Toictext extends FrameLayout {

    public Toictext(Context context, int ResLayoutId,String text)
    {
        super(context);

        //初始化版面
        initView(ResLayoutId,text);
    }

    private void initView(int ResLayoutId,String text)
    {
        //將定義好的加入這個View
        View view = inflate(getContext(), ResLayoutId, null);

        //答題狀態 星等數量
        TextView tv;

        //初始化 抓元件
        tv  = (TextView) view.findViewById(R.id.ttext);

        tv.setText(text);


        addView(view);
    }
}
