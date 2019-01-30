package com.honestwalker.android;

import com.honestwalker.android.spring.core.annotation.Component;
import com.honestwalker.android.spring.core.annotation.Scope;
import com.honestwalker.android.spring.core.bean.ScopeType;
import com.honestwalker.androidutils.equipment.SDCardUtil;

/**
 * Created by lanzhe on 18-4-27.
 */
@Component
@Scope(ScopeType.singleton)
public class BasicConfig {

    private static String cacheDir;

    /**
     * 获取SD卡缓存路径
     * @return
     */
    public static String getSDCardCacheDir() {
        return SDCardUtil.getSDRootPath() + cacheDir + "/";
    }

}
