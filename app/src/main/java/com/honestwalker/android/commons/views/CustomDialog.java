package com.honestwalker.android.commons.views;

import android.app.Dialog;
import android.content.Context;

import com.honestwalker.android.fastroid.R;


/**
 * Depiction:
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-2-18 下午5:58. <br />
 * Rewrite Date :  16-2-18 下午5:58. <br />
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, R.style.CustomDialog);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
