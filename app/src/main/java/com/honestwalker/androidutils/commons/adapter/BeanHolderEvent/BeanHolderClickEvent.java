package com.honestwalker.androidutils.commons.adapter.BeanHolderEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by honestwalker on 15-11-18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BeanHolderClickEvent {
    int id();
}
