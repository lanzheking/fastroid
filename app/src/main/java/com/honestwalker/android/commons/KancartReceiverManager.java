package com.honestwalker.android.commons;

/**
 * Created by honestwalker on 15-10-9.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.honestwalker.android.fastroid.R;


/** 广播管理器，仅用于App内部广播，目的是为了代替观测者模式，它可以支持多进程 */
public class KancartReceiverManager {

    public static enum Action {

        ACTION_LOGOUT ,
        ACTION_FINISH_MENUACTIVITY ,
        ACTION_SELECTED_DECORATE ,
        ACTION_WEB_ERROR ,
        ACTION_CLOSE_LOADING ,
        ACTION_CLOSE_HOMEPAGER ,
        ACTION_CLOSE_ACCOUNTBIND ,
        ACTION_CLOSE_EDITPAGE ,
        ACTION_ADD_IMAGE,

        ACTION_REMOVE_IMAGE,


        /** 去商场活动广播 */
        ACTION_TO_Market_ACTIVITY ,
        /** 去商户活动广播 */
        ACTION_TO_STORE_ACTIVITY ,
        /** 去商店详情广播 */
        ACTION_TO_STORE_DETAIL ,
        /** 去搜索页面广播 */
        ACTION_TO_SEARCH ,

        /** 关闭欢迎页面通知 */
        ACTION_CLOSE_WELCOME ,

        /** 装修阶段刷新广播 */
        ACTION_REFRESH_DECORATION_STAGE,

        /** 改变首页tab广播 */
        ACTION_CHANGE_HOME_TABBAR,

        /** 在首页显示后退按钮 */
        ACTION_HOME_SHOW_BACK,

        /*房主选择入住时间*/
        ACTION_INTO_TIME,

        /*房主选择退房时间*/
        ACTION_OUT_TIME,


        /*是否接待外宾*/

        ACTION_RECEPTION,
    }

    /**
     * 注册广播接收器
     * @param context
     * @param action
     * @param receiver
     */
    public static void registerReceiver (Context context, String action,
                                        BroadcastReceiver receiver) {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(R.class.getPackage().getName()
                + action);
        context.registerReceiver(receiver, myIntentFilter);
    }
    /**
     * 注册有序广播接收器
     * @param priority 广播接收器的优先级
     */
    public static void registerReceiver(Context context, Action action,int priority,
                                        BroadcastReceiver receiver) {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(R.class.getPackage().getName()
                + action.toString());
        myIntentFilter.setPriority(priority);
        context.registerReceiver(receiver, myIntentFilter);
    }

    public static void unregisterReceiver(Context context,
                                          BroadcastReceiver receiver) {
        try {
            context.unregisterReceiver(receiver);
        } catch (Exception e) {
        }
    }

    /** 发送广播 */
    public static void sendBroadcast(Context context, Action action,
                                     Intent intent) {
        if (intent == null) {
            intent = new Intent(R.class.getPackage().getName()
                    + action.toString());
        } else {
            intent.setAction(R.class.getPackage().getName() + action.toString());
        }
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, Action action){
        sendBroadcast(context, action, null);
    }

    /** 发送动态广播 */
    public static void sendBroadcase(Context context, Action action) {
        sendBroadcast(context, action, null);
    }

    public static void sendBroadcase(Context context , String action) {
        sendBroadcase(context, action, null);
    }

    public static void sendBroadcase(Context context , String action , Intent intent) {
        if (intent == null) {
            intent = new Intent(R.class.getPackage().getName() + action);
        } else {
            intent.setAction(R.class.getPackage().getName() + action);
        }
        context.sendBroadcast(intent);
    }

    public static void sendBroadcase(Context context , Action action , Intent intent) {
        if (intent == null) {
            intent = new Intent(R.class.getPackage().getName() + action);
        } else {
            intent.setAction(R.class.getPackage().getName() + action);
        }
        context.sendBroadcast(intent);
    }

    /**
     *	发送有序动态广播
     */
    public static void sendOrderedBroadcast(Context context, Action action,Intent intent){
        if (intent == null) {
            intent = new Intent(R.class.getPackage().getName()
                    + action.toString());
        } else {
            intent.setAction(R.class.getPackage().getName() + action.toString());
        }
        context.sendOrderedBroadcast(intent, null);
    }
    public static void sendOrderedBroadcast(Context context, Action action) {
        sendOrderedBroadcast(context, action, null);
    }
}
