package com.honestwalker.android.kc_test.actions;

import com.honestwalker.android.kc_test.KCTestLauncher;
import com.honestwalker.android.kc_test.event.DelayEvent;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.pool.ThreadPool;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by lanzhe on 16-11-4.
 */
public class DelayAction extends KCTestBaseAction {

    @Override
    protected void registerBusMessage() {}

    @Subscribe
    public void delay(DelayEvent delayEvent) {

        try{
            long time = Long.parseLong(delayEvent.getTask().getValue());
            KCTestLauncher.log("等待" + delayEvent.getTask().getValue() + "毫秒");
            ThreadPool.sleep(time);
        } catch (Exception e) {
            ExceptionUtil.showException(e);
        }

    }

}
