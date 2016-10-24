package com.honestwalker.android.commons.jscallback.actionclass;

import android.app.Activity;

import com.honestwalker.android.commons.jscallback.bean.JSActionParamBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;

/**
 * dialog 类型的 js callback 业务实现
 * Created by honestwalker on 15-6-2.
 */
public class LoginAction extends JSCallbackAction<JSActionParamBean> {

    private final String TAG = "HtmlWebViewLogin";

    private static int retryTimes = 0;

    @Override
    protected void doAction(Activity context , JSActionParamBean paramBean , HtmlWebView webView) {



    }

}
