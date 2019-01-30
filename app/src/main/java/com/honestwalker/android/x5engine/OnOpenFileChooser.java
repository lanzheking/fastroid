package com.honestwalker.android.x5engine;

import android.net.Uri;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by lanzhe on 18-5-2.
 */
public interface OnOpenFileChooser {

    /**
     * 返回true 拦截 X5Webview onShowFileChooser，否则执行完毕后继续执行X5WebView的 onShowFileChooser
     * @param webView
     * @param valueCallback
     * @param fileChooserParams
     * @return
     */
    boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams);

}
