package com.honestwalker.android.kc_test.actions;

import com.honestwalker.android.kc_test.models.Task;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.lang.reflect.Method;

/**
 * Created by lanzhe on 16-11-3.
 */
public class MethodAction extends KCTestBaseAction {

    public void doAction(final Object obj,final Task task) {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                uiAction(obj, task);
            }
        });
    }

    private void uiAction(Object obj, Task task) {
        String methodCall = task.getValue();
        String methodName = methodCall.substring(0, methodCall.indexOf("("));

        LogCat.d("tester", "调用方法： " + methodName);

        String valuesLine = methodCall.substring(methodCall.indexOf("(") + 1, methodCall.indexOf(")"));
        Class valuesType[] = null;
        String valuesStr[] = valuesLine.split("[,]");

        Object values[] = null;

        if(valuesStr.length > 0) {
            valuesType = new Class[valuesStr.length];
            values = new Object[valuesStr.length];
            for (int i = 0; i < valuesStr.length; i++) {
                String value = valuesStr[i].trim();
                if(isString(value)) {
                    valuesType[i] = String.class;
                    values[i] = value;
                } else if (isInt(value)) {
                    valuesType[i] = Integer.class;
                    values[i] = Integer.parseInt(value);
                }

                LogCat.d("tester", "参数类型： " + valuesType[i] + "  值： " + values[i]);

            }

        }
        try {
            final Method method = obj.getClass().getDeclaredMethod(methodName, valuesType);

            method.setAccessible(true);
            method.invoke(obj, values);

        } catch (Exception e) {
            ExceptionUtil.showException("tester", e);
        }
    }

    private boolean isString(String value) {
        return value.startsWith("\"") && value.endsWith("\"");
    }

    private boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {}
        return false;
    }

}
