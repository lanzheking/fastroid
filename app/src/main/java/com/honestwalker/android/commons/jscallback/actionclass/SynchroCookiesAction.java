package com.honestwalker.android.commons.jscallback.actionclass;

import android.app.Activity;
import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.honestwalker.android.commons.jscallback.bean.AppPageBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by honestwalker on 15-7-8.
 */
public class SynchroCookiesAction extends JSCallbackAction<AppPageBean> {

    private final String TAG = "HtmlWebView";

    @Override
    protected void doAction(Activity context, AppPageBean paramBean, HtmlWebView webView) {
        syncCookie(context , webView.getUrl());
    }

    public synchronized void syncCookie(Context context, String url) {

//        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
//        cookieSyncManager.sync();

        CookieManager cookieManager = CookieManager.getInstance();
//        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.acceptCookie();

        String webCookie = cookieManager.getCookie(url);

        if(webCookie != null) {
            LogCat.d(TAG, "保存webView 变更的cookie: " + URLDecoder.decode(webCookie));
        }

        SharedPreferencesLoader.getInstance(context).putString("static_cookie" , webCookie);

        if(true) return;

        String cookie = SharedPreferencesLoader.getInstance(context).getString("cookie", "");

//        cookieManager.removeSessionCookie();

        String[] cookieStrs = cookie.split(";");
        String[] webCookieStrs = webCookie.split(";");

        // 记录本地cookie的keys列表， 用于过滤web的cookie重复项，优先使用本地的
        List<String> localCookieKeys = new ArrayList<String>();
        for (String cookieKV : cookieStrs) {
            String[] kvs = cookieKV.split("=");
            try {
                localCookieKeys.add(kvs[0]);
            } catch (Exception e) {
                ExceptionUtil.showException(e);
            }
        }

        if (cookieStrs.length > 0) {
            for (String cookieKV : cookieStrs) {    // 读取保存的登录cookie
                if(webCookieStrs == null ||
                   webCookieStrs.length == 0 ||
                   (webCookieStrs.length == 1 && webCookieStrs[0].trim().equals(""))) {
                    cookieManager.setCookie(url, cookieKV);
                    LogCat.d(TAG, "添加cookie " + cookieKV);
                } else {
                    for(String webCookieKV : webCookieStrs) {  // 读取web的cookie
                        try {
                            String[] kvs = webCookieKV.split("=");
                            if(!localCookieKeys.contains(kvs[0])) {  // 如果本地cookie没有该项cookie，添加
                                LogCat.d(TAG, "本地cookie不包含 " + kvs[0] + " 添加 " + webCookieKV);
                                cookieManager.setCookie(url, webCookieKV);
                            } else {
                                cookieManager.setCookie(url, cookieKV);
                                LogCat.d(TAG, "本地cookie包含 " + kvs[0] + " 覆盖:" + cookieKV);
                            }
                        } catch (Exception e) {
                            ExceptionUtil.showException(e);
                        }

                    }
                }
            }
        }

        CookieSyncManager.getInstance().sync();

        {   // 保存新cookie
//            webCookie = cookieManager.getCookie(url);
//            SharedPreferencesLoader.getInstance(context).putString("cookie" , webCookie);
//            LogCat.d(TAG , "保存webView 变更的cookie: " + webCookie);
        }

    }

}
