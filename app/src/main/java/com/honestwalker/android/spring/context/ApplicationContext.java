package com.honestwalker.android.spring.context;

import com.honestwalker.android.spring.core.bean.Scope;
import com.honestwalker.android.spring.core.bean.SpringBean;
import com.honestwalker.androidutils.ClassUtil;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lanzhe on 17-8-31.
 */
public class ApplicationContext {

    private static HashMap<String, SpringBean> beanMapping = new HashMap<>();

    private static HashMap<String, Object> singletonBeanMapping = new HashMap<>();

    ApplicationContext() {
    }

    public static <T> T getBean(String name, Class<T> beanClass) {
        return (T)getBean(name);
    }

    public static Object getBean(String name) {

        if(singletonBeanMapping.get(name) != null) return singletonBeanMapping.get(name);

        LogCat.d("Spring", "beanMapping 长度 " + beanMapping.size());
        SpringBean springBean = beanMapping.get(name);
        if(springBean == null) return null;
        String springBeanClassPath = springBean.getClassPath();

        Object bean;
        try {
            bean = Class.forName(springBeanClassPath).newInstance();
        } catch (Exception e) {
            return null;
        }

        if(Scope.singleton.equals(springBean.getScope())) {
            singletonBeanMapping.put(name, bean);
        }

        autoWireBean(bean);

        if(springBean.getFieldValueMapping().size() > 0) {
            for (String fieldName : springBean.getFieldValueMapping().keySet()) {

                String fieldValue = springBean.getFieldValueMapping().get(fieldName);

                if(FieldType.value.equals(springBean.getFieldType().get(fieldName))) { // value方式赋值
                    if(fieldValue != null && fieldValue.trim().startsWith("#{") && fieldValue.endsWith("}")) {
                        String refBeanName = fieldValue.trim().substring(2);
                        refBeanName = refBeanName.substring(0, refBeanName.length() - 1);
                        Object refBean = getBean(refBeanName);
                        LogCat.d("config", "#{}查找 " + refBeanName + " refBean=" + refBean);
                        ClassUtil.setFieldValue(bean, fieldName, refBean);
                    } else {
                        ClassUtil.setFieldValue(bean, fieldName, fieldValue);
                    }
                } else if (FieldType.ref.equals(springBean.getFieldType().get(fieldName))) { //
                    Object refBean = getBean(fieldValue);
                    ClassUtil.setFieldValue(bean, fieldName, refBean);
                }

            }
        }
        return bean;
    }

    static void autoWireBean(Object object) {
        ApplicationContextUtils.inject(object);
    }

    static boolean containsBean(String name) {
        return beanMapping.containsKey(name);
    }

    static void putBeanMapping(String name, SpringBean springBean) {
        beanMapping.put(name, springBean);
        LogCat.d("Spring", "add spring bean name=" + name + "  " + springBean.getClassPath());
    }

    static void putSingletonBeanMapping(String name , Object bean) {
        singletonBeanMapping.put(name, bean);
    }

}