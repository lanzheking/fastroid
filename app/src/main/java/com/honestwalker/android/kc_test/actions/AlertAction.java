package com.honestwalker.android.kc_test.actions;

import android.app.Activity;

import com.honestwalker.android.kc_test.event.AlertEvent;
import com.honestwalker.android.kc_test.event.ToastEvent;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.window.DialogHelper;
import com.honestwalker.androidutils.window.ToastHelper;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by lanzhe on 16-11-4.
 */
public class AlertAction extends KCTestBaseAction {

    @Subscribe
    public void alert(final AlertEvent alertEvent) {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.alert(alertEvent.getContext(), alertEvent.getTask().getValue());
            }
        });
    }

    @Subscribe
    public void toast(final ToastEvent event) {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastHelper.alert(event.getContext(), event.getTask().getValue());
            }
        });
    }

}
