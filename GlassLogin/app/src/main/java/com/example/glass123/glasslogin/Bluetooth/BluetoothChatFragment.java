package com.example.glass123.glasslogin.Bluetooth;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass123.glasslogin.ConnectSuccess;
import com.example.glass123.glasslogin.Mplayer.Player;
import com.example.glass123.glasslogin.R;

import java.util.Locale;


/**
 * Created by 海馬瀨人 on 2015/9/1.
 */
public class BluetoothChatFragment extends Activity implements TextToSpeech.OnInitListener{

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int MY_DATA_CHECK_CODE = 0;

    // Layout Views
    private Button mSendButton;
    private Button mBTButton;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    //private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    //Bluetooth Chat Services Member Objects
    private BluetoothChatService mChatService = null;

    // User Profile
    private Profile mProfile;
    //private TextView mUserProfileText;

    private TextToSpeech tts,tts2;
    Player player;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        setContentView(R.layout.bluetooth_connect);

        //按鈕findElementById
        mBTButton = (Button)findViewById(R.id.button_bluetooth);

        // 使用者資料顯示findViewById
        //mUserProfileText = (TextView)findViewById(R.id.text_user_profile);

        // 取得本地藍芽解析器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 如果Adapter是null，則不支援藍芽
        if (mBluetoothAdapter == null) {
            //FragmentActivity activity = getActivity();
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();

            this.finish();
        }

        // 取得SetProfileAge或是MyProfile傳過來的使用者Google帳戶資料
        Bundle bundle = this.getIntent().getExtras();
        mProfile = new Profile();
        mProfile.USER_NAME = bundle.getString("username");
        mProfile.USER_EMAIL = bundle.getString("useremail");
        mProfile.USER_IMAGE = bundle.getString("userimage");
        mProfile.USER_SEX = bundle.getString("usersex");
        mProfile.USER_AGE = bundle.getString("userage");

//        Toast.makeText(getApplicationContext(),mProfile.USER_SEX +" and "+ mProfile.USER_AGE,Toast.LENGTH_SHORT).show();
        // 顯示使用者Google帳戶資料
        //mUserProfileText.setText(mProfile.USER_NAME + ", " + mProfile.USER_EMAIL);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent,MY_DATA_CHECK_CODE);

        //player = new Player("http://163.17.135.75/TTS/Lelogin/connect.mp3");
        //player.play();
    }


    @Override
    public void onStart() {
        super.onStart();
        //藍芽沒有開啟的話，則跳出視窗要求開啟
        // setupChat() 會在onActivityResult 被呼叫
        if (!mBluetoothAdapter.isEnabled()) {

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mChatService != null) {
            mChatService.stop();
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
    public void onResume() {
        super.onResume();

        // 在 onResume()這裡檢查藍芽在 onStart()沒有被允許，所以我們暫停來允許藍芽
        // onResume() 會在 ACTION_REQUEST_ENABLE activity 傳回時被呼叫.
        if (mChatService != null) {
            // 只有狀態是 STATE_NONE，知道我們還沒開始藍芽服務
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // 開啟 Bluetooth chat services
                mChatService.start();
            }
        }
    }

    //設定藍芽傳輸的UI及背景資料
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // 開啟藍芽裝置清單
        mBTButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               /* if (null != v) {
                    Intent serverIntent = new Intent(BluetoothChatFragment.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                }*/
                tts.speak("與您的", TextToSpeech.QUEUE_ADD, null);
                tts2.speak("Google Glass", TextToSpeech.QUEUE_ADD, null);
                tts.speak("連接，請點擊", TextToSpeech.QUEUE_ADD, null);
                tts2.speak("Login Creative Glass", TextToSpeech.QUEUE_ADD, null);
                tts.speak("按鈕，選擇您的", TextToSpeech.QUEUE_ADD, null);
                tts2.speak("Google Glass。", TextToSpeech.QUEUE_ADD, null);
                tts.speak("登入資料將從行動裝置傳送至", TextToSpeech.QUEUE_ADD, null);
                tts2.speak("Google Glass。", TextToSpeech.QUEUE_ADD, null);

            }
        });

        // 初始化 BluetoothChatService 來表現藍芽連接
        mChatService = new BluetoothChatService(this, mHandler);

        // 初始化緩衝區給 outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    // 讓裝置能被搜尋
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    // 傳送登入資料
    public void sendProfileMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }

    // 藍芽連線成功後，將使用者Google帳戶傳到眼鏡後，跳轉到成功頁面
    public void onLoginConnectSuccess(){
//        if(player.pause())
//        {
//
//        }
        Intent it = new Intent(BluetoothChatFragment.this, ConnectSuccess.class);
        startActivity(it);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        //FragmentActivity activity = getActivity();
        if (null == BluetoothChatFragment.this) {
            return;
        }
        final ActionBar actionBar = BluetoothChatFragment.this.getActionBar();
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
        if (null == BluetoothChatFragment.this) {
            return;
        }
        final ActionBar actionBar = BluetoothChatFragment.this.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * Handler 從BluetoothChatService接收資料
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
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    // 建立一個字串將byte從buffer取出
                    String writeMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;

                    // 建立一個字串將byte從buffer取出
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // 儲存已連結的裝置名稱
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != BluetoothChatFragment.this) {

                        Toast.makeText(BluetoothChatFragment.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                        String message = mProfile.USER_NAME +","+ mProfile.USER_EMAIL +","+mProfile.USER_AGE+","+mProfile.USER_SEX;
                        sendProfileMessage(message);
                        onLoginConnectSuccess();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != BluetoothChatFragment.this) {
//                        Toast.makeText(BluetoothChatFragment.this, msg.getData().getString(Constants.TOAST),
//                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    // startActivityForResult(Intent, Intent request codes); 之後的結果
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
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
                    Toast.makeText(BluetoothChatFragment.this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    BluetoothChatFragment.this.finish();
                }
                break;
            case MY_DATA_CHECK_CODE:
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    // success, create the TTS instance
                    tts = new TextToSpeech(this, this);
                    tts2 = new TextToSpeech(this,this);
                }
                else {
                    // missing data, install it
                    Intent installIntent = new Intent();
                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
                break;
        }
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    // 建立ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflater.inflate(R.menu.bluetooth_menu, menu);
        getMenuInflater().inflate(R.menu.bluetooth_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(BluetoothChatFragment.this,
                    "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
            tts2.setLanguage(Locale.ENGLISH);
            tts.setLanguage(Locale.TAIWAN);

        }
        else if (status == TextToSpeech.ERROR) {
            Toast.makeText(BluetoothChatFragment.this,
                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
        }
    }
}
