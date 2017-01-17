package com.cognizant.collab.collabtrak;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by ctsuser1 on 1/16/17.
 */

public class ReadyNotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ready_notification);

        TextView tx = (TextView)findViewById(R.id.spinner);
        Typeface iconFont = Typeface.createFromAsset(getAssets(), "fonts/TeleIcon-Outline.ttf");
        tx.setTypeface(iconFont);

        final Handler delayedActivity = new Handler();
        delayedActivity.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(getBaseContext(), WaitAssociateActivity.class);
                startActivity(myIntent);
            }
        }, 3000);
    }
}
