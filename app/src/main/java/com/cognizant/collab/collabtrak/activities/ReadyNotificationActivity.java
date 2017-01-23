package com.cognizant.collab.collabtrak.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cognizant.collab.collabtrak.MainActivity;
import com.cognizant.collab.collabtrak.R;

/**
 * Created by ctsuser1 on 1/16/17.
 */

public class ReadyNotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ready_notification);

        TextView tx = (TextView)findViewById(R.id.spinner);
        Typeface iconFont = Typeface.createFromAsset(getAssets(), "fonts/TeleIcon-Solid.ttf");
        tx.setTypeface(iconFont);

        MediaPlayer player = MediaPlayer.create(ReadyNotificationActivity.this,R.raw.tmobile_jingle);
        player.start();

        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 400 milliseconds
        v.vibrate(400);

        final Handler delayedActivity = new Handler();
        delayedActivity.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(getBaseContext(), WaitAssociateActivity.class);
                startActivity(myIntent);

            }
        }, 5000);
    }
}
