package com.honestwalker.android.spring.core.annotation;

import com.honestwalker.android.spring.core.bean.ScopeType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记bean scope , 优先度大于xml bean配置的scope， bean scope xml和注解同时存在时， 此Scope生效
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    ScopeType value() default ScopeType.prototype;

}
