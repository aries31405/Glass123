package com.example.gameking_var2.myapplication;



import com.example.gameking_var2.myapplication.DB_connection.DB_connection;
import com.example.gameking_var2.myapplication.GPS.GPS;
import com.example.gameking_var2.myapplication.Hello.Hello;
import com.example.gameking_var2.myapplication.RANK.RANK;
import com.example.gameking_var2.myapplication.cardAdapter.CardAdapter;
import com.google.android.glass.app.Card;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

//IMPORT給card創建list（可以左右滑動的卡片）
import java.util.ArrayList;
import java.util.List;

//功能不明
import android.util.Log;
import android.widget.Switch;

/**
 * An {@link Activity} showing a tuggable "Hello World!" card.
 * <p/>
 * The main content view is composed of a one-card {@link CardScrollView} that provides tugging
 * feedback to the user when swipe gestures are detected.
 * If your Glassware intends to intercept swipe gestures, you should set the content view directly
 * and use a {@link com.google.android.glass.touchpad.GestureDetector}.
 *
 * @see <a href="https://developers.google.com/glass/develop/gdk/touch">GDK Developer Guide</a>
 */
public class MainActivity extends Activity implements GestureDetector.BaseListener
{
    //不知道
    private static final String TAG = MainActivity.class.getSimpleName();

    //定義卡片  後面的數字是卡片順序  無法顛倒（創造卡片時必須按照順序）定義變數是方便識別
    static final int Ｈello_World = 0;
    static final int Tack_Picture = 1;
    static final int GPS = 2;
    static final int Video = 3;
    static final int G_seanser = 4;
    static final int DB_connection = 5;
    static final int Rank = 6;
    static final int Exit = 7;

    /**
     * {@link CardScrollView} to use as the main content view.
     */

    //上面是設定卡片  下面是場景 設定為上面的卡片
    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    //定義手勢偵測 上面有額外繼承（implements） GestureDetector.BaseListener
    private GestureDetector GestureDetector;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        //將卡片類別 傳回來  並用自定義類別"CardAdapter"（覆寫卡片類別）
        mAdapter = new CardAdapter(createCards(this));

        //預設 抓本體
        mCardScroller = new CardScrollView(this);

        //將本體設定為用好的自定義類別
        mCardScroller.setAdapter(mAdapter);

        //手勢偵測此場景.基本偵測
        GestureDetector = new GestureDetector(this).setBaseListener(this);

        //設定場景
        setContentView(mCardScroller);

        //設定卡片點擊偵測
        setCardScrollerListener();
    }

    //多重繼承後的內建函數  偵測手勢  回傳動作
    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        return GestureDetector.onMotionEvent(event);
    }

    //自訂函式 建立多重卡片  使用內建List回傳
    private List<CardBuilder> createCards(Context context)
    {
        //List的卡片創建／／
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();

        /*  說明
        cards.add
        (
                順序，在此用變數存，不可違反順序 , new CardBuilder(context, CardBuilder.Layout.TEXT).setText("退出")  //新卡片順便設定文字
        );
        */

        //逐一建造
        cards.add
        (
                Ｈello_World, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("Hello World")
        );
        cards.add
        (
                Tack_Picture, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("Tack a picture")
        );
        cards.add
        (
                GPS, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("GPS")
        );
        cards.add
        (
                Video, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("Video")
        );
        cards.add
        (
                G_seanser, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("G-seanser")
        );
        cards.add
        (
                DB_connection, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("DB連接")
        );
        cards.add
        (
                Rank, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("評分")
        );
        cards.add
        (
                Exit, new CardBuilder(context, CardBuilder.Layout.TEXT).setText("退出")
        );
        return cards;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause()
    {
        mCardScroller.deactivate();
        super.onPause();
    }

    //內建  建立選單
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        //取得選單  (先將選單建立在res資料夾底下menu資料夾裡)
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_exit, menu);
        return true;
    }

    //內建  點擊效果 MenuItem 要引用
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.no:

                return true;
            case R.id.yes:
                finish();
                //System.exit(0); 0為正常退出 尚待測試  此為結束全部
                return true;
            default:
                return true;
        }
    }

    //設定卡片點擊監聽  自訂函式
    private void setCardScrollerListener()
    {
        //卡片的View 設定監聽 裡面得內建函式  覆寫
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //不知道
                Log.d(TAG, "Clicked view at position " + position + ", row-id " + id);
                int soundEffect = Sounds.TAP;

                //判斷點擊哪個卡片
                switch (position)
                {
                    case Ｈello_World:
                        /*
                            TTS語音
                        */
                        break;

                    case Tack_Picture:
                        Intent it_Picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult( it_Picture, 007 );
                        break;

                    case GPS:
                        startActivity(new Intent(MainActivity.this, GPS.class));
                        break;

                    case Video:
                        Intent it_Video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult( it_Video, 007 );
                        break;

                    case G_seanser:
                        startActivity(new Intent(MainActivity.this, GPS.class));
                        break;

                    case DB_connection:
                        //startService(new Intent(MainActivity.this, DB_connection.class));
                        startActivity(new Intent(MainActivity.this, DB_connection.class));
                        break;

                    case Rank:
                        startActivity(new Intent(MainActivity.this, RANK.class));
                        break;

                    case Exit:
                        openOptionsMenu();
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

    //多重繼承後的內建函式
    @Override
    public boolean onGesture(Gesture gesture)
    {
        //會傳入手勢  gesture.name()會取得手勢名稱 或是另一種 gesture ＝ Gesture.SWIPE_UP
        switch( gesture.name() )
        {
            case "SWIPE_UP":
                if( mCardScroller.getSelectedItemPosition() == Ｈello_World )
                    startActivity(new Intent(MainActivity.this, Hello.class));
                break;
            case "SWIPE_DOWN":
                //if( mCardScroller.getSelectedItemPosition() == Exit )
                    //finish();
                break;
            case "TWO_TAP":

                break;
            case "THREE_TAP":
                openOptionsMenu();
                break;
        }
        return true;
    }
}
