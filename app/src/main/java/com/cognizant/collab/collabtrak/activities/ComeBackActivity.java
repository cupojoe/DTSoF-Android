package com.cognizant.collab.collabtrak.activities;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cognizant.collab.collabtrak.R;

/**
 * Created by ctsuser1 on 1/16/17.
 */

public class ComeBackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.come_back);

        TextView tx = (TextView)findViewById(R.id.spinner);
        Typeface iconFont = Typeface.createFromAsset(getAssets(), "fonts/TeleIcon-Solid.ttf");
        tx.setTypeface(iconFont);
    }
}
