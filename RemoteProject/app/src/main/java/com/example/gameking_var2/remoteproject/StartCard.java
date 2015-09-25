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
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class StartCard extends Activity  implements GestureDetector.BaseListener
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
    private CardAdapter mAdapter;
    private CardScrollView mCardScroller;

    //現在的卡片  用來判斷開啟哪個選單
    private int nowCard = -1;

    //定義viewFlipper
    private ViewFlipper vfSex, vfAge;

    //定義手勢偵測
    private GestureDetector GestureDetector;

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

    // 使用者是否已經存在資料庫
    private int USER;
    private final int NOT_USER = 0;
    private final int ALREADY_USER = 1;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        //將卡片類別 傳回來  並用自定義類別"CardAdapter"（覆寫卡片類別）
        mAdapter = new CardAdapter(createCards(this,Connection));
        nowCard = Connection;

        //預設 抓本體
        mCardScroller = new CardScrollView(this);

        //將本體設定為用好的自定義類別
        mCardScroller.setAdapter(mAdapter);

        //設定場景
        setContentView(mCardScroller);

        //設定卡片點擊事件
        setCardScrollerListener();

        // 取得本機的 Bluetooth Adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 如果 Adapter是null，表示藍芽不能用
        if (mBluetoothAdapter == null)
        {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);

    }

    //-----------------------建立卡片--------------------//

    //建立滑動卡片 使用List
    private List<CardBuilder> createCards(Context context,int position)
    {
        //List的卡片創建
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();

        Log.e("createCards",position + "!");

        //新增卡片
        cards.add
        (
            0, new CardBuilder(context, CardBuilder.Layout.CAPTION).addImage(R.drawable.con_r)
        );
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
                switch (nowCard)
                {
                    case Connection:
                        Toast.makeText(StartCard.this, "Connection", Toast.LENGTH_SHORT).show();
                        String a = "off";
//                        int b = Integer.parseInt(a);
                        break;
                    case Success:
                        connDb();
                        break;

                    default:
                        soundEffect = Sounds.ERROR;
                        Log.d(TAG, "Don't show anything");
                        connDb();

                }

                // Play sound.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);
            }
        });
    }

    //----------------------變更卡片---------------------//

    //新增卡片
    private void insertNewCard(int position)
    {
        //新增的卡片
        CardBuilder card;
        switch ( position )
        {
            case Success:
                card = new CardBuilder(this, CardBuilder.Layout.CAPTION);
                card.addImage(R.drawable.success);
                break;
            default:
                return;
        }

        mAdapter.clearCard();

        //進行新增  Adapter裡的變數(CardBuilder)
        mAdapter.insertCard(0, card);

        //將現在的卡片進行更新(新增)
        mCardScroller.animate(0, CardScrollView.Animation.INSERTION);

        nowCard = position;

    }

    //--------------------手勢動作------------------//

    //偵測手勢動作，回傳事件
    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        return GestureDetector.onMotionEvent(event);
    }

    @Override
    public boolean onGesture(Gesture gesture)
    {
        //在選擇性別頁面動作
        if(nowCard == Sex)
        {
            //會傳入手勢  gesture.name()會取得手勢名稱 或是另一種 gesture ＝ Gesture.SWIPE_UP
            switch (gesture.name())
            {
                case "SWIPE_DOWN":
                    vfSex.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
                    vfSex.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
                    vfSex.showPrevious();
                    break;
                case "SWIPE_UP":
                    vfSex.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_in));
                    vfSex.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_out));
                    vfSex.showNext();
                    break;
            }
        }

        //在選擇年齡頁面動作
        else if(nowCard == Age)
        {
            //會傳入手勢  gesture.name()會取得手勢名稱 或是另一種 gesture ＝ Gesture.SWIPE_UP
            switch (gesture.name())
            {
                case "SWIPE_DOWN":
                    vfAge.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
                    vfAge.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
                    vfAge.showPrevious();
                    break;
                case "SWIPE_UP":
                    vfAge.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_in));
                    vfAge.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_out));
                    vfAge.showNext();
                    break;
            }
        }
        return true;
    }

    //------------------------內建選單----------------------//

    //給當開啟選單時就會呼叫一次
    @Override
    public boolean onPrepareOptionsMenu (Menu menu)
    {
        Log.e("nowCard", nowCard + "!");
        return true;
    }

    //點擊選單
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

            return true;
    }

    //--------------------------藍芽-----------------------//

    //檢查藍芽是否開啟
    @Override
    public void onStart()
    {
        super.onStart();

        // 如果藍芽沒有開啟，則要求開啟
        // setupChat() 會在onActivityResult 被呼叫
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null)
        {
            setupChat();
        }

    }

    //終止藍芽連線
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mChatService != null)
        {
            mChatService.stop();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mCardScroller.activate();
        // 在 onResume()這裡檢查藍芽在 onStart()沒有被允許，所以我們暫停來允許藍芽
        // onResume() 會在 ACTION_REQUEST_ENABLE activity 傳回時被呼叫.
        if (mChatService != null)
        {
            // 只有狀態是 STATE_NONE，知道我們還沒開始藍芽服務
            if (mChatService.getState() == BluetoothChatService.STATE_NONE)
            {
                // 開啟 Bluetooth chat services
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

    //設定藍芽傳輸的UI及背景資料
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // 初始化 BluetoothChatService 來表現藍芽連接
        mChatService = new BluetoothChatService(this, mHandler);

        // 初始化緩衝區給 outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }
    /**
     * 更新 action bar上面的狀態
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId)
    {
        //FragmentActivity activity = getActivity();
        if (null == StartCard.this)
        {
            return;
        }
        final ActionBar actionBar = StartCard.this.getActionBar();
        if (null == actionBar)
        {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle)
    {
        //FragmentActivity activity = getActivity();
        if (null == StartCard.this)
        {
            return;
        }
        final ActionBar actionBar = StartCard.this.getActionBar();
        if (null == actionBar)
        {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     *  Handler 從BluetoothChatService接收資料
     */
    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            //FragmentActivity activity = getActivity();
            switch (msg.what)
            {

                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1)
                    {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear_text();
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

                    // 建立一個字串將byte從buffer取出
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    //切割出name及email
                    String[] mreadMessage = readMessage.split(",");
                    mProfile.USER_NAME = mreadMessage[0];
                    mProfile.USER_EMAIL= mreadMessage[1];
                    mProfile.USER_AGE=mreadMessage[2];
                    mProfile.USER_SEX=mreadMessage[3];

                    //若傳來的值為null，代表資料庫已有使用者年齡及性別資料
                    if(mProfile.USER_AGE.equals("null")){
                        mProfile.USER_AGE = "20";
                    }
                    if(mProfile.USER_SEX.equals( "null")){
                        mProfile.USER_SEX = "0";
                    }

                    //資料成功傳到Glass
                    LoginSuccess(mProfile.USER_NAME);

                    // 檢查是否已經為使用者，之後跳轉到 profile頁面
                    connDb0();
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // 儲存已連結的裝置名稱
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != StartCard.this)
                    {
                        //Toast.makeText(StartCard.this, "Connected to "
                        //+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                        //藍芽連線成功後加入卡片
                        //insertNewCard(Profile);

                        // 刪除前一張卡片
                        //deleteCard(0);
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != StartCard.this)
                    {
                        //Toast.makeText(StartCard.this, msg.getData().getString(Constants.TOAST),
                         //       Toast.LENGTH_SHORT).show();
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
                // 允許藍芽要求後
                if (resultCode == Activity.RESULT_OK)
                {
                    // 藍芽允許後設定
                    setupChat();
                }
                else
                {
                    // 使用者沒有允許藍芽開啟或發生錯誤
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(StartCard.this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    StartCard.this.finish();
                }
        }
    }

    // 接收到登入資料後，Toast出來
    public void LoginSuccess(String userName)
    {
        Toast.makeText(StartCard.this,userName+", Login Success!",Toast.LENGTH_SHORT).show();
    }

    // 登入，檢查是否已註冊為使用者，之後跳轉到 profile頁面
    private void connDb0(){

        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/db/selectusers.php";

        Map<String,Object> params = new HashMap<String, Object>();

        //測試用
        params.put("email", mProfile.USER_EMAIL);

        aq.ajax(url, params, String.class, new AjaxCallback<String>()
        {

            @Override
            public void callback(String url, String result, AjaxStatus status)
            {
                //連線成功
                if (status.getCode() == 200)
                {
                    //資料庫已經有使用者資料
                    if (result.equals("2"))
                    {
                        USER = ALREADY_USER;
//                        Toast.makeText(StartCard.this, "ALREADY_USER", Toast.LENGTH_SHORT).show();
                    }
                    else if (result.equals("0"))
                    {
                        USER = NOT_USER;
//                        Toast.makeText(StartCard.this, "NOT_USER", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(StartCard.this, "Google account login failed", Toast.LENGTH_SHORT).show();
                    }
                    insertNewCard(Success);
                }
                //失敗傳回HTTP狀態碼
                else {
                    Toast.makeText(getApplicationContext(), String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 註冊為使用者，若已經是使用者則不再註冊
    private void connDb()
    {

        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/db/addusers.php";

        Map<String,Object> params = new HashMap<String, Object>();

        //測試用
        params.put("name", mProfile.USER_NAME);
        params.put("email", mProfile.USER_EMAIL);
        params.put("age", Integer.parseInt(mProfile.USER_AGE));
        params.put("gender", Integer.parseInt(mProfile.USER_SEX));

        aq.ajax(url, params, String.class, new AjaxCallback<String>()
        {

            @Override
            public void callback(String url, String result, AjaxStatus status)
            {
                //連線成功
                if (status.getCode() == 200)
                {
                    //登入失敗
                    if (result.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Login failed, please login again", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // 登入系統成功
                        Toast.makeText(getApplicationContext(), "Hi, " + mProfile.USER_NAME, Toast.LENGTH_SHORT).show();
                        mProfile.USER_ID = result;

                        try//將ID存起來
                        {
                            FileOutputStream out = openFileOutput("Id.txt",MODE_PRIVATE);
                            out.write(mProfile.USER_ID.getBytes());
                            out.close();
                        }
                        catch(IOException e)
                        {

                        }

                        Intent it = new Intent(getApplicationContext(), MainLine.class);
                        startActivity(it);

                        // 結束登入程序
                        StartCard.this.finish();
                    }
                }
                //失敗傳回HTTP狀態碼
                else
                {
                    Toast.makeText(getApplicationContext(), String.valueOf(status.getCode()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
