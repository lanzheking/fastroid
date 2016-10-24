package com.honestwalker.android.commons.menu;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by honestwalker on 15-9-24.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FragmentMenu {
    /**
     *是否是首页
     */
    boolean isHome() default false;

    String menuText() default "";

    int unselectedRenderingIcon() default 0;

    int selectedRenderingIcon() default 0;
}
