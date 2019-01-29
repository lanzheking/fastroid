package com.honestwalker.android.BusEvent;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by lanzhe on 18-3-29.
 */
public class EventBusUtil {

    private static EventBusUtil instance;

    public synchronized static EventBusUtil getInstance() {
        if(instance == null) {
            instance = new EventBusUtil();
        }
        return instance;
    }

    public void register(Object subscriber) {
        try {
            if(!HermesEventBus.getDefault().isRegistered(subscriber)) {
                HermesEventBus.getDefault().register(subscriber);
            }
        } catch (Exception e) {}
    }

    public void unRegister(Object subscriber) {
        try {
            if(HermesEventBus.getDefault().isRegistered(subscriber)) {
                HermesEventBus.getDefault().unregister(subscriber);
            }
        } catch (Exception e) {}
    }

    public void post(Object event) {
        try {
            HermesEventBus.getDefault().post(event);
        } catch (Exception e) {}
    }

    public void postSticky(Object event) {
        try {
            HermesEventBus.getDefault().postSticky(event);
        } catch (Exception e) {}
    }

}
