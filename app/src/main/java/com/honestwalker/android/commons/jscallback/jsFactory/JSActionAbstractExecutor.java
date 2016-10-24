package com.honestwalker.android.commons.jscallback.jsFactory;

import android.content.Context;

import com.google.gson.Gson;
import com.honestwalker.android.commons.jscallback.bean.JSActionParamBean;

import java.lang.reflect.ParameterizedType;

/**
 * Created by honestwalker on 15-6-17.
 */
public abstract class JSActionAbstractExecutor<T extends JSActionParamBean> {

    public abstract void execute(Context context,T t);

    public void parse(Context context,String json){

        Class<T> actionClass = (Class<T>) ((ParameterizedType)(getClass().getGenericSuperclass())).getActualTypeArguments()[0];
        T t = new Gson().fromJson(json,actionClass);
        execute(context,t);
    }
}
