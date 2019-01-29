package com.honestwalker.android.kc_test.models;

import java.util.Queue;

/**
 * Created by lanzhe on 16-10-27.
 */
public class Action {

    private String actid;

    private Queue<Task> tasks;

    public String getActid() {
        return actid;
    }

    public void setActid(String actid) {
        this.actid = actid;
    }

    public Queue<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Queue<Task> tasks) {
        this.tasks = tasks;
    }
}
