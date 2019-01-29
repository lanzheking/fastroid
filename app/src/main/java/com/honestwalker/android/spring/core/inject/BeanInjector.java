//package com.honestwalker.android.spring.core.inject;
//
//import android.app.Activity;
//import android.util.Log;
//
//import com.honestwalker.android.spring.core.annotation.Autowired;
//
//import java.lang.reflect.Field;
//
///**
// * Created by lanzhe on 17-7-24.
// */
//class BeanInjector implements Injector {
//
//    @Override
//    public void inject(Activity activity, Object object, Field field) throws InstantiationException, IllegalAccessException {
//        Autowired autowired = field.getAnnotation(Autowired.class);
//        if(autowired == null) return;
//        Class fieldType = field.getType();
//        Log.d("Spring", "fieldType=" + fieldType);
//        Object instance = fieldType.newInstance();
//        field.set(object, instance);
//    }
//
//}
