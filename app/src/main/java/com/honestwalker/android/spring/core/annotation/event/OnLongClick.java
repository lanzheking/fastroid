package com.honestwalker.android.spring.core.annotation.event;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @OnLongClick(R.id.btn1)
 * private boolean btnLongClick(View view) {
 * btnOnClick(view);
 * return false;
 * }
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(
        listenerType = View.OnLongClickListener.class,
        listenerSetter = "setOnLongClickListener",
        methodName = "onLongClick"
)
public @interface OnLongClick {
    int[] value();

    int[] parentId() default {0};
}
