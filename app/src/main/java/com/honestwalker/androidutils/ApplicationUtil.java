package com.honestwalker.androidutils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.honestwalker.androidutils.exception.ExceptionUtil;

/**
 * Created by lanzhe on 18-4-25.
 */

public class ApplicationUtil {

    /**
     * 获取Application中的meta-data.
     * @return
     */
    public static String getApplicationMetadata(Context context, String key) {
        Bundle bundle = getAppMetaDataBundle(context.getPackageManager(), context.getPackageName());
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }
        return null;
    }

    private static Bundle getAppMetaDataBundle(PackageManager packageManager,
                                        String packageName) {
        Bundle bundle = null;
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            ExceptionUtil.showException(e);
        }
        return bundle;
    }

    /**
     * 获取Activity metadata值
     * @param activity
     * @return
     */
    public static String getActivityMetadata(Activity activity, String key) {
        try {
            ActivityInfo info = activity.getPackageManager()
                    .getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            String msg = info.metaData.getString(key);
            return msg;
        } catch (PackageManager.NameNotFoundException e) {
            ExceptionUtil.showException(e);
        }
        return null;
    }

}
