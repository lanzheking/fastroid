package com.honestwalker.android.kc_commons.ui.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.systembartint.SystemBarTintManager;

/**
 * Created by honestwalker on 15-9-24.
 */
public class TranslucentStatus {

    private static boolean translucentStatus = false;

    public static boolean isEnable() {
        return translucentStatus;
    }

    public static void setDisable(Activity context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(context, false);
//        }
        if(support() || !translucentStatus) return;

        setTranslucentStatus(context, false);

        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(false);
        tintManager.setNavigationBarTintEnabled(false);
    }

    public static void setEnable(Activity context) {

        if(!support()) return;

        // 旧的实现方法
//        activity.getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // 新的实现方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(context, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(false);

    }

    public static void setColor(Activity context, String color) {
        if(!support()) return;

        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

//        tintManager.setTintColor(Color.parseColor(color));

        // 设置一个样式背景给导航栏
        tintManager.setNavigationBarTintResource(R.color.green);

        // 设置一个状态栏资源
//        tintManager.setStatusBarTintDrawable(MyDrawable);
    }

    public static void setColor(Activity context, int color) {
        setColor(context, color, null);
    }

    /**
     * 设置统治栏颜色
     * @param context
     * @param color
     * @param systemBarSpaceView 统治栏高度占位符
     */
    public static void setColor(Activity context, int color, View systemBarSpaceView) {

        if(!support()) return;

        setEnable(context);

        if(systemBarSpaceView != null) {
            ViewSizeHelper.getInstance(context).setHeight(systemBarSpaceView, DisplayUtil.getStatusBarHeight(context));
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(color);

//        tintManager.setTintColor(Color.parseColor("#99000FF"));
        // 设置一个样式背景给导航栏
//        tintManager.setNavigationBarTintResource(R.drawable.albums_bg);
        // 设置一个状态栏资源
//        tintManager.setStatusBarTintDrawable(MyDrawable);
    }

    @TargetApi(19)
    protected static void setTranslucentStatus(Activity context, boolean on) {

//        context.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

//        Window window = context.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);
//        window.setNavigationBarColor(Color.TRANSPARENT);

        translucentStatus = on;
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
        win.setStatusBarColor(Color.TRANSPARENT);
        win.setNavigationBarColor(Color.TRANSPARENT);
    }

    public static boolean support() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return false;
        }
    }

}
