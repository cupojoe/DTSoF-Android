package com.cognizant.collab.collabtrak;

import android.content.Context;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Joel Fraga on 8/11/16.
 */
public class SocketNotificationManager {

    private static SocketNotificationManager _instance = new SocketNotificationManager();

    public static SocketNotificationManager getInstance() {
        return _instance;
    }

    private Socket socket;
    {
        try {
            socket = IO.socket("https://mysterious-bayou-86493.herokuapp.com");
        } catch (java.net.URISyntaxException e) {
            Log.w("APP: ", "SOCKET ERROR");
        }
    }


    public SocketNotificationManager() {
        socket.connect();
    }

    public void addEvent(String eventId, Emitter.Listener listener) {
        socket.on(eventId, listener);
    }

    public void removeEvent(String eventId, Emitter.Listener listener) {
        socket.off(eventId, listener);
    }

    public void disconnectSocket() {
        socket.disconnect();
    }

}
