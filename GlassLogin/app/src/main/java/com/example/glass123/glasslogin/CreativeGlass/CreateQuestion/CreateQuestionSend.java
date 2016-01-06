package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass123.glasslogin.R;

import java.io.FileNotFoundException;

/**
 * Created by seahorse on 2015/11/28.
 */
public class CreateQuestionSend extends Activity implements View.OnClickListener,LocationListener{
    TextView hint1_tv,hint2_tv;
    String hint1,hint2,imagepath,answer,titleId=null,ResponseMessages=null,msg;
    Button questionsend_btn;
    private Handler handler  = new Handler();
    private LocationManager mlocation ;
    private double latitude=0.0,longitude=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_send);

        hint1_tv = (TextView)findViewById(R.id.hint1_tv);
        hint2_tv = (TextView)findViewById(R.id.hint2_tv);
        questionsend_btn = (Button)findViewById(R.id.questionsend_btn);

        Bundle bundle = this.getIntent().getExtras();
        hint1 = bundle.getString("hint1");
        hint2 = bundle.getString("hint2");
        imagepath = bundle.getString("imagepath");
        answer = bundle.getString("answer");
        hint1_tv.setText(hint1);
        hint2_tv.setText(hint2);

        questionsend_btn.setOnClickListener(this);

        //取得定位
        mlocation  = (LocationManager)getSystemService(LOCATION_SERVICE);
        mlocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        mlocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

    }

    @Override
    public void onClick(View view) {

        final ProgressDialog dialog = ProgressDialog.show(CreateQuestionSend.this, "讀取中", "請等待數秒...", true);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true) {
                    if(latitude != 0.0 || longitude !=0.0)
                    {
                        dialog.dismiss();
                        GetServerMessage message = new GetServerMessage();
                        titleId = message.all("http://163.17.135.76/glass/add_title.php","UserId="+"20151211151346511431"+"&x="+ latitude+"&y="+longitude+"&floor="+1+"&titleDevice="+1);
                        handler.post(add_image);
                        Log.e("PETER", "UserId=" + "20151211151346511431" + "&x=" + latitude + "&y=" + longitude + "&floor=" + 6 + "&titleDevice=" + 1);
                        break;
                    }
                }

            }

        }).start();
    }

    //執行緒
    final Runnable add_image = new Runnable()
    {
        @Override
        public void run()
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    UploadImage uploadImage = new UploadImage();
                    ResponseMessages = uploadImage.uploadFile(imagepath);
                    handler.post(add_prompt);
                }

            }).start();
        }
    };

    final Runnable add_prompt = new Runnable()
    {
        @Override
        public void run()
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (!ResponseMessages.equals("fail"))
                    {
                        GetServerMessage message = new GetServerMessage();
                        msg = message.all("http://163.17.135.76/glass/add_prompt.php","titleId="+titleId.trim()+"&p1="+ hint1+"&p2="+hint2+"&p3=three&imagepath="+ResponseMessages+"&ans="+answer);
                        Log.e("PETER","@!#");
                        finish();
                    }
                    else
                    {

                    }

                }
            }).start();
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
