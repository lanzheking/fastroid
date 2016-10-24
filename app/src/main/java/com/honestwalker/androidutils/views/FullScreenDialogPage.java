package com.honestwalker.androidutils.views;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

/**
 * Depiction:
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-2-20 下午2:51. <br />
 * Rewrite Date :  16-2-20 下午2:51. <br />
 */
public abstract class FullScreenDialogPage extends DialogPage {

    public FullScreenDialogPage(Activity context, int resLayoutId) {
        super(context, resLayoutId, android.R.style.Theme_Translucent_NoTitleBar);
        super.getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public FullScreenDialogPage(Activity context, int resLayoutId , int style) {
        super(context, resLayoutId, style);
        super.getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }


}