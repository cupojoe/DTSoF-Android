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
    private ArrayAdapter<String> regionsAdapter;

    private Socket socket;
    {
        try {
            socket = IO.socket("https://mysterious-bayou-86493.herokuapp.com");
        } catch (java.net.URISyntaxException e) {
            Log.w("APP: ", "SOCKET ERROR");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.w("APP: ", "Start app");

        beaconManager = AppBeaconManager.getInstance();
        beaconManager.createBeaconManager(this);
        socket.connect();
        socket.on("/device/" + beaconManager.getDeviceId() + "/update", deviceUpdated);

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
        beaconManager.startMonitoring();
    }
    private void deactivateDevice() {
        beaconManager.stopMonitoring();
    }

    Emitter.Listener deviceUpdated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.w("APP: ", "Device updated");
            JSONObject data = (JSONObject) args[0];
            try {
                Log.w("APP: ", data.getString("data"));
                switch (data.getString("data")) {
                    case "activate":
                        activateDevice();
                    break;
                }
            } catch (JSONException e) {
                return;
            }
        }
    };
}