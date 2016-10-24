package com.honestwalker.android.commons.jscallback.actionclass;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.honestwalker.android.commons.jscallback.JSActionExecutor;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.window.ToastHelper;

/**
 * 支持JS Callback addJavascriptInterface 对象，自动根据action寻找实现类
 * Created by honestwalker on 15-6-2.
 */
public class JSCallbackObjectFilter {

    private Activity context;
    private HtmlWebView webView;

    public
    JSCallbackObjectFilter(Activity context, HtmlWebView webView) {
        this.context = context;
        this.webView = webView;
    }

    @JavascriptInterface
    public void app_callback(final String json) {

        LogCat.d("JS", json);

        UIHandler.post(new Runnable() {
            @Override
            public void run() {
//                JSExecutorFactory.execute(context, json);
                JSActionExecutor.execute(context, webView, json);
            }
        });
        // 传递给JSAction处理器处理
//        JSActionExecutor.execute(context, webView, json);

    }

    @JavascriptInterface
    public void toast(final String show) {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {

                ToastHelper.alert(context, show);
            }
        });

    }

}
