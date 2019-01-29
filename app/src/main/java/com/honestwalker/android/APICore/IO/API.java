package com.honestwalker.android.APICore.IO;


import com.honestwalker.android.APICore.API.ParseStrategy.ParseStrategy;

import org.xutils.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface API {

    String host() default "";

    String uri() default "";

    String action_key() default "method";

    String value() default "";

    Class<? extends ParseStrategy>[] parseStrategies() default {};

    /** 标识返回数据类型，有Json和字符串，如果是一般字符串，会自动转换成json格式 {"result":"success" , "info":{"字符串"} */
    ResponseMethod responseMethod() default ResponseMethod.JSON;

    HttpMethod requestMethod() default HttpMethod.POST;

    boolean handleErrorPersonally() default false;

}
