package com.honestwalker.android.BusEvent;

import com.honestwalker.android.spring.context.ApplicationContextUtils;
import com.honestwalker.androidutils.IO.LogCat;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * 用于处理事件总线业务逻辑
 * Created by lanzhe on 17-7-20.
 */

public abstract class BusEventAction implements IBusEventAction {

    protected BusEventAction() {
        LogCat.d("BusEvent", "注册:" + this);
        ApplicationContextUtils.inject(this);

        EventBusUtil.getInstance().register(this);
    }

    public void unRegisterBusEvent() {
        if(HermesEventBus.getDefault().isRegistered(this)) {
            LogCat.d("BusEvent", "反注册:" + this);
            HermesEventBus.getDefault().unregister(this);
        }
    }

}
