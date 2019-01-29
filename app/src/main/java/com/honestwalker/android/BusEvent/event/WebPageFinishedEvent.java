package com.honestwalker.android.BusEvent.event;

import android.webkit.WebView;

/**
 * Created by lanzhe on 17-5-27.
 */

public class WebPageFinishedEvent {

    private WebView webView;

    private String url;

    public WebPageFinishedEvent(){}

    public WebPageFinishedEvent(WebView webView, String url) {
        this.webView = webView;
        this.url = url;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
