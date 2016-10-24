package com.honestwalker.android.commons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.Application;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.system.ProcessUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by honestwalker on 15-10-9.
 */
public class BaseApplication extends android.app.Application  {

    public static Context context = null;

    private static Map<Activity,Activity> signinstanceActivity = new HashMap<Activity,Activity>();

    public static String appVersion = "";
    public static String appName = "";

    /** 记录程序上一次所在的页面名称 */
    public static String lastPage = "";

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
        appVersion = Application.getAppVersion(context, R.class);

    }

    public static void addSingleInstanceActivity(Activity activity) {
        signinstanceActivity.put(activity, activity);
    }
    @SuppressLint("NewApi")
    public static void clearAllSigninstanceActivity() {
        Iterator<Map.Entry<Activity, Activity>> iter = signinstanceActivity.entrySet().iterator();
        while(iter.hasNext()) {
            Activity act = iter.next().getValue();
            try {
                LogCat.d("exit", act.getClass().getSimpleName());
                act.finish();
            } catch (Exception e) {
            }
        }
    }

    public static void exit() {
        Application.exit(context);
        System.exit(0);
        clearAllSigninstanceActivity();
    }

}
