package com.example.glass123.glasslogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.PointF;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.glass123.glasslogin.Bluetooth.Profile;
import com.example.glass123.glasslogin.CreativeGlass.CreativeGlassStart;
import com.example.glass123.glasslogin.CreativeGlass.WithGlass.WithGlassStart;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kyleduo.switchbutton.SwitchButton;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    private Profile mProfile = new Profile();

    //SwitchButton
    SwitchButton withglass_sb;
    boolean mThumbSizeFlag;
    boolean mThumbRadiusFlag;
    boolean mBackRadiusFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //設定登入時要求userID, email address, 基本資料，皆包含於DEFAULT_SIGN_IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //建立一個GoogleApiClient去存取Google Sign-In API 並且將設定gso帶入
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);

        //SwitchButton init
        withglass_sb = (SwitchButton)findViewById(R.id.withglass_sb);
        ChangeSwitchButtonStyle();
        withglass_sb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                withglass_sb.setBackColorRes(isChecked ? R.color.purple : R.color.white);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            signIn();
        }
//        else if(v.getId() == R.id.skip_btn){
//            onSkipClick();
//        }
//        else if(v.getId() == R.id.login_glass){
//            onGlassLoginClick();
//        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone())
        {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else
        {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result 從GoogleSignInApi.getSignInIntent(mGoogleApiClient)回傳回來
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess())
        {
            //登入成功

            //取得帳戶資料
            GoogleSignInAccount acct = result.getSignInAccount();
            getAccountProfile(acct);

            //跳到MyProfile Activity
            toMyProfile();

            //自動登出
            signOut();
        }
        else {
            //登入失敗
            Toast.makeText(getApplicationContext(),"登入失敗",Toast.LENGTH_SHORT).show();
        }
    }

    private void getAccountProfile(GoogleSignInAccount acct){
        mProfile.USER_NAME = acct.getDisplayName();
        mProfile.USER_EMAIL = acct.getEmail();
        try{
            mProfile.USER_IMAGE = acct.getPhotoUrl().toString().split("=")[0]+"="+300;
            Log.e(TAG, "TTT" + mProfile.USER_IMAGE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void toMyProfile(){
        Intent it = new Intent(MainActivity.this, MyProfile.class);

        Bundle bundle = new Bundle();
        bundle.putString("username", mProfile.USER_NAME);
        bundle.putString("useremail", mProfile.USER_EMAIL);
        bundle.putString("userimage", mProfile.USER_IMAGE);
        bundle.putString("device", PhoneOrGlass());

        it.putExtras(bundle);
        startActivity(it);
    }

    private String PhoneOrGlass(){
        String device="";
        return device;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        clearAccountProfile();
                    }
                });
    }

    private void clearAccountProfile(){
        // 清空使用者資料
        mProfile.USER_NAME = "";
        mProfile.USER_EMAIL = "";
        mProfile.USER_IMAGE = "";
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("onConnectionFailed:", "Result:" + connectionResult);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //在此返回上一頁視同放棄作答
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            closeapp();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeapp(){
        new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setTitle("退出")
                .setMessage("要否退出創意戴鏡")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    //調整SwitchButton大小及形狀
    private void ChangeSwitchButtonStyle(){

        //背景顏色
        withglass_sb.setBackColorRes(R.color.white);

        //大小
        float size = 30 * getResources().getDisplayMetrics().density;
        withglass_sb.setThumbSize(mThumbSizeFlag ? null : new PointF(size, size));
        mThumbSizeFlag = !mThumbSizeFlag;

        //形狀
        float r = 2 * getResources().getDisplayMetrics().density;
        withglass_sb.setThumbRadius(mThumbRadiusFlag ? Math.min(withglass_sb.getThumbSizeF().x, withglass_sb.getThumbSizeF().y) / 2f : r);
        mThumbRadiusFlag = !mThumbRadiusFlag;

        float r1 = 2 * getResources().getDisplayMetrics().density;
        withglass_sb.setBackRadius(mBackRadiusFlag ? Math.min(withglass_sb.getBackSizeF().x, withglass_sb.getBackSizeF().y) / 2f : r1);
        mBackRadiusFlag = !mBackRadiusFlag;

        float r2 = 2 * getResources().getDisplayMetrics().density;
        withglass_sb.setThumbRadius(mThumbRadiusFlag ? Math.min(withglass_sb.getThumbSizeF().x, withglass_sb.getThumbSizeF().y) / 2f : r2);
        mThumbRadiusFlag = !mThumbRadiusFlag;

        float r3 = 2 * getResources().getDisplayMetrics().density;
        withglass_sb.setBackRadius(mBackRadiusFlag ? Math.min(withglass_sb.getBackSizeF().x, withglass_sb.getBackSizeF().y) / 2f : r3);
        mBackRadiusFlag = !mBackRadiusFlag;

    }

}
//public class MainActivity extends Activity implements
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        View.OnClickListener,
//        TextToSpeech.OnInitListener{
//
//    private static final int MY_DATA_CHECK_CODE = 1;
//    private static final int RC_SIGN_IN = 0;
//    private GoogleApiClient mGoogleApiClient;
//    private static final String TAG = "android-plus-quickstart";
//
//
//    /* Is there a ConnectionResult resolution in progress? */
//    private boolean mIsResolving = false;
//
//    /* Should we automatically resolve ConnectionResults when possible? */
//    private boolean mShouldResolve = false;
//
//    //連線狀態
//    private TextView mStatus;
//
//    //按鈕
//    private SignInButton mSignInBtn;
//    private Button skip_btn;
//    //private Button mSignOutBtn;
//
//    // 大頭貼
//    private CircleImageView mPhoto;
//
//    //使用者資料
//    private Profile mProfile = new Profile();
//
//    private TextToSpeech tts;
//
//    //SwitchButton
//    SwitchButton withglass_sb;
//    boolean mThumbSizeFlag;
//    boolean mThumbRadiusFlag;
//    boolean mBackRadiusFlag;
//
//    @Override
//    public void onClick(View v){
//        if (v.getId() == R.id.sign_in_button) {
//            onSignInClicked();
//        }
////        else if(v.getId() == R.id.skip_btn){
////            onSkipClick();
////        }
//        /*
//        else if(v.getId() == R.id.login_glass){
//            onGlassLoginClick();
//        }
//        */
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // 建立 GoogleApiClient以存取基本資料
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API)
//                .addScope(Plus.SCOPE_PLUS_LOGIN)
//                .build();
//
//        // 按鈕 findViewById
//        mSignInBtn = (SignInButton) findViewById(R.id.sign_in_button);
////        skip_btn = (Button)findViewById(R.id.skip_btn);
//
//
//        // 設定所有按鈕監聽
//        mSignInBtn.setOnClickListener(this);
////        skip_btn.setOnClickListener(this);
//
//        Intent checkIntent = new Intent();
//        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
//
////        double i = 1;
////        double n=0;
////        for(i=1;i<=3;i++)
////        {
////            n+=(Math.pow(-1,(i+1)))*(1/i);
////        }
////        Toast.makeText(this,String.valueOf(n),Toast.LENGTH_SHORT).show();
//
//        //SwitchButton init
////        withglass_sb = (SwitchButton)findViewById(R.id.withglass_sb);
////        ChangeSwitchButtonStyle();
////        withglass_sb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
////            @Override
////            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
////                withglass_sb.setBackColorRes(isChecked ? R.color.purple : R.color.white);
////            }
////        });
//
//    }
//
//    @Override
//    protected void onStart(){
//        super.onStart();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    protected void onStop(){
//        super.onStop();
//        mGoogleApiClient.disconnect();
//    }
//
//
//    private void onSignInClicked() {
//        // User clicked the sign-in button, so begin the sign-in process and automatically
//        // attempt to resolve any errors that occur.
//        mShouldResolve = true;
//        mGoogleApiClient.connect();
//
//        // Show a message to the user that we are signing in.
//
//    }
//
//    // 按下登出按鈕後，執行登出
//    private void onSignOutClick(){
//        mSignInBtn.setEnabled(true);
//        //mSignOutBtn.setEnabled(false);
//        if(mGoogleApiClient.isConnected()){
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
////            Toast.makeText(this, "onSignOutClick", Toast.LENGTH_SHORT).show();
//        }
//
//        // 清空使用者資料
//        mProfile.USER_NAME = "";
//        mProfile.USER_EMAIL = "";
//        mProfile.USER_IMAGE = "";
//    }
//
//    // 按下登入Glass按鈕後，跳轉到與Glass連結的BluetoothChatFragment
//    private void onGlassLoginClick(){
//        Intent it = new Intent(MainActivity.this, MyProfile.class);
//
//        Bundle bundle = new Bundle();
//        bundle.putString("username", mProfile.USER_NAME);
//        bundle.putString("useremail", mProfile.USER_EMAIL);
//        bundle.putString("userimage", mProfile.USER_IMAGE);
//
//        onSignOutClick();
//
//        it.putExtras(bundle);
//        startActivity(it);
//    }
//
//    private void onSkipClick(){
//
//       if(withglass_sb.isChecked()){
//            Intent it = new Intent(MainActivity.this, GlassMap.class);
//            startActivity(it);
//        }
//        else if(!withglass_sb.isChecked()){
//            Intent it = new Intent(MainActivity.this, CreativeGlassStart.class);
//            startActivity(it);
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        //在此返回上一頁視同放棄作答
//        if(keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            closeapp();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private void closeapp(){
//        new AlertDialog.Builder(MainActivity.this)
//                .setCancelable(false)
//                .setTitle("退出")
//                .setMessage("要否退出創意戴鏡")
//                .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        MainActivity.this.finish();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
//    }
//
//    @Override
//    public void onConnectionSuspended(int cause) {
//        // The connection to Google Play services was lost for some reason.
//        // We call connect() to attempt to re-establish the connection or get a
//        // ConnectionResult that we can attempt to resolve.
//        mGoogleApiClient.connect();
////        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        // onConnected indicates that an account was selected on the device, that the selected
//        // account has granted any requested permissions to our app and that we were able to
//        // establish a service connection to Google Play services.
//
//        mSignInBtn.setEnabled(false);
//        //mSignOutBtn.setEnabled(true);
//        //Log.d(TAG, "onConnected:" + bundle);
//        mShouldResolve = false;
//
//        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//
//        // Show the signed-in UI
//
//        mProfile.USER_NAME = currentUser.getDisplayName();
//        mProfile.USER_EMAIL = Plus.AccountApi.getAccountName(mGoogleApiClient);
//        mProfile.USER_IMAGE = currentUser.getImage().getUrl();
//        mProfile.USER_IMAGE = mProfile.USER_IMAGE.split("=")[0]+"="+300;
//        Log.e(TAG, "TTT" + mProfile.USER_IMAGE);
//
////        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
//        onGlassLoginClick();
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        // Could not connect to Google Play Services.  The user needs to select an account,
//        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
//        // ConnectionResult to see possible error codes.
//        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
//
//        if (!mIsResolving && mShouldResolve) {
//            if (connectionResult.hasResolution()) {
//                try {
//                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
//                    mIsResolving = true;
//                } catch (IntentSender.SendIntentException e) {
//                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
//                    mIsResolving = false;
//                    mGoogleApiClient.connect();
//                }
//            } else {
//                // Could not resolve the connection result, show the user an
//                // error dialog.
//
//            }
//        } else {
//            // Show the signed-out UI
//
//        }
////        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
//
//        switch (requestCode) {
//            case RC_SIGN_IN:
//                // If the error resolution was not successful we should not resolve further.
//                if (resultCode != RESULT_OK) {
//                    mShouldResolve = false;
//                }
//
//                mIsResolving = false;
//                mGoogleApiClient.connect();
//                break;
//            case MY_DATA_CHECK_CODE:
//                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
//                    // success, create the TTS instance
//                    tts = new TextToSpeech(this, this);
//
//                }
//                else {
//                    // missing data, install it
//                    Intent installIntent = new Intent();
//                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
//                    startActivity(installIntent);
//                }
//                break;
//        }
//
//    }
//
//    /*
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//    */
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onInit(int status) {
//        if (status == TextToSpeech.SUCCESS) {
//            Toast.makeText(MainActivity.this,
//                    "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
//            tts.speak("請登入Google Plus，點擊Sign in 按鈕，選擇欲登入創意戴鏡的Google帳戶。", TextToSpeech.QUEUE_ADD, null);
//        }
//        else if (status == TextToSpeech.ERROR) {
//            Toast.makeText(MainActivity.this,
//                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    //調整SwitchButton大小及形狀
//    private void ChangeSwitchButtonStyle(){
//
//        //背景顏色
//        withglass_sb.setBackColorRes(R.color.white);
//
//        //大小
//        float size = 30 * getResources().getDisplayMetrics().density;
//        withglass_sb.setThumbSize(mThumbSizeFlag ? null : new PointF(size, size));
//        mThumbSizeFlag = !mThumbSizeFlag;
//
//        //形狀
//        float r = 2 * getResources().getDisplayMetrics().density;
//        withglass_sb.setThumbRadius(mThumbRadiusFlag ? Math.min(withglass_sb.getThumbSizeF().x, withglass_sb.getThumbSizeF().y) / 2f : r);
//        mThumbRadiusFlag = !mThumbRadiusFlag;
//
//        float r1 = 2 * getResources().getDisplayMetrics().density;
//        withglass_sb.setBackRadius(mBackRadiusFlag ? Math.min(withglass_sb.getBackSizeF().x, withglass_sb.getBackSizeF().y) / 2f : r1);
//        mBackRadiusFlag = !mBackRadiusFlag;
//
//        float r2 = 2 * getResources().getDisplayMetrics().density;
//        withglass_sb.setThumbRadius(mThumbRadiusFlag ? Math.min(withglass_sb.getThumbSizeF().x, withglass_sb.getThumbSizeF().y) / 2f : r2);
//        mThumbRadiusFlag = !mThumbRadiusFlag;
//
//        float r3 = 2 * getResources().getDisplayMetrics().density;
//        withglass_sb.setBackRadius(mBackRadiusFlag ? Math.min(withglass_sb.getBackSizeF().x, withglass_sb.getBackSizeF().y) / 2f : r3);
//        mBackRadiusFlag = !mBackRadiusFlag;
//
//    }
//}
