package com.example.gameking_var2.remoteproject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.gameking_var2.remoteproject.Bluetooth.BluetoothChatService;
import com.example.gameking_var2.remoteproject.Bluetooth.Constants;
import com.example.gameking_var2.remoteproject.CardsAdapter.CardAdapter;
import com.example.gameking_var2.remoteproject.Login.Login;
import com.example.gameking_var2.remoteproject.MainLine.MainLine;
import com.example.gameking_var2.remoteproject.Profile.Profile;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

/*
遊玩前頁面
功能：
１．連結手機
２．登入google帳號
３．顯示登入後帳戶資訊
４．選擇性別及年齡
５．完成設定，進入主畫面
－－－－－－－－－
連結至MainLine頁面
*/

public class StartCard extends Activity
{
    //不知道
    private static final String TAG = StartCard.class.getSimpleName();

    //定義卡片順序 方便了解
    static final int Connection = 0;
    static final int Login = 1;
    static final int Profile = 2;
    static final int Sex = 3;
    static final int Age = 4;
    static final int Success = 5;

    //上滑動佈景 下是滑動卡片
    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    //現在的卡片  用來判斷開啟哪個選單
    private int nowCard = -1;

    private View mView;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    //基本資料
    Profile mProfile = new Profile();

    //List的卡片創建
    ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();

    //
    private int USER;
    private int NOT_USER = 0;
    private int ALREADY_USER = 1;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        cards.clear();
        //將卡片類別 傳回來  並用自定義類別"CardAdapter"（覆寫卡片類別）
        mAdapter = new CardAdapter(createCards(this,Connection));

        //預設 抓本體
        mCardScroller = new CardScrollView(this);

        //將本體設定為用好的自定義類別
        mCardScroller.setAdapter(mAdapter);

        //設定場景
        setContentView(mCardScroller);

        //設定卡片點擊事件
        setCardScrollerListener();

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            //FragmentActivity activity = getActivity();
            //Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();

