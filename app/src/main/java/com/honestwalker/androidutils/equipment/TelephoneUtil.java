package com.honestwalker.androidutils.equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;
import com.honestwalker.androidutils.StringUtil;

public class TelephoneUtil {

    public TelephonyManager tm;
    private Context context;
    public static String deviceID;

    public static TelephoneUtil getInstance(Context context) {
        return new TelephoneUtil(context);
    }

    public TelephoneUtil(Context context) {
        this.context = context;
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取设备id
     *
     * @return
     */
    public String getDeviceId() {
        if (StringUtil.isEmptyOrNull(deviceID)) {   //判断内存中是否存在
            deviceID = tm.getDeviceId();
            if(StringUtil.isEmptyOrNull(deviceID)){ //判断获取到设备号是否为null
                deviceID = SharedPreferencesLoader.getString("deviceID");
                if(StringUtil.isEmptyOrNull(deviceID)){ //判断share里是否存在
                    deviceID = UUID.randomUUID().toString();
                    SharedPreferencesLoader.putString("deviceID",deviceID);
                }
            }
        }
        return deviceID;
    }

    /**
     * 判断某app是否存在
     *
     * @param packageName
     * @return
     */
    public Boolean existApp(String packageName) {
        PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
                if (pn.indexOf("map") > -1)
                    LogCat.d(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    public void vibrate() {
        vibrate(200);
    }

    public void vibrate(long time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    /**
     * 获取一些设备信息
     *
     * @return
     */
    public String getInfoDetail() {
        return "Model:" + getModel() + ";" +
                "NetworkType:" + getNetworkType() + ";" +
                "VersionRelease:" + getVersionRelease() + ";" +
                "exist SDCard:" + SDCardUtil.existsSDCard() + ";"/* +
               "device id:" + getDeviceId()*/;
    }

    /**
     * 获取品牌型号
     *
     * @return
     */
    public String getModel() {
        return android.os.Build.BRAND + " " + android.os.Build.MODEL;
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public String getNetworkType() {
        String networkType = "UNKNOWN";
        switch (getNetworkTypeCode()) {
            case 1:
                networkType = "GPRS";
                break;
            case 2:
                networkType = "EDGE";
                break;
            case 3:
                networkType = "UMTS";
                break;
            case 8:
                networkType = "HSDPA";
                break;
            case 9:
                networkType = "HSUPA";
                break;
            case 10:
                networkType = "HSPA";
                break;
            case 4:
                networkType = "CDMA";
                break;
            case 5:
                networkType = "EVDO_0";
                break;
            case 6:
                networkType = "EVDO_A";
                break;
            case 7:
                networkType = "1xRTT";
                break;
        }
        return networkType;
    }

    /**
     * 当前使用的网络类型：
     * 例如： NETWORK_TYPE_UNKNOWN  网络类型未知  0
     * NETWORK_TYPE_GPRS     GPRS网络  1
     * NETWORK_TYPE_EDGE     EDGE网络  2
     * NETWORK_TYPE_UMTS     UMTS网络  3
     * NETWORK_TYPE_HSDPA    HSDPA网络  8
     * NETWORK_TYPE_HSUPA    HSUPA网络  9
     * NETWORK_TYPE_HSPA     HSPA网络  10
     * NETWORK_TYPE_CDMA     CDMA网络,IS95A 或 IS95B.  4
     * NETWORK_TYPE_EVDO_0   EVDO网络, revision 0.  5
     * NETWORK_TYPE_EVDO_A   EVDO网络, revision A.  6
     * NETWORK_TYPE_1xRTT    1xRTT网络  7
     */
    public int getNetworkTypeCode() {
        return tm.getNetworkType();
    }

    /**
     * 获取android sdk版本号
     *
     * @return
     */
    public String getVersionRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    public void copy(String content) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

}
