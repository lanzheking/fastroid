package com.honestwalker.android.commons.jscallback.BusEvent;

/**
 * Created by lanzhe on 17-7-3.
 */

public class LoginTaskEvent {

    public boolean manualLoginResult = false;

    public String callback;

    public String platform;

    public boolean isManualLoginResult() {
        return manualLoginResult;
    }

    public void setManualLoginResult(boolean manualLoginResult) {
        this.manualLoginResult = manualLoginResult;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
