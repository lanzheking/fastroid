package com.honestwalker.android.kancart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by honestwalker on 15-12-14.
 */
public class SpinnerWhithTitle extends Spinner {


    public SpinnerWhithTitle(Context context) {
        super(context);
    }

    public SpinnerWhithTitle(Context context, int mode) {
        super(context, mode);
    }

    public SpinnerWhithTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerWhithTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpinnerWhithTitle(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpinnerWhithTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
