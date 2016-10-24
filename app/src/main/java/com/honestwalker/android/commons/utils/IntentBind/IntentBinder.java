package com.honestwalker.android.commons.utils.IntentBind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Intent 参数 与 Activity field 绑定器
 * Created by honestwalker on 15-10-10.
 */
public class IntentBinder {

    private Intent intent;
    private Context context;

    /**
     * 执行Itent 参数绑定activity field
     */
    public void doIntentBind(Activity context) {
        this.context = context;
        this.intent = context.getIntent();
        try{
            Class c = context.getClass();
            Annotation classAnno = c.getAnnotation(IntentBind.class); // activity包含@IntentBind注解就自动匹配所有field
            Field[] fields = c.getDeclaredFields();
            LogCat.d("INTENT", "获取 " + c.getName() + " 的字段:");
            for(Field field : fields) {
                field.setAccessible(true);
                LogCat.d("INTENT" , "field=" + field.getName());
                if(classAnno != null) {
                    bindField(field);
                } else {

                    // 如果字段包含IntentBind注解 ， 自动帮顶
                    Annotation fIntentBind = field.getAnnotation(IntentBind.class);
                    if(fIntentBind != null) {
                        bindField(field);
                    }
                }

            }
        } catch (Exception e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * 绑定字段
     * @param field
     */
    private void bindField(Field field) {

        // intent 不包含对应字段就过滤掉
        if(!intent.hasExtra(field.getName())) return;

        Object paramInIntent = intent.getExtras().get(field.getName());
        try {
            field.set(this.context , paramInIntent);
        } catch (Exception e) {}

    }

}
