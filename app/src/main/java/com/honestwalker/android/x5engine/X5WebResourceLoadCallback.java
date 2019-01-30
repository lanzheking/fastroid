package com.honestwalker.android.x5engine;

import android.graphics.Bitmap;

import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by lanzhe on 18-5-14.
 */

public abstract class X5WebResourceLoadCallback {

    public boolean onLoadResource(WebView webView, String url) {
        return false;
    }

    public boolean onPageStarted(WebView webView, String url, Bitmap bitmap) {
        return false;
    }
    public boolean onPageFinished(WebView webView, String url) {
        return false;
    }

    public boolean onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
        return false;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    public boolean onProgressChanged(WebView webView, int progressInPercent) {
        return false;
    }

}
