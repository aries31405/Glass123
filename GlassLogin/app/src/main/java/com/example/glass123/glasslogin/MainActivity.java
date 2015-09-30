package com.example.glass123.glasslogin;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.glass123.glasslogin.Bluetooth.BluetoothChatFragment;
import com.example.glass123.glasslogin.Bluetooth.Constants;
import com.example.glass123.glasslogin.Bluetooth.Profile;
import com.example.glass123.glasslogin.Mplayer.Player;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "android-plus-quickstart";

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    //連線狀態
    private TextView mStatus;

    //按鈕
    private SignInButton mSignInBtn;
    //private Button mSignOutBtn;

    // 大頭貼
    private CircleImageView mPhoto;

    //使用者資料
    private Profile mProfile = new Profile();

    Player player;

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.sign_in_button) {
            onSignInClicked();
        }
        /*
        else if(v.getId() == R.id.login_glass){
            onGlassLoginClick();
        }
        */


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 建立 GoogleApiClient以存取基本資料
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        // 按鈕 findViewById
        mStatus = (TextView) findViewById(R.id.textView);
        mSignInBtn = (SignInButton) findViewById(R.id.sign_in_button);

        // Google大頭貼 findViewById
        mPhoto = (CircleImageView) findViewById(R.id.profile_image);

        // 設定所有按鈕監聽
        mSignInBtn.setOnClickListener(this);

        player = new Player("http://163.17.135.75/TTS/Lelogin/googlelogin.mp3");
        player.play();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        mStatus.setText(R.string.signing_in);
//        Toast.makeText(this, "onSignInClicked", Toast.LENGTH_SHORT).show();

    }

    // 按下登出按鈕後，執行登出
    private void onSignOutClick(){
        mSignInBtn.setEnabled(true);
        //mSignOutBtn.setEnabled(false);
        if(mGoogleApiClient.isConnected()){
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
//            Toast.makeText(this, "onSignOutClick", Toast.LENGTH_SHORT).show();
        }

        // 清空使用者資料
        mProfile.USER_NAME = "";
        mProfile.USER_EMAIL = "";
        mProfile.USER_IMAGE = "";
    }

    // 按下登入Glass按鈕後，跳轉到與Glass連結的BluetoothChatFragment
    private void onGlassLoginClick(){
        if(player.pause())
        {

        }

        Intent it = new Intent(MainActivity.this, MyProfile.class);

        Bundle bundle = new Bundle();
        bundle.putString("username", mProfile.USER_NAME);
        bundle.putString("useremail", mProfile.USER_EMAIL);
        bundle.putString("userimage", mProfile.USER_IMAGE);

        onSignOutClick();

        it.putExtras(bundle);
        startActivity(it);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
//        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.

        mSignInBtn.setEnabled(false);
        //mSignOutBtn.setEnabled(true);
        //Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        // Show the signed-in UI
        //showSignedInUI();

        //mStatus.setText(String.format("Sign in as: %s", currentUser.getDisplayName() + ", " + Plus.AccountApi.getAccountName(mGoogleApiClient)));

        mProfile.USER_NAME = currentUser.getDisplayName();
        mProfile.USER_EMAIL = Plus.AccountApi.getAccountName(mGoogleApiClient);
        mProfile.USER_IMAGE = currentUser.getImage().getUrl();
        mProfile.USER_IMAGE = mProfile.USER_IMAGE.split("=")[0]+"="+300;
        Log.e(TAG, "TTT" + mProfile.USER_IMAGE);

//        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        onGlassLoginClick();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                //showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            //showSignedOutUI();
        }
//        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
//        Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
