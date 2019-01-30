package com.honestwalker.androidutils.Permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理工具
 * Created by lanzhe on 17-5-23.
 */
public class PermissionManager {

    /** 摄像头权限 */
    public final static String[] CAMERA_PERMISSION = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA"};

    /** 存储权限 */
    public final static String[] EXTRNAL_STORAGE_PERMISSION = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    /** 设备信息权限 */
    public final static String[] READ_PHONE_STATE_PERMISSION = {
            "android.permission.READ_PHONE_STATE"
    };

    /** 录音权限 */
    public final static String[] RECORD_AUDIO_PERMISSION = {
            "android.permission.RECORD_AUDIO"
    };

    public final int REQUEST_CAMERA_PERMISSION = 5001;
    public final int REQUEST_EXTRNAL_STORAGE_PERMISSION = 5002;
    public final int REQUEST_READ_PHONE_STATE_PERMISSION = 5003;
    public final int REQUEST_RECORD_AUDIO_PERMISSION = 5004;

    /**
     * 是否包含指定权限
     * @param permissionKey
     * @return
     */
    public boolean hasPermission(Context context, @NonNull String[] permissionKey) {
        for (String permission : permissionKey) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测权限
     * @param permissionName
     * @return
     */
    private boolean checkPermission(Context context, @NonNull String permissionName) {
        return ActivityCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否包含读写权限
     * @return
     */
    public boolean hasExtrnalStoragePermission(Context context) {
        return hasPermission(context, EXTRNAL_STORAGE_PERMISSION);
    }

    /**
     * 请求磁盘读写权限
     */
    public void requestExtrnalStoragePermission(Activity activity) {
        requestPermissions(activity, EXTRNAL_STORAGE_PERMISSION , REQUEST_EXTRNAL_STORAGE_PERMISSION);
    }

    /**
     * 检测是否有相机权限
     * @return
     */
    public boolean hasCameraPermission(Context context) {
        return hasPermission(context, CAMERA_PERMISSION);
    }

    /**
     * 请求相继权限
     */
    public void requestCameraPermission(Activity activity) {
        requestPermissions(activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.CAMERA"} , REQUEST_CAMERA_PERMISSION);
    }

    /**
     * 是否有录音权限
     * @return
     */
    public boolean hasAudioRecordPermission(Context context) {
        return hasPermission(context, RECORD_AUDIO_PERMISSION);
    }

    /**
     * 申请录音权限
     */
    public void requestAudioRecordPermission(Activity activity) {
        requestPermissions(activity, RECORD_AUDIO_PERMISSION, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    /**
     * 是否有获取设备信息权限
     * @return
     */
    public boolean hasReadPhoneStatePermission(Context context) {
        return hasPermission(context, READ_PHONE_STATE_PERMISSION);
    }

    /**
     * 获取设备信息权限
     */
    public void requestReadPhoneStatePermission(Activity activity) {
        requestPermissions(activity, READ_PHONE_STATE_PERMISSION, REQUEST_READ_PHONE_STATE_PERMISSION);
    }

    /**
     * 请求权限
     * @param permissions
     * @param requestCode
     */
    public void requestPermissions(Activity activity, @NonNull String[] permissions, int requestCode) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            context.requestPermissions(permissions, requestCode);
//        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 请求权限
     * @param permissions
     * @param requestCode
     */
    public void requestPermissions(Activity activity, int requestCode, @NonNull String[]... permissions) {
        if(permissions == null || permissions.length == 0) return;
        List<String> permissionList = new ArrayList<>();
        for (String[] permission : permissions) {
            for (String p : permission) {
                permissionList.add(p);
            }
        }
        ActivityCompat.requestPermissions(activity,
                permissionList.toArray(new String[permissionList.size()]), requestCode);
    }

}
