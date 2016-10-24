package com.honestwalker.androidutils.ImageSelector.utils;

import java.io.Serializable;

/**
 * Depiction:   Crash 信息封装
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-3-3 下午6:25. <br />
 * Rewrite Date :  16-3-3 下午6:25. <br />
 */
public class CrashInfoBean implements Serializable {

    public static final String CRASH_BROADCASE_ACTION = "KC_CRASH_BROADCASE";
    public static final String CRASH_BROADCASE_BEAN = "crashInfoBean";

    private String processName;

    private Throwable throwable;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
