package com.honestwalker.android.spring.context;

import com.honestwalker.android.spring.core.bean.SpringBean;
import com.honestwalker.android.spring.exception.BeanRepeatException;

public interface BeanFactory {

    Object getBean(String id);

    <T> T getBean(String id, Class<T> type);

    boolean containsBean(String id);

    SpringBean getSpringBean(String id);

    void registerSpringBean(String id, Class beanType) throws BeanRepeatException;

    void registerSingleton(String id, Object bean) throws BeanRepeatException ;

}
