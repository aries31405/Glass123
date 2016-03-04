package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass123.glasslogin.CreativeGlass.CreativeGlassStart;
import com.example.glass123.glasslogin.MapsActivity;
import com.example.glass123.glasslogin.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by seahorse on 2015/11/28.
 */
public class CreateQuestionSend extends Activity implements View.OnClickListener{
    ProgressDialog dialog;
    TextView hint1_tv,hint2_tv;
    String hint1,hint2,imagepath,answer,floor,device,titleId=null,ResponseMessages=null,msg;
    Button questionsend_btn;
    ImageView hint3_img;
    private DisplayMetrics metrics;
    private Handler handler  = new Handler();
    private double latitude=0.0,longitude=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_send);

        hint1_tv = (TextView)findViewById(R.id.hint1_tv);
        hint2_tv = (TextView)findViewById(R.id.hint2_tv);
        questionsend_btn = (Button)findViewById(R.id.questionsend_btn);
        hint3_img = (ImageView)findViewById(R.id.hint3_img);

        //接bundle
        Bundle bundle = this.getIntent().getExtras();
        hint1 = bundle.getString("hint1");
        hint2 = bundle.getString("hint2");
        imagepath = bundle.getString("imagepath");
        answer = bundle.getString("answer");
        latitude = bundle.getDouble("lat");
        longitude = bundle.getDouble("lon");
        floor = "1";//---------------------------------------------------還要拿
        device = "1";//手機固定1


        hint1_tv.setText(hint1);
        hint2_tv.setText(hint2);

        Log.e("PETER", String.valueOf(latitude) + " , " + String.valueOf(longitude));

        //讀取手機解析度
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        setPic();

        questionsend_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


        dialog = ProgressDialog.show(CreateQuestionSend.this, "讀取中", "請等待數秒...", true);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true) {
                    if(latitude != 0.0 || longitude !=0.0)
                    {
                        UploadImage uploadImage = new UploadImage();
                        ResponseMessages = uploadImage.uploadFile(imagepath);
                        handler.post(add_info);
                        break;
                    }
                }

            }

        }).start();
    }

    public void set_image(View v)
    {
        // 建立 "選擇檔案 Action" 的 Intent
        Intent intent = new Intent( Intent.ACTION_PICK );

        // 過濾檔案格式
        intent.setType( "image/*" );

        // 建立 "檔案選擇器" 的 Intent  (第二個參數: 選擇器的標題)
        Intent destIntent = Intent.createChooser( intent, "選擇檔案" );

        // 切換到檔案選擇器 (它的處理結果, 會觸發 onActivityResult 事件)
        startActivityForResult( destIntent, 0 );
    }

    private void setPic() {
        Uri uri = Uri.parse("file://" + imagepath);
        ContentResolver resolver = this.getContentResolver();

        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=4;
            Log.e("PETER",uri.toString());
            InputStream inputStream = resolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri),null,options);

            if(bitmap.getWidth()>bitmap.getHeight()){
                scalepic(bitmap,metrics.heightPixels);
            }
            else{
                scalepic(bitmap,metrics.widthPixels);
            }
            inputStream.close();

        }
        catch (Exception e){
            Log.e("PETER",e.toString());

        }
    }

    //調整圖片大小
    private void scalepic(Bitmap bitmap,int metrics){
        float scale = 1;
        if(bitmap.getWidth()>metrics){
            scale = (float)metrics/(float)bitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.setScale(scale,scale);

            Bitmap scale_bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);

            hint3_img.setImageBitmap(scale_bitmap);
        }
        else{
            hint3_img.setImageBitmap(bitmap);
        }
    }

    //執行緒
    final Runnable add_info = new Runnable()
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
                        msg = message.all("http://163.17.135.76/new_glass/add_info.php","userId="+"1"+"&p1="+ hint1+"&p2="+hint2+"&p3="+ResponseMessages+"&ans="+answer+"&x="+latitude+"&y="+longitude+"&floor="+floor+"&titleDevice="+device);
                        Log.e("PETER", "@!#");
                        handler.post(askcontinue);
                    }
                    else
                    {

                    }
                    Log.e("Image",  ResponseMessages);
                }
            }).start();
        }
    };

    final Runnable askcontinue = new Runnable()
    {
        @Override
        public void run()
        {
            dialog.dismiss();
            new AlertDialog.Builder(CreateQuestionSend.this)
                    .setTitle("繼續出題")
                    .setMessage("要繼續出題嗎?")
                    .setPositiveButton("繼續", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CreateQuestionSend.this, MapsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("不繼續", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CreateQuestionSend.this, CreativeGlassStart.class);
                            startActivity(intent);
                            finish();
                        }
                    }).show();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // 有選擇檔案
        if ( resultCode == RESULT_OK )
        {
            // 取得檔案的 Uri
            Uri uri = data.getData();
            if( uri != null )
            {
                Toast.makeText(CreateQuestionSend.this,getPath(uri),Toast.LENGTH_LONG).show();
                imagepath = getPath(uri);
                setPic();
            }
            else
            {
                Toast.makeText(CreateQuestionSend.this,"無效的檔案路徑 !!",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(CreateQuestionSend.this,"取消選擇檔案 !!",Toast.LENGTH_LONG).show();
        }
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
