package com.honestwalker.androidutils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.ui.DesignUtil;

/**
 * Depiction:
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-3-4 下午1:40. <br />
 * Rewrite Date :  16-3-4 下午1:40. <br />
 */
public class ArrowView extends BaseMyViewLinearLayout {

    private ImageView arrowIV;

    public ArrowView(Context context) {
        super(context);
    }

    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArrowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {

        arrowIV = (ImageView) getContentView().findViewById(R.id.array_right_view);

        DesignUtil designUtil = new DesignUtil(context);
        ViewSizeHelper.getInstance(context).setWidth(this ,
                (int)designUtil.getWidth(getResources().getInteger(R.integer.arrow_scaleX)) , 20 , 40);
        ViewSizeHelper.getInstance(context).setWidth(arrowIV,
                (int) designUtil.getWidth(getResources().getInteger(R.integer.arrow_scaleX)), 20, 40);
    }

    @Override
    protected int contentViewLayout() {
        return R.layout.view_arrow;
    }
}
