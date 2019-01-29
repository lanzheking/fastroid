package com.honestwalker.android.spring.core.inject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.honestwalker.android.spring.core.annotation.IntentInject;

import java.lang.reflect.Field;

/**
 * Created by lanzhe on 17-7-24.
 */

class IntentInjector implements Injector {

    /**
     *
     * @param activity 参数或参数所在的对象所属于的activity
     * @param handler  参数所属于的对象
     * @param field    参数field
     * @throws IllegalAccessException
     */
    @Override
    public void inject(Activity activity, Object handler, Field field) throws IllegalAccessException {
        IntentInject intentInject = field.getAnnotation(IntentInject.class);
        if(intentInject == null) return;
        Intent intent = activity.getIntent();
        String name = "";
        if(intentInject.name() != null && !intentInject.name().trim().equals("")) {
            name = intentInject.name();
        } else {
            name = field.getName();
        }
        if(intent.hasExtra(name)) {
            Object intentBean = intent.getExtras().get(name);
            Log.d("Spring", "intentInject fieldName=" + name + "  " + intentBean);
            field.set(handler, intentBean);
        }
    }

}
