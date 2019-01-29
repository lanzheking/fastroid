package com.honestwalker.android.commons.bean;

import java.util.HashMap;

/**
 * JS 参数封装类
 * Created by lanzhe on 17-6-22.
 */
public final class JSParam {

    private HashMap<String, Object> params = new HashMap<>();

    public HashMap<String, Object> getParams() {
        return params;
    }

    public Object getValue(String key) {
        return params.get(key);
    }

    public JSParam put(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public Object get(String key) {
        return params.get(key);
    }

    public JSParam remove(String key) {
        params.remove(key);
        return this;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        for (String key : params.keySet()) {
            stringBuffer.append(key).append(get(key)).append(" ");
        }
        stringBuffer.append("]");
        return params.toString();
    }
}
