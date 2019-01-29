package com.honestwalker.android.spring.core.inject;

import android.app.Activity;
import android.util.Log;

import com.honestwalker.android.spring.core.annotation.ContentView;
import com.honestwalker.androidutils.IO.LogCat;

import java.lang.reflect.Field;

/**
 * Created by lanzhe on 17-7-24.
 */
class ContentViewInjector implements Injector {
    @Override
    public void inject(Activity activity, Object object, Field field) throws IllegalAccessException {
        ContentView contentView = activity.getClass().getAnnotation(ContentView.class);

        if(contentView == null) {
            Class currentClass = activity.getClass().getSuperclass();
            while(Object.class != currentClass) {
                contentView = (ContentView) currentClass.getAnnotation(ContentView.class);
                if(contentView != null) {
                    break;
                }
                currentClass = currentClass.getSuperclass();
            }
        }

        if(contentView == null) return;
        activity.setContentView(contentView.value());
        Log.d("Spring", "contentView=" + contentView.value());
    }
}
