package com.honestwalker.android.BusEvent;

import android.app.Activity;

import com.honestwalker.androidutils.IO.LogCat;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * 用于处理事件总线业务逻辑
 * Created by lanzhe on 17-7-20.
 */

public abstract class WebViewBusEventAction<W> extends BusEventAction {

    protected Activity context;

    protected W webView;

    protected WebViewBusEventAction(Activity context, W webView) {
        super(context);
        this.webView = webView;
    }

    public void unRegisterBusEvent() {
        if(HermesEventBus.getDefault().isRegistered(this)) {
            LogCat.d("BusEvent", "反注册:" + this);
            HermesEventBus.getDefault().unregister(this);
        }
    }

}
