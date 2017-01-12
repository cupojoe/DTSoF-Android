package com.cognizant.collab.collabtrak;

import com.loopj.android.http.*;

/**
 * Created by Joel Fraga on 8/17/16.
 */
public class AsyncClient {
    private static final String BASE_URL = "https://mysterious-bayou-86493.herokuapp.com/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(composeURL(path), params, responseHandler);
    }

    public static void post(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(composeURL(path), params, responseHandler);
    }

    private static String composeURL(String path) {
        return BASE_URL + path;
    }
}
