package com.example.glass123.glasslogin.CreativeGlass.AnswerQuestion;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.glass123.glasslogin.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindMap extends FragmentActivity implements OnMapReadyCallback,LocationListener,View.OnTouchListener {

    private GoogleMap mMap;

    LocationManager mlocation;

    String choose;

    private double latitude=0.0,longitude=0.0;

    int radius = 0;

    public SeekBar sb;
    private ViewFlipper findfloor_viewflipper;
    private float touchDownX;
    private float touchUpX;
    private float touchDownY;
    private float touchUpY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_map);

        findfloor_viewflipper = (ViewFlipper)findViewById(R.id.findfloor_viewflipper);
        int i = 0;

        for(i=0;i<11;i++){

            TextView tv1 = new TextView(this);

            if(i==0)
            {
                tv1.setText("戶外");
            }
            else
            {
                tv1.setText(String.valueOf(i)+"樓");
            }

            tv1.setTextColor(this.getResources().getColor(R.color.new_purple));
            tv1.setTextSize(50);

            LinearLayout lq1 = new LinearLayout(this);
            lq1.addView(tv1);
            findfloor_viewflipper.addView(lq1);

        }

        findfloor_viewflipper.setOnTouchListener(this);
        //抓樓層
//        findfloor_viewflipper.getDisplayedChild();

        //設定向使用者連接的手持裝置取得位置
        mlocation  = (LocationManager)getSystemService(LOCATION_SERVICE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        /*new AlertDialog.Builder(FindMap.this)
                .setTitle("提示")
                .setMessage("室內or室外")
                .setPositiveButton("內", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choose = "Network";
                        mlocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0,FindMap.this);
                        mapFragment.getMapAsync(FindMap.this);
                    }
                })
                .setNegativeButton("外", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choose = "GPS";
                        mlocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, FindMap.this);
                        mapFragment.getMapAsync(FindMap.this);
                    }
                }).show();*/

        //暫時刪去GPS用法 ^^^^^^^^^^^^^^^^^^^^^^^^^
        choose = "Network";
        mlocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0,FindMap.this);
        mapFragment.getMapAsync(FindMap.this);



        sb = (SeekBar)findViewById(R.id.seekBar);


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                //拉動SeekBar停止時做的動作

                if(latitude != 0.0)
                {
                    LatLng place = new LatLng(latitude, longitude);

                    mMap.clear();

                    addMarker(place, "您所選取室內中心位置", latitude + " : " + longitude);

                    if(radius!=0) {
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(latitude, longitude))
                                .radius(radius)
                                .strokeColor(getResources().getColor(R.color.transparent_blue))
                                .fillColor(getResources().getColor(R.color.transparent_blue)));}

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                //開始拉動SeekBar時做的動作
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                //SeekBar改變時做的動作

                radius = progress;

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(choose.equals("Network"))
        {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {

                    latitude = latLng.latitude;
                    longitude = latLng.longitude;

                    LatLng place = new LatLng(latLng.latitude, latLng.longitude);

                    mMap.clear();

                    addMarker(place, "您所選取室內中心位置", latLng.latitude + " : " + latLng.longitude);

                    if(radius!=0) {
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(latitude, longitude))
                                .radius(radius)
                                .strokeColor(getResources().getColor(R.color.transparent_blue))
                                .fillColor(getResources().getColor(R.color.transparent_blue)));}

                }
            });
        }

    }


    public void ok(View v)
    {
        Bundle bundle = new Bundle();
        bundle.putString("choose", choose);
        if(choose.equals("Network")){
            bundle.putInt("radius",radius);
            bundle.putDouble("latitude", latitude);
            bundle.putDouble("longitude",longitude);
        }
        Intent intent = new Intent(FindMap.this,FindQuestion.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

        // 移動地圖到參數指定的位置
    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(19)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addMarker(LatLng place, String title, String snippet) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .title(title)
                .snippet(snippet);

        mMap.addMarker(markerOptions);
    }


    @Override
    public void onLocationChanged(Location location) {

        LatLng place = new LatLng(location.getLatitude(), location.getLongitude());

        if(choose.equals("GPS")){addMarker(place, "您現在所定位位置", location.getLatitude() + " : " + location.getLongitude());}

        moveMap(place);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //在此返回上一頁視同放棄作答
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            int EXIT_CODE=1;
            setResult(EXIT_CODE);
        }
        return super.onKeyDown(keyCode, event);
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
            if(touchUpX-touchDownX > 100){
                findfloor_viewflipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in_b));
                findfloor_viewflipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out_b));

                findfloor_viewflipper.showPrevious();
            } else if (touchDownX - touchUpX > 100){
                findfloor_viewflipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_in_b));
                findfloor_viewflipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_out_b));

                findfloor_viewflipper.showNext();
            }
            return true;
        }
        return false;
    }
}
