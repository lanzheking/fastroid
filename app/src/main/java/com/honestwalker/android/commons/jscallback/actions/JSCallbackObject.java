package com.honestwalker.android.commons.jscallback.actions;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.honestwalker.androidutils.IO.LogCat;

/**
 * 支持JS Callback addJavascriptInterface 对象，自动根据action寻找实现类
 * Created by honestwalker on 15-6-2.
 */
public class JSCallbackObject<WEBVIEW> {

    private Activity context;
    private WEBVIEW webView;
    private String appId;

    public JSCallbackObject(Activity context, WEBVIEW webView) {
        this.context = context;
        this.webView = webView;
    }

    public JSCallbackObject(Activity context, WEBVIEW webView, String appId) {
        this.context = context;
        this.webView = webView;
        this.appId = appId;
    }

    @JavascriptInterface
    public String app_callback(final String json) {
//        return JSActionExecutor.execute(context, appId, webView ,json);
        return JSActionExecutor.execute(context, webView ,json);
    }

}
