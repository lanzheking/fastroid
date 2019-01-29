package com.honestwalker.android.commons.jscallback.bean;

import java.io.Serializable;

/**
 * 所以js callback 参数的父类。
 * Created by honestwalker on 15-6-2.
 */
public class JSActionParamBean implements Serializable {

    private String action;
    private String callback;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
