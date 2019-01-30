package com.honestwalker.androidutils.IO;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by lanzhe on 17-4-12.
 */

public class UriUtil {

    public static Uri fromFile(File file) {
        return Uri.fromFile(file);
    }


//    /**
//     * 获取文件URI， 预留android 7.0 Uri获取方式兼容
//     * @param context
//     * @param applicationId
//     * @param file
//     * @return
//     */
//    public static Uri fromFile(Context context, String applicationId, File file) {
////        Uri uri = null;
////        if (Build.VERSION.SDK_INT >= 24) {
////            LogCat.d("ImageSelector", "context=" + context + "  applicationId=" + applicationId + "  file=" + file);
////            uri = FileProvider.getUriForFile(context, applicationId, file);
////        } else {
////            uri = Uri.fromFile(file);
////        }
////        return uri;
//    }

}
