package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.glass123.glasslogin.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by seahorse on 2016/3/7.
 */
public class Commend extends Fragment implements View.OnTouchListener{

    ViewFlipper viewFlipper;
    private float touchDownX;
    private float touchUpX;
    private float touchDownY;
    private float touchUpY;
    int nowCommend=0;

    ArrayList<Integer> CommendNoList = new ArrayList<Integer>();
    ArrayList<String> CommendNameList = new ArrayList<String>();

    public interface Listener{
        void saveCommend(int commend);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_commend,container,false);

        getCommend();

        //把評價給ViewFlipper
        viewFlipper = (ViewFlipper)v.findViewById(R.id.viewFlipper);
        viewFlipper.setOnTouchListener(this);
        return v;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            touchDownX = event.getX();
            touchDownY = event.getY();
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            touchUpX=event.getX();
            touchUpY=event.getY();
            if(touchUpY-touchDownY > 100){
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_up_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_up_out));

                viewFlipper.showPrevious();
            }
            else if(touchDownY-touchUpY > 100){
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_down_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_down_out));

                viewFlipper.showNext();
            }

            nowCommend = CommendNoList.get(viewFlipper.getDisplayedChild());
            ((Listener)getActivity()).saveCommend(nowCommend);
            return true;
        }
        return false;
    }

    private void getCommend(){

        final AQuery aq = new AQuery(getActivity());
        String url = "http://163.17.135.76/new_glass/getcommend.php";

        Map<String,Object> params = new HashMap<String, Object>();

        aq.ajax(url,params,String.class,new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                //成功
                if(status.getCode() == 200)
                {
                    try
                    {
                        JSONArray jsonArray = new JSONArray(result);
                        for(int i = 0; i<jsonArray.length()/2;i++){
                            Log.e("PETER", String.valueOf(jsonArray.getInt(2 * i)));
                            Log.e("PETER", jsonArray.getString(2 * i + 1));
                            CommendNoList.add(jsonArray.getInt(2*i));
                            CommendNameList.add(jsonArray.getString(2*i+1));
                        }
                        //初始
                        nowCommend = CommendNoList.get(0);
                        ((Listener)getActivity()).saveCommend(nowCommend);
                        Log.e("PETER","CommendNo初始 : "+nowCommend);
                        setViewFlipper();
                    }
                    catch (Exception e)
                    {
                        Log.e("PETER", e.toString());
                    }
                }
                //失敗
                else
                {
                    Toast.makeText(getActivity(), String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setViewFlipper(){
        int i = 0;

        for(i=0;i<CommendNoList.size();i++){

            TextView tv1 = new TextView(getActivity());

            tv1.setText(CommendNameList.get(i));

            tv1.setTextColor(this.getResources().getColor(R.color.white));
            tv1.setTextSize(30);
            LinearLayout lq1 = new LinearLayout(getActivity());
            lq1.addView(tv1);
            viewFlipper.addView(lq1);

        }
    }
}
