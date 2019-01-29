package com.honestwalker.android.x5engine;

import android.annotation.TargetApi;
import android.os.Build;

import com.honestwalker.android.x5engine.views.X5WebView;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * Created by lanzhe on 17-5-27.
 */

public abstract class InterceptRequest {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean interceptRule(X5WebView view, WebViewClient webViewClient,
                                 final WebResourceRequest request) {
        return interceptRule(view, webViewClient, request.getUrl().toString());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WebResourceResponse getResponse(X5WebView view, WebViewClient webViewClient,
                                           final WebResourceRequest request) {
        return getResponse(view, webViewClient, request.getUrl().toString());
    }

    /**
     * android 4.x 会调用改方法
     * @param view
     * @param webViewClient
     * @param url
     * @return
     */
    public abstract WebResourceResponse getResponse(X5WebView view, WebViewClient webViewClient, String url);

    public abstract boolean interceptRule(X5WebView view, WebViewClient webViewClient, String url);

}
