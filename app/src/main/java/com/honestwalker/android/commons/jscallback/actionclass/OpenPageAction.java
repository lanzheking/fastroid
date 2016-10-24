package com.honestwalker.android.commons.jscallback.actionclass;

import android.app.Activity;

import com.honestwalker.android.commons.jscallback.bean.OpenPageParamBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;

/**
 * OpenUrl类型的 js callback 业务实现
 * Created by honestwalker on 15-6-2.
 */
public class OpenPageAction extends JSCallbackAction<OpenPageParamBean> {

    private final String STORE_LIST = "storeList";
    private final String ACTIVITY_LIST = "activityList";

    @Override
    protected void doAction(Activity context , OpenPageParamBean paramBean , HtmlWebView webView) {

//        String name = paramBean.getName();
//        String title = paramBean.getTitle();
//
//        if(STORE_LIST.equals(name)) {
//            WebActivityEntry.toWebActivity(context, WebConstant.UrlStoreList() , title == null ? "店铺列表" : title);
//        } else if(ACTIVITY_LIST.equals(name)) {
//            Intent intent = new Intent();
//            intent.putExtra("tab_index" , 3);
//            KancartReceiverManager.sendBroadcast(context , KancartReceiverManager.Action.ACTION_CHANGE_HOME_TABBAR , intent);
//        }

    }

}
