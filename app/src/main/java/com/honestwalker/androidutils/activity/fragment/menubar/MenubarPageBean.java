package com.honestwalker.androidutils.activity.fragment.menubar;

import android.os.Bundle;

/**
 * Created by honestwalker on 15-10-8.
 */
public class MenubarPageBean {

    private String target;
    private String title;
    private Class targetClass;
    private String targetUrl;
    private String action;
    private Bundle data;

    public static final String PAGE_TARGET_FRAGMENT = "fragment";
    public static final String PAGE_TARGET_ACTIVITY = "activity";
    public static final String PAGE_TARGET_FRAGMENT_WEB = "fragment_web";
    public static final String PAGE_TARGET_ACTIVITY_WEB = "activity_web";


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
