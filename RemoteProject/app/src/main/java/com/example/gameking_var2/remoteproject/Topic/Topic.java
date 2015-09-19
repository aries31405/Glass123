package com.example.gameking_var2.remoteproject.Topic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.gameking_var2.remoteproject.CardsAdapter.CardAdapter;
import com.example.gameking_var2.remoteproject.R;
import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

/**
 * Created by 孔雀舞 on 2015/9/17.
 */
public class Topic  extends Activity {

    Card card;

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        card = new Card(this);
        card.setText("沒東西");
        View view = card.getView();
        setContentView(view);
    }


}
