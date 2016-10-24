package com.honestwalker.androidutils.commons.adapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by honestwalker on 15-11-18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface BeanHolder {
    int id();
    int viewWidth() default -1;
    int viewHeight() default -1;

    /** 相对屏幕宽度比例 */
    float scaleViewWidth() default -1;

    /** 宽高比  (高除宽的值) */
    float scaleSize() default -1;

}
