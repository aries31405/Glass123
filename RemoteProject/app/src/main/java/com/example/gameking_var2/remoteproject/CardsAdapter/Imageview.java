package com.example.gameking_var2.remoteproject.CardsAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.gameking_var2.remoteproject.R;


/**
 * Created by 孔雀舞 on 2015/9/29.
 */
public class Imageview extends FrameLayout {

    public Imageview(Context context, int ResLayoutId, Bitmap bitmap)
    {
        super(context);

        //初始化版面
        initView(ResLayoutId,bitmap);
    }

    private void initView(int ResLayoutId,Bitmap bitmap)
    {
        //將定義好的加入這個View
        View view = inflate(getContext(), ResLayoutId, null);

        //答題狀態 星等數量
        ImageView Iv;

        //初始化 抓元件
        Iv  = (ImageView) view.findViewById(R.id.t_image);

        Iv.setImageBitmap(bitmap);


        addView(view);
    }
}
