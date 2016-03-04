package com.example.glass123.glasslogin.CreativeGlass.CreateQuestion;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.glass123.glasslogin.R;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by seahorse on 2015/11/28.
 */
public class CreateHint3 extends Fragment implements View.OnClickListener{
    ImageButton createqnext_btn,camera_btn;
    String answer,hint1,hint2,imagepath;

    ImageView hint3;
    private DisplayMetrics metrics;
    private final static int CAMERA = 66 ;

    private double latitude=0.0,longitude=0.0;

    public interface Listener {
        public void saveImagepath(String im);
        public String getImagepath();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_create_hint3,container,false);

        camera_btn = (ImageButton)v.findViewById(R.id.camera_btn);
        camera_btn.setOnClickListener(this);

        //讀取手機解析度
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //init
        hint3 = (ImageView)v.findViewById(R.id.hint3);

        //取得imagepath，可能為路徑或空值
        imagepath = ((Listener)getActivity()).getImagepath();

        //有照片的路徑，顯示圖片
        if(!imagepath.equals(""))
        {
            Toast.makeText(getActivity().getApplicationContext(),"有東西，"+imagepath,Toast.LENGTH_SHORT).show();
            setPic();
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.camera_btn){
            OpenCamera();
        }
    }


    private void OpenCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((requestCode == CAMERA) && (data != null)) {
            Uri uri = data.getData();
            imagepath = getPath(uri);

            //拍照後儲存路徑
            ((Listener)getActivity()).saveImagepath(imagepath);

            //顯示圖片
            setPic();

            Log.e("PETER", "0");

//            Uri aa = Uri.parse("file://" + imagepath);
//            Toast.makeText(getActivity().getApplicationContext(), imagepath, Toast.LENGTH_LONG).show();
//            ContentResolver resolver = getActivity().getContentResolver();
//
//            try {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                Log.e("PETER", aa.toString());
//                Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(aa), null, options);
//
//                if (bitmap.getWidth() > bitmap.getHeight()) {
//                    scalepic(bitmap, metrics.heightPixels);
//                } else {
//                    scalepic(bitmap, metrics.widthPixels);
//                }
//
//            } catch (Exception e) {
//                Log.e("PETER", e.toString());
//
//            }
        }
    }

    private void setPic(){
        Uri uri = Uri.parse("file://" + imagepath);
        ContentResolver resolver = getActivity().getContentResolver();

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

            hint3.setImageBitmap(scale_bitmap);
        }
        else{
            hint3.setImageBitmap(bitmap);
        }
    }
    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_hint3);
//
//        createqnext_btn = (ImageButton)findViewById(R.id.createqnext_btn);
//        createqnext_btn.setOnClickListener(this);
//        camera_btn = (ImageButton)findViewById(R.id.camera_btn);
//        camera_btn.setOnClickListener(this);
//
//        Bundle bundle = this.getIntent().getExtras();
//        answer = bundle.getString("answer");
//        hint1 = bundle.getString("hint1");
//        hint2 = bundle.getString("hint2");
//        latitude = bundle.getDouble("lat");
//        longitude = bundle.getDouble("lon");
//
//        hint3 = (ImageView)findViewById(R.id.hint3);
//
//        //讀取手機解析度
//        metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        //init
//        hint3 = (ImageView)findViewById(R.id.hint3);
//
//        //首次進入自動開相機
//        OpenCamera();
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        if(view.getId() == R.id.createqnext_btn){
//            CreateQuestionNext();
//        }
//        else if(view.getId() == R.id.camera_btn){
//            OpenCamera();
//        }
//    }
//
//    private void OpenCamera(){
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//
//        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
//
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
//        startActivityForResult(intent, CAMERA);
//    }
//
//    //拍照完畢或選取圖片後呼叫此函式
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
//        if((requestCode == CAMERA) && (data != null)){
//            Uri uri = data.getData();
//            imagepath = getPath(uri);
//            Toast.makeText(CreateHint3.this,imagepath,Toast.LENGTH_LONG).show();
//            ContentResolver resolver = this.getContentResolver();
//
//            try{
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize=4;
//                Log.e("PETER",uri.toString());
//                InputStream inputStream = resolver.openInputStream(uri);
//                Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri),null,options);
//
//                if(bitmap.getWidth()>bitmap.getHeight()){
//                    scalepic(bitmap,metrics.heightPixels);
//                }
//                else{
//                    scalepic(bitmap,metrics.widthPixels);
//                }
//                inputStream.close();
//
//            }
//            catch (Exception e){
//                Log.e("PETER",e.toString());
//
//            }
//
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    //調整圖片大小
//    private void scalepic(Bitmap bitmap,int metrics){
//        float scale = 1;
//        if(bitmap.getWidth()>metrics){
//            scale = (float)metrics/(float)bitmap.getWidth();
//            Matrix matrix = new Matrix();
//            matrix.setScale(scale,scale);
//
//            Bitmap scale_bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
//
//            hint3.setImageBitmap(scale_bitmap);
//        }
//        else{
//            hint3.setImageBitmap(bitmap);
//        }
//    }
//
//    public String getPath(Uri uri) {
//
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
//
//    private void CreateQuestionNext(){
//        Intent intent = new Intent(CreateHint3.this,CreateQuestionSend.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("answer",answer);
//        bundle.putString("hint1",hint1);
//        bundle.putString("hint2",hint2);
//        bundle.putString("imagepath",imagepath);
//        bundle.putDouble("lat",latitude);
//        bundle.putDouble("lon",longitude);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
}
