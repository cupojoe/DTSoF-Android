package com.cognizant.collab.collabtrak;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.estimote.sdk.SystemRequirementsChecker;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SessionManager sessionManager;
    private AppBeaconManager beaconManager;
    private ArrayAdapter<String> regionsAdapter;
    private ListView regionsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TextView userIdView = (TextView) findViewById(R.id.userId);
//        regionsListView = (ListView)findViewById(R.id.list_regions);
//
//        sessionManager = new SessionManager(this);
//        sessionManager.checkLogin();
//
//        HashMap<String, String> user = sessionManager.getUserDetails();
//        userIdView.setText(user.get("email"));

        Log.w("APP: ", "Start app");

        beaconManager = AppBeaconManager.getInstance();
        beaconManager.createBeaconManager(this);
//        beaconManager.startMonitoring();

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

//        Switch aSwitch = (Switch)findViewById(R.id.trackSwitch);
//        aSwitch.setOnClickListener(this);

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

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.toggleButton:
                if (beaconManager.isScanning()) {
                    beaconManager.stopMonitoring();
                } else {
                    beaconManager.startMonitoring();
                }
                break;
        }
    }
}