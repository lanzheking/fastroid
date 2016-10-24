package com.honestwalker.android.commons.views.HtmlWebView;

import android.graphics.Bitmap;
import android.webkit.WebView;


/**
 * HtmlWebView 回调
 * Created by honestwalker on 15-7-23.
 */
public interface HtmlWebViewCallback {

    public boolean shouldOverrideUrlLoading(WebView view, String url);

    public void onPageStarted(WebView view, String url, Bitmap favicon);

    public void onPageFinished(WebView view, String url);

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

    public void onNetworkErrorRetry(WebView view, String url);

}
