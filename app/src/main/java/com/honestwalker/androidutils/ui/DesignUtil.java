package com.honestwalker.androidutils.ui;

import android.content.Context;

import com.honestwalker.androidutils.equipment.DisplayUtil;

/**
 * Depiction:    设计图工具， 根据设计图比例得到相应尺寸
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-2-19 上午10:10. <br />
 * Rewrite Date :  16-2-19 上午10:10. <br />
 */
public class DesignUtil {

    private int screenWidth;

    private int designWidth = 640;

    public DesignUtil(Context context) {
        this.screenWidth = DisplayUtil.getWidth(context);
    }

    /**
     * @param designWidth 设计图宽度
     */
    public DesignUtil(Context context , int designWidth) {
        this.screenWidth = DisplayUtil.getWidth(context);
        this.designWidth = designWidth;
    }

    /**
     * 计算设计图控件比例宽度
     * @param scaleDesignWidth 控件在设计图宽度
     * @return 控件在屏幕的宽度
     */
    public float getWidth(float scaleDesignWidth) {
        return (scaleDesignWidth / (float)designWidth) * screenWidth;
    }

    /**
     * 计算设计图控件比例宽度
     * @param scaleDesignWidth 控件在设计图宽度
     * @return 控件在屏幕的宽度
     */
    public float getWidth(int scaleDesignWidth) {
        return ((float)scaleDesignWidth / (float)designWidth) * screenWidth;
    }

    /**
     * 计算设计图控件比例尺寸
     * @param scaleDesignWidth 控件在设计图宽度
     * @return 控件在屏幕的宽度
     */
    public Size scaleDesignSize(int scaleDesignWidth , int viewScaleWidth , int viewScaleHeight) {
        float viewWidth = getWidth(scaleDesignWidth);   // 控件实际宽度
        float viewHeight = viewWidth * (float)viewScaleHeight / (float)viewScaleWidth;
        Size size = new Size((int)viewWidth , (int)viewHeight);
        return size;
    }

    /**
     * 根据相对控件计算高度
     * 如果已一个控件的实际高度，可以根据他计算另一个控件应该现实的高度。
     * 需要三个参数 ， 待计算控件在设计图的高度， 相对控件的实际高度， 相对控件在设计图的实际高度
     * @param scalesHeight            设计图上的高度
     * @param relativeViewRealHeight  相对控件的实际高度
     * @param relativeViewScaleHeight 相对控件在设计图的高度
     * @return
     */
    public float getHeightByRelativeView(int scalesHeight,int relativeViewRealHeight , int relativeViewScaleHeight) {
        return (float)scalesHeight * (float)relativeViewRealHeight / (float)relativeViewScaleHeight;
    }

}
