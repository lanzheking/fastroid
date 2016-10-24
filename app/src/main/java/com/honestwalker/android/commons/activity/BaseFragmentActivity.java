package com.honestwalker.android.commons.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.kc_commons.ui.utils.TranslucentStatus;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.systembartint.SystemBarTintManager;

/**
 * FragmentActivity父类
 * Created by honestwalker on 15-9-24.
 */
public class BaseFragmentActivity extends FragmentActivity {

    protected FragmentActivity context;

    protected int screenWidth;
    protected int screenHeight;
    protected int statusBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TranslucentStatus.setEnable(this);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.none);


        this.context = this;
        init();
    }

    private void init() {
        this.screenWidth = DisplayUtil.getWidth(context);
        this.screenHeight = DisplayUtil.getHeight(context);
        this.statusBarHeight = DisplayUtil.getStatusBarHeight(context);
    }

}
