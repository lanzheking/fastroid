package com.honestwalker.android.commons.jscallback.bean;

/**
 * 对应webview_js_callback配置的实体对象。
 * Created by honestwalker on 15-6-2.
 */
public class JSActionConfigBean {

    private String key;
    private Class clazz;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
