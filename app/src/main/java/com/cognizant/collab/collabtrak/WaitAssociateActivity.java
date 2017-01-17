package com.cognizant.collab.collabtrak;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by ctsuser1 on 1/16/17.
 */

public class WaitAssociateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wait_associate);

        TextView tx = (TextView)findViewById(R.id.spinner);
        Typeface iconFont = Typeface.createFromAsset(getAssets(), "fonts/TeleIcon-Outline.ttf");
        tx.setTypeface(iconFont);
    }
}
