package com.honestwalker.android.commons.jscallback.actions;

import android.app.Activity;

import com.google.gson.Gson;
import com.honestwalker.android.commons.jscallback.bean.JSActionConfigBean;
import com.honestwalker.android.commons.jscallback.bean.JSActionParamBean;
import com.honestwalker.android.commons.jscallback.io.ConfigLoader;
import com.honestwalker.android.spring.context.ApplicationContext;
import com.honestwalker.android.spring.context.ApplicationContextUtils;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.util.ArrayList;

/**
 * JSAction 处理器，
 * 1. 解析配置 webview_js_callback 配置
 * 2. 根据action寻找指定业务实现jscallback action 并执行业务实现。
 * Created by honestwalker on 15-6-2.
 */
public class JSActionExecutor<WEBVIEW> {

    public static ArrayList<JSActionConfigBean> jsActionConfigBeanList;

    public static void init(Activity context) {
        if(jsActionConfigBeanList == null || jsActionConfigBeanList.size() == 0) {
            try {
                jsActionConfigBeanList = ConfigLoader.loadConfig(context);
            } catch (Exception e) {
            }
        }
    }

    public static void init(Activity context, String applicationId) {
        if(jsActionConfigBeanList == null || jsActionConfigBeanList.size() == 0) {
            try {
                jsActionConfigBeanList = ConfigLoader.loadConfig(context,  applicationId);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 根据action 在配置中相应action中执行业务
     * @param context
     * @param webView
     * @param json
     */
    public static <WEBVIEW> String execute(Activity context , WEBVIEW webView, String json) {

        // 根据action 得到响应的 JSActionConfigBean 对象
        LogCat.d("JS", "json=" + json);
        JSActionParamBean actionBean = new Gson().fromJson(json , JSActionParamBean.class);
        String action = actionBean.getAction();

        if(PreventRepeatingManager.isRepeating(action)) return "FAIL";

        LogCat.d("JS", "action=" + action);
        try {

            // 获取实现具体业务的JSCallbackAction对象
            JSCallbackActionSupport jsCallbackAction = ApplicationContextUtils.getApplicationContext().getBean(action, JSCallbackActionSupport.class);
            if(jsCallbackAction == null) {
                LogCat.d("JS", "找不打JS action对应的ActionBean对象 name=" + action);
                return "fail";
            }
            LogCat.d("JS", "jsCallbackAction===============" + jsCallbackAction);
            jsCallbackAction.setParamJson(json);

//            PreventRepeating preventRepeating = jsCallbackAction.getClass().getAnnotation(PreventRepeating.class);
//            if(preventRepeating != null) {
//
//            }

//            PreventRepeatingManager.startExecute(action);

            return jsCallbackAction.execute(context, json, webView);
        } catch (Exception e) {
            ExceptionUtil.showException("JS", e);
        }
        return "";
    }

    /**
     * 根据js action 的 key 找到 JSActionBean 对象
     * @param context
     * @param key
     * @return
     */
    public static JSActionConfigBean getJSActionBean(Activity context , String key) {
        init(context);
        if(jsActionConfigBeanList == null) return null;
        if(key == null) return null;
        for(JSActionConfigBean bean : jsActionConfigBeanList) {
            if(key.equals(bean.getKey())) {
                return bean;
            }
        }
        return null;
    }

}
