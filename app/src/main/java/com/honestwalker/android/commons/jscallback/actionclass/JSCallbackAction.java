package com.honestwalker.android.commons.jscallback.actionclass;

import android.app.Activity;

import com.google.gson.Gson;
import com.honestwalker.android.commons.jscallback.bean.JSActionConfigBean;
import com.honestwalker.android.commons.jscallback.bean.JSActionParamBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;

import java.lang.reflect.ParameterizedType;

/**
 * JS回调抽象父类，负责参数实体对象封装，调用子类业务实现(doAction)。
 * Created by honestwalker on 15-6-2.
 */
public abstract class JSCallbackAction<T extends JSActionParamBean> {

    private String paramJson;

    private T paramBean;

    protected abstract void doAction(Activity context, T paramBean, HtmlWebView webView);

    public void execute(final Activity context, JSActionConfigBean jsCallbackAction, String paramJson, final HtmlWebView webView) {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        paramBean = (T) new Gson().fromJson(paramJson, tClass);
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                LogCat.d("JS", "doAction(" + paramBean.getAction() + ") 来自" + webView.getUrl());
                doAction(context, paramBean, webView);
            }
        });

    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }
}
