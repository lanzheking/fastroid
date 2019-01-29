package com.honestwalker.androidutils.Permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * 权限管理工具
 * Created by lanzhe on 17-5-23.
 */
public class PermissionManager {

    public final int REQUEST_CAMERA_PERMISSION = 5001;
    public final int REQUEST_EXTRNAL_STORAGE_PERMISSION = 5002;

    private Activity context;
    private String applicationId;

    public PermissionManager(Activity context, String applicationId) {
        this.context = context;
        this.applicationId = applicationId;
    }

    /**
     * 是否包含指定权限
     * @param permissionKey
     * @return
     */
    public boolean hasPermission(@NonNull String[] permissionKey) {
        for (String permission : permissionKey) {
            if (!checkPermission(permission)) {
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
    private boolean checkPermission(@NonNull String permissionName) {
        PackageManager packageManager = context.getPackageManager();
        int permission = packageManager.checkPermission(permissionName, applicationId);
        if(PackageManager.PERMISSION_GRANTED != permission){
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否包含存储权限
     * @return
     */
    public boolean hasExtrnalStoragePermission() {
        return hasPermission(new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                             "android.permission.WRITE_EXTERNAL_STORAGE"});
    }

    /**
     * 请求相继权限
     */
    public void requestExtrnalStoragePermission() {
        requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"} , REQUEST_EXTRNAL_STORAGE_PERMISSION);
    }

    /**
     * 检测是否有相机权限
     * @return
     */
    public boolean hasCameraPermission() {
        return hasPermission(new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.CAMERA"});
    }

    /**
     * 请求相继权限
     */
    public void requestCameraPermission() {
        requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.CAMERA"} , REQUEST_CAMERA_PERMISSION);
    }

    /**
     * 请求权限
     * @param permissions
     * @param requestCode
     */
    public void requestPermissions(@NonNull String[] permissions, int requestCode) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            context.requestPermissions(permissions, requestCode);
//        }
        ActivityCompat.requestPermissions(context, permissions, requestCode);
    }

}
