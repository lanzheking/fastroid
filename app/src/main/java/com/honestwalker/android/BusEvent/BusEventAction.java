package com.honestwalker.android.BusEvent;

import android.content.Context;

import com.honestwalker.androidutils.IO.LogCat;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * 用于处理事件总线业务逻辑
 * Created by lanzhe on 17-7-20.
 */

public abstract class BusEventAction implements IBusEventAction {

    protected Context context;

    protected BusEventAction(Context context) {
        this.context = context;
        LogCat.d("BusEvent", "注册:" + this);
        HermesEventBus.getDefault().register(this);
    }

    public void unRegisterBusEvent() {
        if(HermesEventBus.getDefault().isRegistered(this)) {
            LogCat.d("BusEvent", "反注册:" + this);
            HermesEventBus.getDefault().unregister(this);
        }
    }

    public Context getContext() {
        return context;
    }

}
