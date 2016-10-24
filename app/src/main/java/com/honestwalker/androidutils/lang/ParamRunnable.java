package com.honestwalker.androidutils.lang;

import com.honestwalker.androidutils.net.Parameter;

/**
 * Created by honestwalker on 16-1-21.
 */
public abstract class ParamRunnable implements Runnable {

    private Object[] args;

    public ParamRunnable(Object... args) {
        this.args = args;
    }

    public abstract void run(Object... args);

    @Override
    public void run() {
        run(args);
    }
}
