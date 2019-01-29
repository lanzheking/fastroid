package com.honestwalker.android.commons.jscallback.bean;

/**
 * Created by lanzhe on 17-6-22.
 */

public class SyncCookieBean extends JSActionParamBean  {
    // logout
    private String event;

    private String platform;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
