package com.honestwalker.android.commons.jscallback.actions;

import android.app.Activity;

import com.google.gson.Gson;
import com.honestwalker.android.commons.jscallback.bean.JSActionConfigBean;
import com.honestwalker.android.commons.jscallback.io.ConfigLoader;
import com.honestwalker.android.spring.core.annotation.RunInMainThread;
import com.honestwalker.android.commons.jscallback.bean.JSActionParamBean;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.ObjectCopy;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * JS回调抽象父类，负责参数实体对象封装，调用子类业务实现(doAction)。
 * Created by honestwalker on 15-6-2.
 */
public abstract class JSCallbackActionSupport<T extends JSActionParamBean, WEBVIEW> {

    private String paramJson;

    private T paramBean;

    protected abstract String doAction(Activity context, T paramBean, WEBVIEW webView);

    protected static final String SUCCESS = "success";
    protected static final String FAIL = "fail";
    protected static final String ERROR = "error";

    public JSCallbackActionSupport(){}

    public String execute(final Activity context, String paramJson, final WEBVIEW webView) {
        Type type = getClass().getGenericSuperclass();
        if(type == null) {
            LogCat.e("JS", this.getClass() + " 没有设置参数类型 ");
            return null;
        }
        ParameterizedType parameterizedType =  (ParameterizedType)type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class<T> tClass = (Class<T>)actualTypeArguments[0];
        paramBean = new Gson().fromJson(paramJson , tClass);
        String result = doAction(context, paramBean, webView);
        return result;
    }
//    public String execute(final Activity context, JSActionConfigBean jsCallbackAction, String paramJson, final WEBVIEW webView) {
//        Type type = getClass().getGenericSuperclass();
//        if(type == null) {
//            LogCat.e("JS", jsCallbackAction + " 没有设置参数类型 ");
//            return null;
//        }
//        ParameterizedType parameterizedType =  (ParameterizedType)type;
//        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//        Class<T> tClass = (Class<T>)actualTypeArguments[0];
//        paramBean = new Gson().fromJson(paramJson , tClass);
//        String result = doAction(context, paramBean, webView);
//        return result;
//    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }

}
