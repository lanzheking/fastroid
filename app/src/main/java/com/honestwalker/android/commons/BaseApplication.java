package com.honestwalker.android.commons;

import android.content.Context;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.Application;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.system.ProcessUtil;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Application父类，包涵了一些核心功能的加载
 * Created by honestwalker on 15-10-9.
 */
public class BaseApplication extends android.app.Application  {

    public static Context context = null;

    public static String appVersion = "";
    public static String appName = "";

    /** 当前数据库版本 */
    private final int DATABASE_VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        initApp();

        LogCat.d("PROC", "启动进程：" + ProcessUtil.getMyPid() +
                " 进程名：" + ProcessUtil.getCurProcessName(getApplicationContext()));

    }

    /** 做一些app初始化工作 */
    private void initApp() {

        appName = getResources().getString(R.string.app_name);
        initHermesEventBus();

        UIHandler.init();

    }

    private void initHermesEventBus() {
        HermesEventBus.getDefault().init(this);
        if(!ProcessUtil.isMainProcess(this)) {
            // 非主进程调用该方法
            HermesEventBus.getDefault().connectApp(this, getPackageName());
        }
    }

    public static void exit() {
        Application.exit(context);
    }

}
