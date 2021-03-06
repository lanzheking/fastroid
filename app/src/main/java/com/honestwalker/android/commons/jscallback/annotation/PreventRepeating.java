package com.honestwalker.android.commons.jscallback.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止JS API连续执行，一般用于防止前端连点击触发
 * Created by lanzhe on 17-10-17.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventRepeating {

    /** 间隔时间， 小于这个时间，视为连续触发 */
    int value() default 400;

}
