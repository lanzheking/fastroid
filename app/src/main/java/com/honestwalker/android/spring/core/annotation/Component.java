package com.honestwalker.android.spring.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lanzhe on 17-7-24.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
//    Scope scope() default Scope.prototype;
    /** 设置非懒加载，自定成为单例模式，即scope=Scope.singleton */
//    boolean lazyInit() default true;
}
