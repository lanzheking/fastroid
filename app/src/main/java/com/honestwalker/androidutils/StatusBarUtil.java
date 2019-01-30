package com.honestwalker.androidutils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.githang.statusbar.StatusBarCompat;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtil {

    public static void setNavigationBarColor(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(color);
        }

    }

    public static void setStatusBarColor(Activity activity, int color, boolean lightStatusBar) {
        StatusBarCompat.setStatusBarColor(activity, color, lightStatusBar);
    }

    private static boolean isMIUI() {
        try {
            Class c  = Class.forName("android.os.SystemProperties");
            Method m = c.getDeclaredMethod("get", String.class);
            m.setAccessible(true);
            String miuiVersionName = m.invoke(c, "ro.miui.ui.version.name") + "";
            LogCat.d("COLOR", "versionName=" + miuiVersionName);
            String miuiVersionCode = miuiVersionName.substring(1);
            if(Integer.parseInt(miuiVersionCode) >= 6) return true;
        } catch (Exception e) {
            ExceptionUtil.showException("COLOR", e);
        }
        return false;
    }

}