            this.finish();
        }
    }

    //建立滑動卡片 使用List
    private List<CardBuilder> createCards(Context context,int position)
    {
        /*
        //List的卡片創建
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
*/
        switch(position){
            case Connection:
                if(cards.size() == Connection){
                    cards.add
                            (
                                    Connection, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.con_r)
                            );
                }
                break;
            case Login:
                if(cards.size() == Login){
                    cards.add
                            (
                                    Login, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.signin)
                            );
                }
                break;
            case Profile:
                if(cards.size() == Profile){
                    cards.add
                            (
                                    Profile, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.profile)
                            );
                }
                break;
            case Sex:
                if(cards.size() == Sex) {
                    cards.add
                            (
                                    Sex, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.sex)
                            );
                }
                break;
            case Age:
                if(cards.size() == Age) {
                    cards.add
                            (
                                    Age, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.age)
                            );
                }
                break;
            case Success:
                if(cards.size() == Success) {
                    cards.add
                            (
                                    Success, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.success)
                            );
                }
                if((USER == ALREADY_USER) && (cards.size() == 3)){
                    cards.add
                            (
                                    Sex, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.success)
                            );
                }
                break;
        }
        /*
        //逐一建造
        cards.add
        (
            Connection, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.con_r)
        );
        cards.add
        (
            Login, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.signin)
        );
        cards.add
        (
            Profile, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.profile)
        );
        cards.add
                (
                        Sex, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.sex)
                );
        cards.add
                (
                        Age, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.age)
                );
        cards.add
                (
                        Success, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.success)
                );
                */
        return cards;
    }

    //設定卡片點擊監聽
    private void setCardScrollerListener()
    {
        //卡片的View 設定監聽
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //不知道
                Log.d(TAG, "Clicked view at position " + position + ", row-id " + id);
                int soundEffect = Sounds.TAP;

                //判斷點擊哪個卡片
                switch (position) {
                    case Connection:
                        nowCard = Connection;
/*
                        mAdapter = new CardAdapter(createCards(StartCard.this,Login));
                        mCardScroller.animate(Login, CardScrollView.Animation.INSERTION);
*/
                        break;

                    case Login:
                        nowCard = Login;

                        mAdapter = new CardAdapter(createCards(StartCard.this,Profile));
                        //mCardScroller.animate(Login, C ardScrollView.Animation.DELETION);
                        mCardScroller.animate(Profile, CardScrollView.Animation.NAVIGATION);

                        Toast.makeText(StartCard.this, cards.size() + "!", Toast.LENGTH_SHORT).show();
                        connDb0();
                        //登入
                        break;

                    case Profile:
                        nowCard = Profile;
                        mAdapter = new CardAdapter(createCards(StartCard.this, Sex));
                        mCardScroller.animate(Sex, CardScrollView.Animation.NAVIGATION);
                        break;

                    case Sex:
                        if(USER == ALREADY_USER){
                            connDb();
                        }
                        else{
                            nowCard = Sex;
                            mAdapter = new CardAdapter(createCards(StartCard.this, Age));
                            mCardScroller.animate(Age, CardScrollView.Animation.NAVIGATION);
                            openOptionsMenu();
                        }

                        break;

                    case Age:
                        nowCard = Age;
                        mAdapter = new CardAdapter(createCards(StartCard.this, Success));
                        mCardScroller.animate(Success, CardScrollView.Animation.NAVIGATION);
                        openOptionsMenu();
                        break;

                    case Success:
                        nowCard = Success;

                        /*
                        //有名字及信箱的資料後，POST出去
                        if((!mProfile.USER_EMAIL.equals("")) && (!mProfile.USER_NAME.equals(""))){
                            connDb();
                            Toast.makeText(StartCard.this,"你好，"+mProfile.USER_NAME,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StartCard.this, MainLine.class));
                        }
                        else{
                            Toast.makeText(StartCard.this,"請在手機上登入",Toast.LENGTH_SHORT).show();
                        }
                        */
                        connDb();
                        break;

                    default:
                        soundEffect = Sounds.ERROR;
                        Log.d(TAG, "Don't show anything");

                }

                // Play sound.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);
            }
        });
    }

    //給當開啟選單時就會呼叫一次
    @Override
    public boolean onPrepareOptionsMenu (Menu menu)
    {
        //清除之前選單
        menu.clear();

        //宣告選單
        MenuInflater inflater = getMenuInflater();

        //判斷位置取得選單
        switch(nowCard)
        {
            case Sex:
                inflater.inflate(R.menu.sex_menu, menu);
                break;
            case Age:
                inflater.inflate(R.menu.age_menu, menu);
                break;
            default:
                Toast.makeText(StartCard.this, "選單發生錯誤！", LENGTH_LONG).show();
                break;
        }
        return true;
    }

    //點擊選單
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.question:

                return true;
            case R.id.answer:

                return true;
            default:
                return true;
        }
    }

    //檢查藍芽是否開啟
    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }

    }

    //終止藍芽連線
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mCardScroller.activate();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    @Override
    protected void onPause()
    {
        mCardScroller.deactivate();
        super.onPause();
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        //mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);
        /*
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);


        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText.setOnEditorActionListener(mWriteListener);
        */
        /*
        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                //View view = getView();
                if (null != v) {
                    TextView textView = (TextView)findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });
        */

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }
    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        //FragmentActivity activity = getActivity();
        if (null == StartCard.this) {
            return;
        }
        final ActionBar actionBar = StartCard.this.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        //FragmentActivity activity = getActivity();
        if (null == StartCard.this) {
            return;
        }
        final ActionBar actionBar = StartCard.this.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //FragmentActivity activity = getActivity();
            switch (msg.what) {

                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear();
                            break;

                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                /*
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                    */
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);

                    //切割出name及email
                    String[] mreadMessage = readMessage.split(",");
                    mProfile.USER_NAME = mreadMessage[0];
                    mProfile.USER_EMAIL= mreadMessage[1];

                    //資料成功傳到Glass
                    LoginSuccess(mProfile.USER_NAME);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != StartCard.this) {
                        Toast.makeText(StartCard.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                        Toast.makeText(StartCard.this, cards.size() + "!", Toast.LENGTH_SHORT).show();
                        //連線成功後加入卡片
                        mAdapter = new CardAdapter(createCards(StartCard.this,Login));
                        mCardScroller.animate(Login, CardScrollView.Animation.INSERTION);
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != StartCard.this) {
                        Toast.makeText(StartCard.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    //connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    //connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(StartCard.this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    StartCard.this.finish();
                }
        }
    }

    public void LoginSuccess(String userName){
        Toast.makeText(StartCard.this,userName+"登入成功",Toast.LENGTH_SHORT).show();
    }

    // 註冊為使用者，若已經是使用者則不再註冊
    private void connDb() {

        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/db/addusers.php";

        Map<String,Object> params = new HashMap<String, Object>();

        //測試用
        mProfile.USER_NAME = "林元博";
        mProfile.USER_EMAIL = "s1100b027@nutc.edu.tw";
        params.put("name", mProfile.USER_NAME);
        params.put("email", mProfile.USER_EMAIL);
        params.put("age", 20);
        params.put("gender", 0);

        aq.ajax(url, params, String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String result, AjaxStatus status) {
                //連線成功
                if (status.getCode() == 200) {
                    //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    Log.e("PETER", result);
                    if (result.equals("0") || result.equals("2")) {
                        Toast.makeText(getApplicationContext(), "你好，" + mProfile.USER_NAME, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainLine.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "登入失敗，請再登入一次。", Toast.LENGTH_SHORT).show();
                    }
                }
                //失敗傳回狀態碼
                else {
                    Toast.makeText(getApplicationContext(), String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 檢查是否已註冊為使用者
    private void connDb0(){

        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/db/addusers.php";

        Map<String,Object> params = new HashMap<String, Object>();

        //測試用
        mProfile.USER_NAME = "林元博";
        mProfile.USER_EMAIL = "s1100b027@nutc.edu.tw";
        params.put("name", mProfile.USER_NAME);
        params.put("email", mProfile.USER_EMAIL);
        params.put("age", 20);
        params.put("gender", 0);

        aq.ajax(url, params, String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String result, AjaxStatus status) {
                //連線成功
                if (status.getCode() == 200) {
                    //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    Log.e("PETER", result);
                    if (result.equals("2")) {
                        USER = ALREADY_USER;
                        mAdapter = new CardAdapter(createCards(StartCard.this, Success));
                        mCardScroller.animate(Success, CardScrollView.Animation.NAVIGATION);
                    } else {
                        USER = NOT_USER;
                        //Toast.makeText(getApplicationContext(), "登入失敗，請再登入一次。", Toast.LENGTH_SHORT).show();
                    }
                }
                //失敗傳回狀態碼
                else {
                    Toast.makeText(getApplicationContext(), String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
