package com.honestwalker.android.commons.jscallback.bean;

/**
 * exit 类型的参数实体对象。
 * animated	是否以动画形式退出当前页	true
 */
public class ExitParamBean extends JSActionParamBean {

    private boolean animated;

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }
}
