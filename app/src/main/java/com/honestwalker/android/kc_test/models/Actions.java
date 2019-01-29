package com.honestwalker.android.kc_test.models;

import java.util.Map;
import java.util.Queue;

/**
 * 配置最外层bean对象
 * Created by lanzhe on 16-10-27.
 */
public class Actions {

    private boolean enable;

    private String logTag;

    private boolean logDialogEnable;

    private Map<String, Action> actions;

    public Map<String, Action> getActions() {
        return actions;
    }

    public void setActions(Map<String, Action> actions) {
        this.actions = actions;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLogTag() {
        return logTag;
    }

    public void setLogTag(String logTag) {
        this.logTag = logTag;
    }

    public boolean isLogDialogEnable() {
        return logDialogEnable;
    }

    public void setLogDialogEnable(boolean logDialogEnable) {
        this.logDialogEnable = logDialogEnable;
    }
}
