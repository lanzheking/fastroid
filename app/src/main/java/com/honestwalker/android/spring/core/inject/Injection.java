package com.honestwalker.android.spring.core.inject;

import android.app.Activity;
import android.view.View;

import com.honestwalker.androidutils.IO.LogCat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanzhe on 17-7-24.
 */

public class Injection {

    public static void inject(Object ownerObject, Activity activity, View contentView) {
        if(ownerObject == null) return;

        // 加载ViewInject
        Class currentClass = ownerObject.getClass();
        List<Field> fieldList = new ArrayList<>();
        List<String> fieldNameList = new ArrayList<>();
        while(Object.class != currentClass) {
            Field[] fields = currentClass.getDeclaredFields();
            if(fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if(fieldNameList.contains(field.getName())) continue;
                    fieldList.add(field);
                    fieldNameList.add(field.getName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        for (Field field : fieldList) {

            Class<?> fieldType = field.getType();
            if (
                /* 不注入静态字段 */     Modifier.isStatic(field.getModifiers())  ||
                /* 不注入final字段 */    Modifier.isFinal(field.getModifiers())  ||
                /* 不注入基本类型字段 */  fieldType.isPrimitive()                 ||
                /* 不注入数组类型字段 */  fieldType.isArray()) {
                continue;
            }

            field.setAccessible(true);

            try {
                new ViewInjector().inject(contentView, ownerObject, field);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            try {
                new IntentInjector().inject(activity, ownerObject, field);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

//            try {
//                new BeanInjector().inject(null, ownerObject, field);
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }

        }

        new ViewInjector().injectEvent(ownerObject, contentView);

    }

    /**
     * 普通的对象注入调用该方法即可注入
     * 将不支持 @IntentInject
     * @param ownerObject       注入所处的对象
     * @param contentView  View的父容器
     */
    public static void inject(Object ownerObject, View contentView) {
        if(ownerObject == null) return;

        // 加载ViewInject
        Class currentClass = ownerObject.getClass();
        List<Field> fieldList = new ArrayList<>();
        List<String> fieldNameList = new ArrayList<>();
        while(Object.class != currentClass) {
            Field[] fields = currentClass.getDeclaredFields();
            if(fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if(fieldNameList.contains(field.getName())) continue;
                    fieldList.add(field);
                    fieldNameList.add(field.getName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        for (Field field : fieldList) {

            Class<?> fieldType = field.getType();
            if (
                /* 不注入静态字段 */     Modifier.isStatic(field.getModifiers())  ||
                /* 不注入final字段 */    Modifier.isFinal(field.getModifiers())  ||
                /* 不注入基本类型字段 */  fieldType.isPrimitive()                 ||
                /* 不注入数组类型字段 */  fieldType.isArray()) {
                continue;
            }

            field.setAccessible(true);

            try {
                new ViewInjector().inject(contentView, ownerObject, field);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

//            try {
//                new BeanInjector().inject(null, ownerObject, field);
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }

        }

        new ViewInjector().injectEvent(ownerObject, contentView);

    }

    /**
     * 带intent对象绑定和试图注入，要传activity
     * @param activity
     */
    public static void inject(Activity activity) {

        long start = System.currentTimeMillis();
        try {
            new ContentViewInjector().inject(activity, null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        LogCat.d("Time", activity.getClass().getName() + " SetContentView 耗时:" + (System.currentTimeMillis() - start));

        if(activity == null) return;

        Class currentClass = activity.getClass();
        List<Field> fieldList = new ArrayList<>();
        List<String> fieldNameList = new ArrayList<>();
        while(Object.class != currentClass) {
            Field[] fields = currentClass.getDeclaredFields();
            if(fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if(fieldNameList.contains(field.getName())) continue;
                    fieldList.add(field);
                    fieldNameList.add(field.getName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        for (Field field : fieldList) {

            Class<?> fieldType = field.getType();
            if (
                /* 不注入静态字段 */     Modifier.isStatic(field.getModifiers())  ||
                /* 不注入final字段 */    Modifier.isFinal(field.getModifiers())  ||
                /* 不注入基本类型字段 */  fieldType.isPrimitive()                 ||
                /* 不注入数组类型字段 */  fieldType.isArray()) {
                continue;
            }

            field.setAccessible(true);

            try {
                new ViewInjector().inject(activity, activity, field);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            try {
                new IntentInjector().inject(activity, activity, field);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        new ViewInjector().injectEvent(activity);

    }

}
