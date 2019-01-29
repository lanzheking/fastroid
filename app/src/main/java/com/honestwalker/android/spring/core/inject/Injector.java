package com.honestwalker.android.spring.core.inject;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Created by lanzhe on 17-7-24.
 */

interface Injector {

    void inject(Activity activity, Object object, Field field) throws IllegalAccessException, InstantiationException;

}
