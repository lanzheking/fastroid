package com.honestwalker.android.spring.core.inject;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.honestwalker.android.spring.core.annotation.ContentView;
import com.honestwalker.android.spring.core.annotation.event.EventBase;
import com.honestwalker.androidutils.IO.LogCat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by lanzhe on 17-7-24.
 */
public class ViewInjector implements Injector {

    public void inject(View contentView, Object object, Field field) throws IllegalAccessException {
        ViewInject viewInject = field.getAnnotation(ViewInject.class);
        if(viewInject == null) return;
        Object view = contentView.findViewById(viewInject.value());
        Log.d("Spring", "view=" + view);
        field.set(object, view);
    }

    @Override
    public void inject(Activity activity, Object object, Field field) throws IllegalAccessException {
        ViewInject viewInject = field.getAnnotation(ViewInject.class);
        if(viewInject == null) return;
        Object view = activity.findViewById(viewInject.value());
        Log.d("Spring", "view=" + view);
        field.set(activity, view);
    }

    public void injectEvent(Activity activity) {
        // inject event
        Method[] methods = activity.getClass().getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {

                LogCat.d("TEST", "11111111111 " + method.getName());
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                LogCat.d("TEST", "2222222222222");

                //检查当前方法是否是event注解的方法
                EventBase baseEvent = null;
                Annotation[] annotations = method.getAnnotations();
                if(annotations == null || annotations.length == 0) continue;
                Object event = null;
                for (Annotation annotation : annotations) {
                    baseEvent = annotation.annotationType().getAnnotation(EventBase.class);
                    if(baseEvent != null) {
                        event = annotation;
                        break;
                    }
                }
                if (event != null) {
                    try {
                        // id参数
//                        int[] values = event.value();
                        int[] values = (int[]) callMethod(event, "value");
//                        int[] parentIds = event.parentId();
                        int[] parentIds = (int[]) callMethod(event, "parentId");
                        int parentIdsLen = parentIds == null ? 0 : parentIds.length;
                        //循环所有id，生成ViewInfo并添加代理反射
                        for (int i = 0; i < values.length; i++) {
                            int value = values[i];
                            if (value > 0) {
                                ViewInfo info = new ViewInfo();
                                info.value = value;
                                info.parentId = parentIdsLen > i ? parentIds[i] : 0;
                                method.setAccessible(true);
                                EventListenerManager.addEventMethod(new ViewFinder(activity), info, baseEvent, activity, method);
                            }
                        }
                    } catch (Throwable ex) {
                    }
                }
            }
        } // end inject event
    }

    public void injectEvent(Object handler, View containerView) {
        // inject event
        Method[] methods = handler.getClass().getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {

                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                //检查当前方法是否是event注解的方法
                EventBase baseEvent = null;
                Annotation[] annotations = method.getAnnotations();
                if(annotations == null || annotations.length == 0) continue;
                Object event = null;
                for (Annotation annotation : annotations) {
                    baseEvent = annotation.annotationType().getAnnotation(EventBase.class);
                    if(baseEvent != null) {
                        event = annotation;
                        break;
                    }
                }
                if (event != null) {
                    try {
                        // id参数
                        int[] values = (int[]) callMethod(event, "value");
                        int[] parentIds = (int[]) callMethod(event, "parentId");
                        int parentIdsLen = parentIds == null ? 0 : parentIds.length;
                        //循环所有id，生成ViewInfo并添加代理反射
                        for (int i = 0; i < values.length; i++) {
                            int value = values[i];
                            if (value > 0) {
                                ViewInfo info = new ViewInfo();
                                info.value = value;
                                info.parentId = parentIdsLen > i ? parentIds[i] : 0;
                                method.setAccessible(true);
                                EventListenerManager.addEventMethod(new ViewFinder(containerView), info, baseEvent, handler, method);
                            }
                        }
                    } catch (Throwable ex) {
                    }
                }
            }
        } // end inject event
    }

    private Object callMethod(Object object, String methodName) {
        try {
            Method method = object.getClass().getDeclaredMethod(methodName);
            Object value = method.invoke(object);
            return value;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
