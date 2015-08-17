package com.example.gameking_var2.myapplication.DB_connection;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.gameking_var2.myapplication.MainActivity;
import com.example.gameking_var2.myapplication.R;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

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
public class DB_connection extends Activity {

    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollView mCardScroller;

    /**
     * "Hello World!" {@link View} generated by {@link #buildView()}.
     */
    private View mView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mView = buildView();
        connDb();
        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(new CardScrollAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Object getItem(int position) {
                return mView;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return mView;
            }

            @Override
            public int getPosition(Object item) {
                if (mView.equals(item)) {
                    return 0;
                }
                return AdapterView.INVALID_POSITION;
            }
        });
        // Handle the TAP event.
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Plays disallowed sound to indicate that TAP actions are not supported.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.DISALLOWED);
            }
        });
        setContentView(mCardScroller);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }

    /**
     * Builds a Glass styled "Hello World!" view using the {@link CardBuilder} class.
     */
    private View buildView() {


        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);
        card.setText("DDDBB");
        return card.getView();
    }


    private void connDb(){

        AQuery aq = new AQuery(this);
        String url = "http://163.17.135.76/db/select.php";

        Map<String,Object> params = new HashMap<String, Object>();
        params.put("email","s1100b043@nutc.edu.tw");

        aq.ajax(url, params, String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String result, AjaxStatus status) {
                if (status.getCode() == 200) {
                    CardBuilder card = new CardBuilder(DB_connection.this, CardBuilder.Layout.TEXT);
                    card.setText(result);
                    View view = card.getView();
                    setContentView(view);


                } else {
                    CardBuilder card = new CardBuilder(DB_connection.this,CardBuilder.Layout.TEXT);
                    card.setText("FAIL!!!");
                    View view = card.getView();
                    setContentView(view);
                }
            }
        });




    }



}
