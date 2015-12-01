package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.glass123.glasslogin.R;

import java.io.FileNotFoundException;

/**
 * Created by seahorse on 2015/11/28.
 */
public class CreateHint3 extends AppCompatActivity implements View.OnClickListener {
    ImageButton createqnext_btn;
    String answer,hint1,hint2;

    ImageView hint3;
    private DisplayMetrics mymetrics;
    private final static int CAMERA = 66 ;
    private final static int PHOTO = 99 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hint3);

        createqnext_btn = (ImageButton)findViewById(R.id.createqnext_btn);
        createqnext_btn.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        answer = bundle.getString("answer");
        hint1 = bundle.getString("hint1");
        hint2 = bundle.getString("hint2");

        hint3 = (ImageView)findViewById(R.id.hint3);

        //讀取手機解析度
        mymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mymetrics);

        //開啟相機
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri.getPath());
        startActivityForResult(intent,CAMERA);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.createqnext_btn){
            CreateQuestionNext();
        }
    }

    //拍照完畢或選取圖片後呼叫此函式
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        if ((requestCode == CAMERA || requestCode == PHOTO) && data != null) {
            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();

            try {
                //讀取照片，型態為Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                //判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
                if (bitmap.getWidth() > bitmap.getHeight()) ScalePic(bitmap,
                        mymetrics.heightPixels);
                else ScalePic(bitmap, mymetrics.widthPixels);

                bitmap.recycle();
            } catch (Exception e) {
                Log.e("PETER",e.toString());
            }
        }
    }

    private void ScalePic(Bitmap bitmap,int phone)
    {
        //縮放比例預設為1
        float mScale = 1 ;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if(bitmap.getWidth() > phone )
        {
            //判斷縮放比例
            mScale = (float)phone/(float)bitmap.getWidth();

            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,0, 0, bitmap.getWidth(),bitmap.getHeight(),mMat, false);
            hint3.setImageBitmap(mScaleBitmap);
        }
        else hint3.setImageBitmap(bitmap);
    }

    private void CreateQuestionNext(){
        Intent intent = new Intent(CreateHint3.this,CreateQuestionSend.class);
        Bundle bundle = new Bundle();
        bundle.putString("answer",answer);
        bundle.putString("hint1",hint1);
        bundle.putString("hint2",hint2);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
