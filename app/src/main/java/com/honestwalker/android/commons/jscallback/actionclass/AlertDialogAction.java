package com.honestwalker.android.commons.jscallback.actionclass;

import android.app.Activity;

import com.honestwalker.android.commons.jscallback.bean.AlertParamBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;
import com.honestwalker.androidutils.window.DialogHelper;

/**
 * dialog 类型的 js callback 业务实现
 * Created by honestwalker on 15-6-2.
 */
public class AlertDialogAction extends JSCallbackAction<AlertParamBean> {

    @Override
    protected void doAction(Activity context , AlertParamBean paramBean , HtmlWebView webView) {
        DialogHelper.alert(context, paramBean.getTitle(), paramBean.getContent());
    }

}
