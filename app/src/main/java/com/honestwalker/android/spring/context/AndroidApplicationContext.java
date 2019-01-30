package com.honestwalker.android.spring.context;

import com.honestwalker.android.spring.core.bean.SpringBean;
import com.honestwalker.android.spring.exception.BeanRepeatException;

/**
 * Created by lanzhe on 17-8-31.
 */
class AndroidApplicationContext implements ApplicationContext, BeanFactory {

    private BeanFactory beanFactory = new AndroidBeanFactory();

    @Override
    public Object getBean(String id) {
        return beanFactory.getBean(id);
    }

    @Override
    public <T> T getBean(String id, Class<T> type) {
        return beanFactory.getBean(id, type);
    }

    @Override
    public boolean containsBean(String id) {
        return beanFactory.containsBean(id);
    }

    @Override
    public void putSingletonBeanMapping(String id, Object bean) throws BeanRepeatException {
        ((CreatableBeanFactory)beanFactory).putSingletonBeanMapping(id, bean);
    }

    @Override
    public void putBeanMapping(String id, SpringBean springBean) throws BeanRepeatException {
        ((CreatableBeanFactory)beanFactory).putBeanMapping(id, springBean);
    }

    @Override
    public SpringBean getSpringBean(String id) {
        return beanFactory.getSpringBean(id);
    }

    @Override
    public void registerSpringBean(String id, Class beanType) throws BeanRepeatException {
        beanFactory.registerSpringBean(id, beanType);
    }

    @Override
    public void registerSingleton(String id, Object bean) throws BeanRepeatException {
        beanFactory.registerSingleton(id, bean);
    }

    @Override
    public BeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }
}
