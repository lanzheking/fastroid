package com.honestwalker.android.BusEvent.event;

/**
 * 是否允许web后退 js callback 事件
 * Created by lanzhe on 17-6-6.
 */
public class AllowBackEvent {

    private boolean allowBack = false;

    public boolean isAllowBack() {
        return allowBack;
    }

    public void setAllowBack(boolean allowBack) {
        this.allowBack = allowBack;
    }
}
