package com.honestwalker.android.spring.exception;

/**
 * Created by lanzhe on 17-11-13.
 */
public class BeanNotFoundException extends Exception {

    public BeanNotFoundException(String beanName) {
        super("找不到bean named : " + beanName);
    }

    public BeanNotFoundException(String beanName, String message) {
        super("找不到bean named : " + beanName + ". " + message);
    }

}
