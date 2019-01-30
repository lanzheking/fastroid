package com.honestwalker.android.commons.jscallback.actions;

import com.honestwalker.android.commons.jscallback.annotation.PreventRepeating;
import com.honestwalker.android.spring.context.ApplicationContext;
import com.honestwalker.android.spring.context.ApplicationContextUtils;
import com.honestwalker.android.spring.core.bean.SpringBean;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于JS API Action 的防止连续执行
 * Created by lanzhe on 18-4-28.
 */

class PreventRepeatingManager {

    private static Map<String, Long> preventRepeatMapping = new HashMap<>();

    public static void recordExecute(String action) {
        SpringBean springBean = getSpringBean(action);
        if(springBean == null) return;
        try {
            Class beanClass = Class.forName(springBean.getClassPath());
            PreventRepeating preventRepeating = (PreventRepeating) beanClass.getAnnotation(PreventRepeating.class);
            if(preventRepeating == null) return;
            preventRepeatMapping.put(action, System.currentTimeMillis());
        } catch (Exception e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * 触发间隔是否满足条件
     * @param action
     * @return
     */
    public static boolean isRepeating(String action) {
        if(!preventRepeatMapping.containsKey(action)) {
            recordExecute(action);
            return false;
        }
        long now = System.currentTimeMillis();
        SpringBean springBean = getSpringBean(action);
        Long executeTime = preventRepeatMapping.get(action);
        try {
            Class beanClass = Class.forName(springBean.getClassPath());
            PreventRepeating preventRepeating = (PreventRepeating) beanClass.getAnnotation(PreventRepeating.class);

            if(now - executeTime <= preventRepeating.value()) {
                return true;
            }
        } catch (Exception e) {
        }
        preventRepeatMapping.remove(action);
        recordExecute(action);
        return false;
    }

    private static SpringBean getSpringBean(String action) {
        SpringBean springBean = ApplicationContextUtils.getApplicationContext().getSpringBean(action);
        return springBean;
    }

}
