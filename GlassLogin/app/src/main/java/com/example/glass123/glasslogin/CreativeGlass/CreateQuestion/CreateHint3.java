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
import java.io.InputStream;

/**
 * Created by seahorse on 2015/11/28.
 */
public class CreateHint3 extends AppCompatActivity implements View.OnClickListener {
    ImageButton createqnext_btn;
    String answer,hint1,hint2;

    ImageView hint3;
    private DisplayMetrics metrics;
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
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //init
        hint3 = (ImageView)findViewById(R.id.hint3);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
        startActivityForResult(intent, CAMERA);


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
        if((requestCode == CAMERA) && (data != null)){
            Uri uri = data.getData();
            ContentResolver resolver = this.getContentResolver();

            try{
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=4;
                Log.e("PETER",uri.getPath());
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

        super.onActivityResult(requestCode, resultCode, data);
    }

    //調整圖片大小
    private void scalepic(Bitmap bitmap,int metrics){
        float scale = 1;
        if(bitmap.getWidth()>metrics){
            scale = (float)metrics/(float)bitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.setScale(scale,scale);

            Bitmap scale_bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);

            hint3.setImageBitmap(scale_bitmap);
        }
        else{
            hint3.setImageBitmap(bitmap);
        }
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
