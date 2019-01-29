package com.honestwalker.android.share;


import com.honestwalker.androidutils.IO.LogCat;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * ShareSDK分享工具类
 * Created by lanzhe on 16-8-4.
 */
public class ShareUtil {

   /* public void share(String platform , Platform.ShareParams shareParams, PlatformActionListener listener) {
        Platform mPlatform = ShareSDK.getPlatform (Wechat.NAME);
        if(platform == null) return;
        mPlatform. setPlatformActionListener (listener);
        mPlatform.share(shareParams);
    }*/

    public Platform login(String platform, boolean getUserInfo, PlatformActionListener listener) {
        Platform platformBean = ShareSDK.getPlatform(platform);

        if(platformBean.isAuthValid()) {
            LogCat.d("share", platform + " 已经授权");
        } else {
            LogCat.d("share", platform + " 没有授权");
        }

        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        platformBean.setPlatformActionListener(listener);

        if(getUserInfo) {
            platformBean.authorize();          //单独授权,OnComplete返回的hashmap是空的
        } else {
            platformBean.showUser(null);       //授权并获取用户信息
        }
        return platformBean;
    }

    public void removeAccount(Platform platformBean) {
        platformBean.removeAccount(true);
    }

}
