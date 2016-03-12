package com.example.glass123.glasslogin;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateQuestion;
import com.example.glass123.glasslogin.CreativeGlass.CreateQuestion.CreateQuestionAnswer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,View.OnTouchListener {

    private GoogleMap mMap;

    // Google API用戶端物件
    private GoogleApiClient googleApiClient;

    // Location請求物件
    private LocationRequest locationRequest;

    ViewFlipper floor_viewflipper;
    private float touchDownX;
    private float touchUpX;
    private float touchDownY;
    private float touchUpY;

    Button setfloor_btn;

    private double latitude=0.0,longitude=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // 建立Google API用戶端物件
        configGoogleApiClient();

        // 建立Location請求物件
        configLocationRequest();

        googleApiClient.connect();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //--------------------------------------------------
        floor_viewflipper = (ViewFlipper)findViewById(R.id.floor_viewflipper);

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

//            tv1.setGravity(0x11);
//            tv1.setWidth(150);
            LinearLayout lq1 = new LinearLayout(this);
            lq1.addView(tv1);
            floor_viewflipper.addView(lq1);

        }

        floor_viewflipper.setOnTouchListener(this);

        setfloor_btn = (Button)findViewById(R.id.setfloor_btn);

        setfloor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, CreateQuestion.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat",latitude);
                bundle.putDouble("lon",longitude);
                intent.putExtras(bundle);
                startActivity(intent);
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

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                mMap.clear();
                LatLng place = new LatLng(latLng.latitude, latLng.longitude);

                addMarker(place, "您所選取位置", latLng.latitude + " : " + latLng.longitude);

                moveMap(place);
            }
        });
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


    // 建立Google API用戶端物件
    private synchronized void configGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // 建立Location請求物件
    private void configLocationRequest() {
        locationRequest = new LocationRequest();
        // 設定讀取位置資訊的間隔時間為一秒（1000ms）
        locationRequest.setInterval(1000);
        // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
        locationRequest.setFastestInterval(1000);
        // 設定優先讀取高精確度的位置資訊（GPS）
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // 已經連線到Google Services
        // 啟動位置更新服務
        // 位置資訊更新的時候，應用程式會自動呼叫LocationListener.onLocationChanged
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MapsActivity.this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Google Services連線中斷
        // int參數是連線中斷的代號
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Services連線失敗
        // ConnectionResult參數是連線失敗的資訊
        int errorCode = connectionResult.getErrorCode();

        // 裝置沒有安裝Google Play服務
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(this, "裝置沒有安裝Google Play服務", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        // 位置改變
        LatLng place = new LatLng(location.getLatitude(), location.getLongitude());

        addMarker(place, "您現在所在位置", place.latitude + " : " + place.longitude);
        // 移動地圖到目前的位置
        moveMap(place);

        googleApiClient.disconnect();

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            touchDownX = event.getX();
            touchDownY = event.getY();
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            touchUpX=event.getX();
            touchUpY=event.getY();
            if(touchUpX-touchDownX > 100){
                floor_viewflipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in_b));
                floor_viewflipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out_b));

                floor_viewflipper.showPrevious();
            }
            else if(touchDownX-touchUpX > 100){
                floor_viewflipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_in_b));
                floor_viewflipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_out_b));

                floor_viewflipper.showNext();
            }
            return true;
        }
        return false;
    }
}
