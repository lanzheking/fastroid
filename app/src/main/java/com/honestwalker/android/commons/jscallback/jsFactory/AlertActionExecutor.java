package com.honestwalker.android.commons.jscallback.jsFactory;

import android.content.Context;

import com.honestwalker.android.commons.jscallback.bean.AlertParamBean;
import com.honestwalker.androidutils.window.DialogHelper;

/**
 * Created by honestwalker on 15-6-17.
 */
public class AlertActionExecutor extends JSActionAbstractExecutor<AlertParamBean>{
    @Override
    public void execute(Context context,AlertParamBean alertParamBean) {

        DialogHelper.alert(context, alertParamBean.getTitle(), alertParamBean.getContent());

    }
}
