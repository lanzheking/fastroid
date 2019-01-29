package com.honestwalker.androidutils.net;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by lanzhe on 18-3-21.
 */
public final class HttpRequest {

    public HttpRequest() {
    }

//    public void doGet(String url, RequestParams parameters) {
//        request(HttpMethod.GET, parameters);
//    }
//
//    public void doPost(String url, RequestParams parameters) {
//        request(HttpMethod.POST, parameters);
//    }
//
//    public void doDelete(String url, RequestParams parameters) {
//        request(HttpMethod.DELETE, parameters);
//    }
//
//    public void doPut(String url, RequestParams parameters) {
//        request(HttpMethod.PUT, parameters);
//    }

    public String request(HttpMethod requestMethod, RequestParams parameters) throws Throwable {
        return x.http().requestSync(requestMethod, parameters, String.class);
    }


}
