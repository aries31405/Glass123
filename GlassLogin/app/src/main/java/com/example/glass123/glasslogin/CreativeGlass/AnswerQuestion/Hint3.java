package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.glass123.glasslogin.R;

/**
 * Created by seahorse on 2016/3/5.
 */
public class Hint3 extends Fragment{
    WebView webView;
//    ImageView hint3_img;
    View v;

    public interface Listener{
        String getHint3();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_hint3,container,false);
webView = (WebView)v.findViewById(R.id.webView);
//        hint3_img= (ImageView)v.findViewById(R.id.hint3_img);
        setImage(((Listener) getActivity()).getHint3());



        return v;
    }

    public void setImage(String imageurl) {

        imageurl = "http://163.17.135.76"+imageurl;
        Log.e("PETER", "imageurl : " + imageurl);

        try{
            AQuery aq = new AQuery(getActivity(),v);


            // 相片很大，不用記體體快取
            boolean memCache = false;
            boolean fileCache = true;
            aq.id(R.id.webView).webImage(imageurl);
//            aq.id(R.id.hint3_img).image(imageurl,memCache,fileCache);
            Log.e("PETER","finish load");
        }
        catch (Exception e){
            Log.e("PETER", e.toString());
        }

    }


}
