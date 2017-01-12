package com.cognizant.collab.collabtrak;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Joel Fraga on 8/11/16.
 */
public class AppBeaconManager {

    private static AppBeaconManager _instance = new AppBeaconManager();

    public static AppBeaconManager getInstance() {
        return _instance;
    }

    private String estimoteUUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private Region locRegion, productsRegion;
    private String deviceId = "alpha";

    private BeaconManager beaconManager;

    private boolean isScanning = false;

    private Beacon closestLocBeacon;
    private Beacon tempClosestLocBeacon;
    private long startTimeAtLoc;


    //Visit end point
    //https://mysterious-bayou-86493.herokuapp.com/api/Visits/position
//    {
//        "deviceId": "123",
//        "positionId": 5
//    }

    public AppBeaconManager() {
    }

    TimerTask updateLocationBeacon = new TimerTask() {
        @Override
        public void run() {
            closestLocBeacon = tempClosestLocBeacon;
        }
    };

    public void createBeaconManager(Context context) {

        beaconManager = new BeaconManager(context);
        locRegion = new Region("LocRegion", UUID.fromString(estimoteUUID), 2001, null);
        productsRegion = new Region("Products", UUID.fromString(estimoteUUID), 2002, null);
        closestLocBeacon = null;
        tempClosestLocBeacon = null;
        startTimeAtLoc = 0;

//        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
//            @Override
//            public void onEnteredRegion(Region region, List<Beacon> list) {
//                RequestParams params = new RequestParams();
//                params.put("uuid", region.getProximityUUID());
//                params.put("major", region.getMajor());
//                params.put("minor", region.getMinor());
//                params.put("state", "enter");
//
//                OrchestrateClient.post("user/" + userId + "/event", params, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        System.out.println(statusCode);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        System.out.println(statusCode);
//                    }
//                });
//            }
//
//            @Override
//            public void onExitedRegion(Region region) {
//                RequestParams params = new RequestParams();
//                params.put("uuid", region.getProximityUUID());
//                params.put("major", region.getMajor());
//                params.put("minor", region.getMinor());
//                params.put("state", "exit");
//
//                OrchestrateClient.post("user/" + userId + "/event", params, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        System.out.println(statusCode);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        System.out.println(statusCode);
//                    }
//                });
//            }
//        });
        Log.w("APP: ", "creating beacon manager");
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
//                Log.w("APP: ", "Beacons discovered " + list.size() );
                for (Beacon b: list) {
                    if (tempClosestLocBeacon == null || tempClosestLocBeacon.getRssi() < b.getRssi()) {
                        tempClosestLocBeacon = b;
                        startTimeAtLoc = System.currentTimeMillis();
                    }
//                    Log.w("APP: ", b.getMajor() + " " + b.getMinor() + " " + b.getRssi());

                }
                Log.w("APP: ", "End of list");
//                Log.w("APP: ", "Temp beacon is: " + tempClosestLocBeacon.getMinor() + " " + tempClosestLocBeacon.getRssi());
                if (System.currentTimeMillis() - startTimeAtLoc > 3000) {
                    updateLocation(tempClosestLocBeacon);
                    tempClosestLocBeacon = null;
                }
            }
        });
    }

    private void updateLocation(Beacon b) {
        closestLocBeacon = tempClosestLocBeacon;

        Log.w("APP: ", "Updating location");
        if (closestLocBeacon != null) {
            Log.w("APP: ", "Closest beacon is: " + closestLocBeacon.getMinor() + " " + closestLocBeacon.getRssi());
        }

        RequestParams params = new RequestParams();
        params.add("deviceId", deviceId);
        params.add("positionId", Integer.toString(b.getMinor()));
        AsyncClient.post("Visits/position", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.w("APP: ", "Response " + statusCode);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String resp, Throwable error) {
                Log.w("APP: ", "Response " + statusCode + " " + resp);
            }
        });
    }

    public void startMonitoring() {

        beaconManager.connect(new com.estimote.sdk.BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Log.w("APP: ", "service ready");
                beaconManager.startRanging(locRegion);
                isScanning = true;
            }
        });
    }

    public void stopMonitoring() {
        isScanning = false;
        beaconManager.stopRanging(locRegion);
    }


    public boolean isScanning() { return isScanning; }
}
