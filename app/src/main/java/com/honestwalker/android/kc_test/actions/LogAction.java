package com.honestwalker.android.kc_test.actions;

import com.honestwalker.android.kc_test.KCTestLauncher;
import com.honestwalker.android.kc_test.event.LogEvent;
import com.honestwalker.androidutils.IO.LogCat;

/**
 * Created by lanzhe on 16-11-3.
 */
public class LogAction extends KCTestBaseAction {

    @Override
    protected void registerBusMessage() {}

    public void doAction(LogEvent logEvent) {
        LogCat.d(KCTestLauncher.testLogTag, logEvent.getTask().getValue());
    }

}
