package com.honestwalker.android.spring.context;

import com.honestwalker.android.spring.core.bean.ConstructorArg;
import com.honestwalker.android.spring.core.bean.ScopeType;
import com.honestwalker.android.spring.core.bean.SpringBean;
import com.honestwalker.android.spring.exception.BeanRepeatException;
import com.honestwalker.androidutils.ClassUtil;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final class AndroidBeanFactory implements BeanFactory, CreatableBeanFactory {

    private HashMap<String, SpringBean> beanMapping = new HashMap<>();

    private HashMap<String, Object> singletonBeanMapping = new HashMap<>();

    public <T> T getBean(String id, Class<T> beanClass) {
        return (T)getBean(id);
    }

    public List<String> getBeanIds() {
        List<String> beans = new ArrayList<>();
        for (String s : beanMapping.keySet()) {
            beans.add(s);
        }
        for (String s : singletonBeanMapping.keySet()) {
            beans.add(s);
        }
        return beans;
    }

    public Object getBean(String id) {

        if(singletonBeanMapping.get(id) != null) return singletonBeanMapping.get(id);

        if(!beanMapping.containsKey(id)) return null;

        SpringBean springBean = beanMapping.get(id);
        if(springBean == null) return null;

        String springBeanClassPath = springBean.getClassPath();

        Object bean = null;
        try {
            Class springBeanClass = Class.forName(springBeanClassPath);

            if(springBean.getConstructorArgMapping() != null && springBean.getConstructorArgMapping().size() > 0) {
                int constructorArgSize = springBean.getConstructorArgMapping().size();
                Class[] constructorArgType = new Class[constructorArgSize];
                Object[] constructorValues = new Object[constructorArgSize];
                for (int i = 0; i < constructorArgSize; i++) {
                    ConstructorArg constructorArg = springBean.getConstructorArgMapping().get(i + "");
                    if(FieldType.ref.equals(constructorArg.getFieldType())) {
                        Object refBean = getBean(constructorArg.getValue());
                        constructorArgType[i] = refBean.getClass();
                        constructorValues[i] = refBean;
                    } else if(FieldType.value.equals(constructorArg.getFieldType())) {
                        constructorArgType[i] = constructorArg.getType();
                        constructorValues[i] = ClassUtil.getValueByType(constructorArg.getValue(), constructorArgType[i]);
                    }
                }
                Constructor constructor = springBeanClass.getDeclaredConstructor(constructorArgType);
                constructor.setAccessible(true);
                bean = constructor.newInstance(constructorValues);
            } else {
                bean = springBeanClass.newInstance();
            }

        } catch (Exception e) {
            ExceptionUtil.showException("Spring", e);
            return null;
        }

        if(ScopeType.singleton.equals(springBean.getScope())) {
            singletonBeanMapping.put(id, bean);
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

    void autoWireBean(Object object) {
        ApplicationContextUtils.inject(object);
    }

    public boolean containsBean(String id) {
        return beanMapping.containsKey(id) || singletonBeanMapping.containsKey(id);
    }

    public SpringBean getSpringBean(String id) {
        return beanMapping.get(id);
    }

    HashMap<String, SpringBean> getSpringBeans() {
        return beanMapping;
    }

    void setSpringBeans(HashMap<String, SpringBean> beanMapping) {
        this.beanMapping = beanMapping;
    }

    @Override
    public void registerSingleton(String id, Object bean) throws BeanRepeatException {
        if(singletonBeanMapping.containsKey(id)) {
            LogCat.d("Spring","重复添加Bean " + id);
            throw new BeanRepeatException(id);
        }
        putSingletonBeanMapping(id, bean);
    }

    @Override
    public void registerSpringBean(String id, Class beanType) throws BeanRepeatException {
        SpringBean springBean = new SpringBean();
        springBean.setClassPath(beanType.getName());
        springBean.setScope(ScopeType.prototype);
        springBean.setLazyInit(true);
        putBeanMapping(id, springBean);
    }

    public void putBeanMapping(String id, SpringBean springBean) throws BeanRepeatException {
        if(beanMapping.containsKey(id)) {
            LogCat.d("Spring","重复添加Bean " + id);
            throw new BeanRepeatException(id);
        }

        beanMapping.put(id, springBean);
        LogCat.d("Spring", "添加 spring bean id=" + id + "  " + springBean.getClassPath());
    }

    public void putSingletonBeanMapping(String id , Object bean) throws BeanRepeatException {
        if(singletonBeanMapping.containsKey(id)) {
            LogCat.d("Spring","重复添加Bean " + id);
            throw new BeanRepeatException(id);
        }
        singletonBeanMapping.put(id, bean);
    }

}
