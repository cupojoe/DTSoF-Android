package com.cognizant.collab.collabtrak.managers;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


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
