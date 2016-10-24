package com.honestwalker.android.commons.jscallback.jsFactory;

import android.content.Context;

import com.google.gson.Gson;
import com.honestwalker.android.commons.jscallback.bean.JSActionParamBean;

/**
 * Created by honestwalker on 15-6-17.
 */
public class JSExecutorFactory {

    enum ActionType{

        OPEN_URL("open_url"),
        SHARE("share"),
        ALERT("alert"),
        CLOSELOADING("closeLoading");

        private String value;
        ActionType(String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static void execute(Context context,String json){

        Gson gson = new Gson();
        JSActionParamBean actionParamBean = gson.fromJson(json, JSActionParamBean.class);

        getExecutor(actionParamBean.getAction()).parse(context,json);
    }

    private static JSActionAbstractExecutor getExecutor(String action){

        switch (getAction(action)){
            case OPEN_URL:
                return new OpenUrlActionExecutor();
            case SHARE:
                return new ShareActionExecutor();
            case ALERT:
                return new AlertActionExecutor();
            case CLOSELOADING:
                return new closeLoadingActionExecutor();
            default:
                return null;
        }
    }

    private static ActionType getAction(String action){
        for (ActionType actionType : ActionType.values()) {
            if (action.equals(actionType.toString())) {
                return actionType;
            }
        }
        throw new RuntimeException("没有"+action+"类型的Action");
    }

}
