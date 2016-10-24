package com.honestwalker.android.commons.jscallback;

import android.app.Activity;

import com.google.gson.Gson;
import com.honestwalker.android.commons.jscallback.actionclass.JSCallbackAction;
import com.honestwalker.android.commons.jscallback.bean.JSActionConfigBean;
import com.honestwalker.android.commons.jscallback.bean.JSActionParamBean;
import com.honestwalker.android.commons.jscallback.io.ConfigLoader;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;

import java.util.ArrayList;

/**
 * JSAction 处理器，
 * 1. 解析配置 webview_js_callback 配置
 * 2. 根据action寻找指定业务实现jscallback action 并执行业务实现。
 * Created by honestwalker on 15-6-2.
 */
public class JSActionExecutor {

    public static ArrayList<JSActionConfigBean> jsActionConfigBeanList;

    public static void init(Activity context) {
        if(jsActionConfigBeanList == null || jsActionConfigBeanList.size() == 0) {
            try {
                jsActionConfigBeanList = ConfigLoader.loadConfig(context);
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
    public static void execute(Activity context , HtmlWebView webView, String json) {
        init(context);

        // 根据action 得到响应的 JSActionConfigBean 对象
        JSActionParamBean actionBean = new Gson().fromJson(json , JSActionParamBean.class);
        JSActionConfigBean jsActionConfigBean = getJSActionBean(context, actionBean.getAction());

        if(jsActionConfigBean != null) {    // jsActionConfigBean 对象存在继续处理
            try {

                // 获取实现具体业务的JSCallbackAction对象
                JSCallbackAction jsCallbackAction = (JSCallbackAction) jsActionConfigBean.getClazz().newInstance();
                jsCallbackAction.setParamJson(json);

                try {
                    jsCallbackAction.execute(context , jsActionConfigBean , json , webView);
                } catch (Exception e) {
                }

            } catch (Exception e) {
            }

        } else {
        }
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
