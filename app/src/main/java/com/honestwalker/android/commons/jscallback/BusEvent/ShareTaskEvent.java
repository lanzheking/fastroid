package com.honestwalker.android.commons.jscallback.BusEvent;

/**
 * Created by lanzhe on 17-7-3.
 */

public class ShareTaskEvent {

    public boolean manualShareResult = false;

    public String callback;

    public boolean isManualShareResult() {
        return manualShareResult;
    }

    public void setManualShareResult(boolean manualShareResult) {
        this.manualShareResult = manualShareResult;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
