package com.honestwalker.android.spring.context;

/**
 * Created by lanzhe on 17-8-31.
 */
public interface ApplicationContext extends CreatableBeanFactory {

    BeanFactory getBeanFactory() throws IllegalStateException;

}