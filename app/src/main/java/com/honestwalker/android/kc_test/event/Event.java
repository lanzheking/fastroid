package com.honestwalker.android.kc_test.event;

import com.honestwalker.android.kc_test.actions.KCTestBaseAction;

/**
 * Created by lanzhe on 16-11-11.
 */
public class Event {

    private String name;

    private KCTestBaseAction action;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KCTestBaseAction getAction() {
        return action;
    }

    public void setAction(KCTestBaseAction action) {
        this.action = action;
    }

}
