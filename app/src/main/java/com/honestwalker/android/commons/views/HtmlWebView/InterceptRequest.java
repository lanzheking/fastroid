//package com.honestwalker.android.commons.views.HtmlWebView;
//
//import android.annotation.TargetApi;
//import android.os.Build;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebResourceResponse;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
///**
// * Created by lanzhe on 17-5-27.
// */
//
//public abstract class InterceptRequest {
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public boolean interceptRule(HtmlWebView view, WebViewClient webViewClient, WebResourceRequest request) {
//        return interceptRule(view, webViewClient, request.getUrl().getPath());
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public WebResourceResponse getResponse(HtmlWebView view, WebViewClient webViewClient, WebResourceRequest request) {
//        return getResponse(view, webViewClient, request.getUrl().getPath());
//    }
//
//    /**
//     * android 4.x 会调用改方法
//     * @param view
//     * @param webViewClient
//     * @param url
//     * @return
//     */
//    public abstract WebResourceResponse getResponse(HtmlWebView view, WebViewClient webViewClient, String url);
//
//    public abstract boolean interceptRule(HtmlWebView view, WebViewClient webViewClient, String url);
//
//}
