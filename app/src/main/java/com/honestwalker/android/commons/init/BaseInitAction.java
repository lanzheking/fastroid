package com.honestwalker.android.commons.init;

import com.honestwalker.androidutils.Init.InitAction;

/**
 * Created by honestwalker on 15-9-16.
 */
public abstract class BaseInitAction implements InitAction {

    private String proccessName;

    private boolean isMainProccess;

    public String getProccessName() {
        return proccessName;
    }
    public boolean isMainProccess() {
        return isMainProccess;
    }

}
