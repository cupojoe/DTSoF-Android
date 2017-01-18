package com.cognizant.collab.collabtrak;

import com.cognizant.collab.collabtrak.activities.*;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.cognizant.collab.collabtrak.managers.AppBeaconManager;
import com.cognizant.collab.collabtrak.managers.SessionManager;
import com.cognizant.collab.collabtrak.managers.SocketNotificationManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private AppBeaconManager beaconManager;
    private SocketNotificationManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.w("APP: ", "Start app");

        beaconManager = AppBeaconManager.getInstance();
        beaconManager.createBeaconManager(this);
        socketManager = SocketNotificationManager.getInstance();
        socketManager.addEvent("/device/" + beaconManager.getDeviceId() + "/update", deviceUpdated);

//        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Intent myIntent = new Intent(getBaseContext(), SelfServiceActivity.class);
//                startActivity(myIntent);
//
//                if (isChecked) {
//                    // The toggle is enabled
//                    beaconManager.startMonitoring();
//                } else {
//                    // The toggle is disabled
//                    beaconManager.stopMonitoring();
//                }
//            }
//        });

    }

    @Override
    protected void onDestroy(){
        socketManager.disconnectSocket();
    }


    @Override
    protected void onResume(){
        super.onResume();

//        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.toggle_tracking:
//
//                if (beaconManager.isConnected()) {
//                    beaconManager.stopMonitoring();
//                    clearList();
//                } else {
//                    beaconManager.startMonitoring();
//                }
//                return true;
            case R.id.logout_btn:
                sessionManager.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    @Override
//    public void onClick(View view){
//
//        switch(view.getId()){
//            case R.id.toggleButton:
//                Button toggle = (Button)findViewById(R.id.toggleButton);
//                if (beaconManager.isScanning()) {
//                    beaconManager.stopMonitoring();
//                } else {
//                    beaconManager.startMonitoring();
//                }
//                break;
//        }
//    }

    private void activateDevice() {
        TextView counter = (TextView) findViewById(R.id.counterLabel);
        counter.setText("Active");
        TextView label = (TextView)findViewById(R.id.counterLabelText);
        label.setText("");
        beaconManager.startMonitoring();
    }
    private void deactivateDevice() {
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
        TextView counter = (TextView) findViewById(R.id.counterLabel);
        counter.setText("Inactive");
        TextView label = (TextView)findViewById(R.id.counterLabelText);
        label.setText("");
        beaconManager.stopMonitoring();
    }
    private void updateTimer(String value) {
        TextView counter = (TextView) findViewById(R.id.counterLabel);
        counter.setText(value);
        TextView label = (TextView)findViewById(R.id.counterLabelText);
        label.setText("Average waiting time");
    }
    private void updateProduct(final String value) {
        TextView productCounter = (TextView) findViewById(R.id.productCounter);
        productCounter.setText(value);
        final TextView tx = (TextView)findViewById(R.id.counterLabel);
        final CharSequence tempText = tx.getText();
        tx.setText("");
        Typeface iconFont = Typeface.createFromAsset(getAssets(), "fonts/TeleIcon-Outline.ttf");
        tx.setTypeface(iconFont);
        tx.setText("V");

        final Handler delayedActivity = new Handler();
        delayedActivity.postDelayed(new Runnable() {
            @Override
            public void run() {
                tx.setText("");
                tx.setTypeface(Typeface.DEFAULT);
                tx.setText(tempText);
            }
        }, 2000);
    }

    Emitter.Listener deviceUpdated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.w("APP: ", "Device updated");
            JSONObject data = (JSONObject) args[0];
            try {
                Log.w("APP: ", data.getString("data"));
                switch (data.getString("data")) {
                    case "activated":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activateDevice();
                            }
                        });
                        break;
                    case "deactivate":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                deactivateDevice();
                            }
                        });
                        break;
                    case "timer":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTimer("20-25");
                            }
                        });
                        break;
                    case "product":
                        final String qty = data.getString("quantity");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateProduct(qty);
                            }
                        });
                        break;
                    case "ready":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(getBaseContext(), ReadyNotificationActivity.class);
                                startActivity(myIntent);
                            }
                        });
                        break;
                    case "selfservicethankyou":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(getBaseContext(), SelfserviceThankyouActivity.class);
                                startActivity(myIntent);
                            }
                        });
                        break;
                    case "selfservice":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(getBaseContext(), SelfServiceActivity.class);
                                startActivity(myIntent);
                            }
                        });
                        break;
                    case "completed":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(getBaseContext(), ServiceCompletedActivity.class);
                                startActivity(myIntent);
                            }
                        });
                        break;
                    case "comeback":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(getBaseContext(), ComeBackActivity.class);
                                startActivity(myIntent);
                            }
                        });
                        break;
                }
            } catch (JSONException e) {
                Log.w("APP: ", "JSON Error " + e.getMessage());
                return;
            }
        }
    };
}