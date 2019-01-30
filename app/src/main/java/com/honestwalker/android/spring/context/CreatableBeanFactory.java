package com.honestwalker.android.spring.context;

import com.honestwalker.android.spring.core.bean.SpringBean;
import com.honestwalker.android.spring.exception.BeanRepeatException;

interface CreatableBeanFactory extends BeanFactory {

    void putSingletonBeanMapping(String id, Object bean) throws BeanRepeatException;

    void putBeanMapping(String id, SpringBean springBean) throws BeanRepeatException;

}
