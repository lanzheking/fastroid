package com.honestwalker.androidutils.views;

import android.content.Context;
import android.util.AttributeSet;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.honestwalker.androidutils.ui.DesignUtil;

/**
 * Depiction:
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-3-4 下午1:40. <br />
 * Rewrite Date :  16-3-4 下午1:40. <br />
 */
public class LineView extends BaseMyViewLinearLayout {

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        ViewSizeHelper.getInstance(context).setSize(this, screenWidth , DisplayUtil.dip2px(context , 1));
    }

    @Override
    protected int contentViewLayout() {
        return R.layout.view_arrow;
    }
}
