package com.honestwalker.android.kc_test.actions;

import com.honestwalker.androidutils.IO.LogCat;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by lanzhe on 16-11-4.
 */
public abstract class KCTestBaseAction {

    protected KCTestBaseAction() {
        registerBusMessage();
    }

    protected void registerBusMessage() {
        LogCat.d("tester", "注册 " + this.getClass().getSimpleName());
        HermesEventBus.getDefault().register(this);
    }

}
