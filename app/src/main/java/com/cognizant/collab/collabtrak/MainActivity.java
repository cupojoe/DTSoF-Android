package com.cognizant.collab.collabtrak;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

import com.estimote.sdk.SystemRequirementsChecker;
import com.github.nkzawa.emitter.Emitter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

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

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
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
        beaconManager.startMonitoring();
    }
    private void deactivateDevice() {
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
        TextView counter = (TextView) findViewById(R.id.counterLabel);
        counter.setText("Inactive");
        beaconManager.stopMonitoring();
    }
    private void updateTimer(String value) {
        TextView counter = (TextView) findViewById(R.id.counterLabel);
        counter.setText(value);
    }
    private void updateProduct(String value) {
        TextView productCounter = (TextView) findViewById(R.id.productCounter);
        productCounter.setText(value);
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
                    case "selfservice":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(getBaseContext(), SelfServiceActivity.class);
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