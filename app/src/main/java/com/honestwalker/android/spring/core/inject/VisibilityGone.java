package com.honestwalker.android.spring.core.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被添加此注释的View，默认将不显示，并设置Visibility为Gone
 * Created by lanzhe on 18-5-7.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VisibilityGone {
}
