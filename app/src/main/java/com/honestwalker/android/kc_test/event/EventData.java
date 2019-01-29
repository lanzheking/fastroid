package com.honestwalker.android.kc_test.event;

import android.app.Activity;

import com.honestwalker.android.kc_test.models.Task;

/**
 * Created by lanzhe on 16-11-11.
 */
public class EventData {

    private Activity context;

    private Class rClass;

    private Task task;

    public EventData() {}

    public EventData(EventData eventData) {
        this.setContext(eventData.getContext());
        this.setTask(eventData.getTask());
        this.setrClass(eventData.getrClass());
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public Class getrClass() {
        return rClass;
    }

    public void setrClass(Class rClass) {
        this.rClass = rClass;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
