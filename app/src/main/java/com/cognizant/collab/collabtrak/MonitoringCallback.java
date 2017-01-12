package com.cognizant.collab.collabtrak;

import java.util.ArrayList;

/**
 * Created by ctsuser1 on 8/22/16.
 */
interface MonitoringCallback {
    void onMonitoringStarted(ArrayList<String> regions);
}