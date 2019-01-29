package com.honestwalker.android.kc_test.event;

import com.honestwalker.android.kc_test.actions.DelayAction;
import com.honestwalker.android.kc_test.actions.LogAction;
import com.honestwalker.androidutils.IO.LogCat;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by lanzhe on 16-11-11.
 */
public class Events {

    private static String TAG = "tester";

    /**
     * 派发事件
     * @param eventData
     */
    public static void dispatchEvent(final EventData eventData) {
        String event = eventData.getTask().getEvent();
        LogCat.d(TAG, "开始派发事件:[" + eventData.getTask().getEvent() + "]");
        if      ("input"    .equals(event))    HermesEventBus.getDefault().post(new InputEvent(eventData));
        else if ("click"    .equals(event))    HermesEventBus.getDefault().post(new ClickEvent(eventData));
        else if ("longclick".equals(event))    HermesEventBus.getDefault().post(new LongClickEvent(eventData));
        else if ("delay"    .equals(event))    new DelayAction().delay(new DelayEvent(eventData));
        else if ("goBack"   .equals(event))    HermesEventBus.getDefault().post(new GoBackEvent(eventData));
        else if ("log"      .equals(event))    new LogAction().doAction(new LogEvent(eventData));
        else if ("alert"    .equals(event))    HermesEventBus.getDefault().post(new AlertEvent(eventData));
        else if ("toast"    .equals(event))    HermesEventBus.getDefault().post(new ToastEvent(eventData));
        else if ("method"   .equals(event))    {}

    }

}
