package com.honestwalker.android.spring.exception;

/**
 * Created by lanzhe on 17-11-13.
 */
public class BeanRepeatException extends Exception {

    public BeanRepeatException(String beanName) {
        super("bean named : " + beanName + " 重复注入!!!!!!!!!!!!!!!!");
    }

    public BeanRepeatException(String beanName, String message) {
        super("bean named : " + beanName + " 重复注入!!!!!!!!!!!!!!!! " + message);
    }

}
