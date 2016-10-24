package com.honestwalker.androidutils.Annotation;

import com.honestwalker.androidutils.commons.adapter.BeanHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by honestwalker on 15-11-19.
 */
public class AnnotationUtil {
    public static void getAnnoValue(Object obj , Class annoClass) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field f : fields) {
            f.setAccessible(true);
            Annotation beanHolder = f.getAnnotation(annoClass);
            if (beanHolder != null) {

            }
        }
    }
}
