package com.honestwalker.android.kc_test.models;

/**
 * Created by lanzhe on 16-10-27.
 */
public class Task {

    private String event;

    private String desc;

    private String value;

    private String nextAction;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        if(value != null)
            return value.trim();
        else
            return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }
}
