package com.honestwalker.android.commons.jscallback.actionclass;

import android.app.Activity;

import com.honestwalker.android.commons.jscallback.bean.OpenUrlParamBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;

/**
 * OpenUrl类型的 js callback 业务实现
 * Created by honestwalker on 15-6-2.
 */
public class OpenUrlAction extends JSCallbackAction<OpenUrlParamBean> {

    @Override
    protected void doAction(Activity context , OpenUrlParamBean paramBean , HtmlWebView webView) {

//        WebActivityEntry.toWebActivity(context,paramBean.getUrl(),paramBean.getTitle(),paramBean.isLoading());

    }

}
